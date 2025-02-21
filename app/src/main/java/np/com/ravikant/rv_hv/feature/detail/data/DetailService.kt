package np.com.ravikant.rv_hv.feature.detail.data


import np.com.ravikant.rv_hv.core.ApiClient
import np.com.ravikant.rv_hv.feature.landing.LandingData

class DetailService {
    private val apiClient = ApiClient

    suspend fun fetchDetailFromId(id: Int): LandingData {
        return apiClient.get<LandingData>("item/$id.json")
    }
}