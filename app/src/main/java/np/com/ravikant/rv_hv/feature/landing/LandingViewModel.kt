package np.com.ravikant.rv_hv.feature.landing

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
import np.com.ravikant.rv_hv.feature.landing.data.LandingRepository
class LandingViewModel : ViewModel() {

    private val _landingState = MutableStateFlow(LandingState())
    val landingState: StateFlow<LandingState> = _landingState.asStateFlow()

    private val _repository: LandingRepository = LandingRepository()

    private var currentPage = 0
    private val pageSize = 15
    private var isFetching = false

    init {
        fetchLandingApiCall()
    }
    private fun fetchLandingApiCall() {
        if (isFetching) return
        isFetching = true

        // 🔹 Set `isLoadingMore = true` when loading more data
        _landingState.value = _landingState.value.copy(isLoadingMore = true)

        viewModelScope.launch {
            val idList = try {
                _repository.listLandingIds()
            } catch (e: Exception) {
                println("Error fetching ID list: ${e.message}")
                _landingState.value = _landingState.value.copy(
                    screenState = ScreenState.ERROR,
                    isLoadingMore = false
                )
                isFetching = false
                return@launch
            }

            if (idList.isNotEmpty()) {
                val startIndex = currentPage * pageSize
                val endIndex = minOf(startIndex + pageSize, idList.size)
                val paginatedIds = idList.subList(startIndex, endIndex)

                val detailList = coroutineScope {
                    paginatedIds.map { id ->
                        async(Dispatchers.IO) {
                            try {
                                _repository.fetchDetailFrmId(id)
                            } catch (e: Exception) {
                                println("Error fetching details for ID $id: ${e.message}")
                                null
                            }
                        }
                    }.awaitAll().filterNotNull()
                }

                val updatedList = if (currentPage == 0) detailList else _landingState.value.list + detailList
                _landingState.value = _landingState.value.copy(
                    list = updatedList,
                    screenState = ScreenState.SUCCESS,
                    isLoadingMore = false // 🔹 Reset `isLoadingMore` after loading
                )

                currentPage++
            } else {
                _landingState.value = _landingState.value.copy(isLoadingMore = false)
            }

            isFetching = false
        }
    }


    fun loadMore() {
        fetchLandingApiCall()
    }
}