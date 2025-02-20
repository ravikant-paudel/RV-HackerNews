package np.com.ravikant.rv_hv.feature.landing

import kotlinx.serialization.Serializable

@Serializable
data class LandingData(
    val by: String = "",
    val descendants: Int = 0,
    val id: Int = 0,
    val score: Int = 0,
    val time: Long = 0,
    val title: String = "",
    val type: String = "",
    val url: String = "",
    var iconUrl : String =""
)
