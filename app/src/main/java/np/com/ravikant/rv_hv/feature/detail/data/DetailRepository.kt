package np.com.ravikant.rv_hv.feature.detail.data

import np.com.ravikant.rv_hv.feature.detail.DetailData
import np.com.ravikant.rv_hv.feature.landing.LandingData

class DetailRepository {
    val detailService: DetailService = DetailService()

    suspend fun fetchDetailFromId(id: Int): DetailData {
        return detailService.fetchDetailFromId(id)
    }

}