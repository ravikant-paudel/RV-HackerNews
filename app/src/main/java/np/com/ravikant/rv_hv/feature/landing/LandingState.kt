package np.com.ravikant.rv_hv.feature.landing

import np.com.ravikant.rv_hv.ScreenState

data class LandingState(
    val screenState: ScreenState = ScreenState.LOADING,
    val list: List<LandingData> = emptyList(),
    val isLoadingMore: Boolean = false
)
