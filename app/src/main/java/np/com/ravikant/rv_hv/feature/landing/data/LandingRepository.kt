package np.com.ravikant.rv_hv.feature.landing.data

import np.com.ravikant.rv_hv.feature.landing.LandingData

class LandingRepository {
    val service: LandingService = LandingService()

    suspend fun listLandingIds(): List<Int> {
        return service.listLandingIds()
    }

    suspend fun fetchDetailFrmId(id: Int): LandingData {
        return service.fetchDetailFrmId(id)
    }
}