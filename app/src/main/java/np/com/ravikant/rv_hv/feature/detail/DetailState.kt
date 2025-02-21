package np.com.ravikant.rv_hv.feature.detail

import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.landing.LandingData

data class DetailState(
    val screenState: ScreenState = ScreenState.LOADING,
    val list: List<DetailData> = emptyList()
)
