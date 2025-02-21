package np.com.ravikant.rv_hv.feature.detail.data


import np.com.ravikant.rv_hv.core.ApiClient
import np.com.ravikant.rv_hv.feature.detail.DetailData

class DetailService {
    private val apiClient = ApiClient

    suspend fun fetchDetailFromId(id: Int): DetailData {
        return apiClient.get<DetailData>("item/$id.json")
    }
}