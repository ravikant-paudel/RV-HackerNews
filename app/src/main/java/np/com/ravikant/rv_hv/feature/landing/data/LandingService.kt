package np.com.ravikant.rv_hv.feature.landing.data

import np.com.ravikant.rv_hv.core.ApiClient
import np.com.ravikant.rv_hv.core.URL_TOP
import np.com.ravikant.rv_hv.feature.landing.LandingData

class LandingService {
    private val apiClient = ApiClient

    suspend fun listLandingIds(): List<Int> {
        return apiClient.get<List<Int>>(URL_TOP)
    }

    suspend fun fetchDetailFrmId(id: Int): LandingData {
        return apiClient.get<LandingData>("item/$id.json")
    }
}