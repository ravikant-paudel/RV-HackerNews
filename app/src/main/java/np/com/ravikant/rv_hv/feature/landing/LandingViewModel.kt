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

    init {
        fetchLandingApiCall()
    }

    private fun fetchLandingApiCall() {
        viewModelScope.launch {
            val idList = try {
                _repository.listLandingIds()
            } catch (e: Exception) {
                println("Error fetching ID list: ${e.message}")
                _landingState.value = _landingState.value.copy(screenState = ScreenState.ERROR)
                return@launch
            }

            if (idList.isNotEmpty()) {
                val detailList = coroutineScope {
                    idList.take(15).map { id ->
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

                _landingState.value = _landingState.value.copy(
                    list = detailList,
                    screenState = ScreenState.SUCCESS
                )
            }
        }
    }

}