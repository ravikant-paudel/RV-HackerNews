package np.com.ravikant.rv_hv.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.detail.data.DetailRepository

class DetailViewModel : ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()
    private val _repository: DetailRepository = DetailRepository()

    fun fetchDetailApiCall(idx: Int) {
        viewModelScope.launch {
            val detailNews = try {
                _repository.fetchDetailFromId(idx)
            } catch (e: Exception) {
                println("Error fetching ID list: ${e.message}")
                _detailState.value = _detailState.value.copy(
                    screenState = ScreenState.ERROR,
                )
                return@launch
            }

            val kids = detailNews.kids ?: emptyList() // Ensure it's not null
            val commentApiList = fetchCommentsRecursively(kids)

            _detailState.value = _detailState.value.copy(
                list = commentApiList,
                screenState = ScreenState.SUCCESS
            )
        }
    }
    private suspend fun fetchCommentsRecursively(commentIds: List<Int>): List<DetailData> =
        coroutineScope {
            commentIds.map { id ->
                async(Dispatchers.IO) {
                    try {
                        val comment = _repository.fetchDetailFromId(id)
                        println("Fetched comment ID $id: $comment")

                        if (comment == null) {
                            println("Error: Comment ID $id returned null")
                            return@async null
                        }

                        // Fetch replies recursively
                        val replies = if (comment.kids.isNullOrEmpty()) {
                            println("Comment ID $id has no kids")
                            emptyList()
                        } else {
                            println("Fetching replies for comment ID $id: ${comment.kids}")
                            fetchCommentsRecursively(comment.kids)
                        }

                        println("Comment ID $id - Text: ${comment.text}")

                        DetailData(
                            id = id,
                            by = comment.by ?: "Unknown",
                            time = comment.time ?: 0,
                            type = comment.type ?: "comment",
                            text = comment.text ?: "(No Text)",
                            replies = replies
                        )
                    } catch (e: Exception) {
                        println("Error fetching comment ID $id: ${e.message}")
                        null
                    }
                }
            }.awaitAll().filterNotNull()
        }

}
