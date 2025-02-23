package np.com.ravikant.rv_hv.feature.detail

import kotlinx.serialization.Serializable

@Serializable
data class DetailData(
    val id: Int,
    val by: String? = null,
    val text: String? = null,
    val time: Long? = null,
    val kids: List<Int>?,
    val replies: List<DetailData> = emptyList()
)
