package np.com.ravikant.rv_hv.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.detail.data.DetailRepository
import kotlin.math.min


class DetailViewModel : ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()
    private val repository = DetailRepository()

    // Pagination state
    private var currentPage = 0
    private val pageSize = 2 // Number of top-level comments to load per page
    private var allKids: List<Int> = emptyList()

    fun fetchDetailApiCall(storyId: Int) {
        viewModelScope.launch {
            _detailState.value = _detailState.value.copy(screenState = ScreenState.LOADING)
            try {
                // Fetch the story to get the initial list of top-level comment IDs
                val story = repository.fetchDetailFromId(storyId)
                allKids = story.kids ?: emptyList()
                currentPage = 0
                // Load the first page of comments
                loadNextPage()
            } catch (e: Exception) {
                _detailState.value = _detailState.value.copy(screenState = ScreenState.ERROR)
            }
        }
    }

    fun loadNextPage() {
        viewModelScope.launch {
            val startIndex = currentPage * pageSize
            val endIndex = min((currentPage + 1) * pageSize, allKids.size)
            if (startIndex >= allKids.size) return@launch // No more comments to load

            // For each top-level comment, fetch only the comment details (no nested replies)
            val pageIds = allKids.subList(startIndex, endIndex)
            val comments = pageIds.mapNotNull { id -> fetchComment(id) }

            // Append the new comments to the current list
            _detailState.value = _detailState.value.copy(
                list = _detailState.value.list + comments,
                screenState = ScreenState.SUCCESS
            )
            currentPage++
        }
    }

    // Fetch only a single comment without recursively loading replies.
    // This minimizes memory usage by deferring nested data until needed.
    private suspend fun fetchComment(commentId: Int): DetailData? {
        return repository.fetchDetailFromId(commentId)?.copy(replies = emptyList())
    }

    fun loadRepliesForComment(commentId: Int) {
        viewModelScope.launch {
            // Recursively search for the comment in all levels of replies
            fun findComment(comments: List<DetailData>): DetailData? {
                for (comment in comments) {
                    if (comment.id == commentId) return comment
                    val foundInReplies = findComment(comment.replies)
                    if (foundInReplies != null) return foundInReplies
                }
                return null
            }

            val comment = findComment(_detailState.value.list) ?: return@launch
            val replies = comment.kids?.mapNotNull { kidId -> fetchComment(kidId) } ?: emptyList()

            // Update the comment's replies in the state
            fun updateList(comments: List<DetailData>): List<DetailData> {
                return comments.map {
                    if (it.id == commentId) it.copy(replies = replies)
                    else it.copy(replies = updateList(it.replies))
                }
            }

            _detailState.value = _detailState.value.copy(
                list = updateList(_detailState.value.list)
            )
        }
    }
}

