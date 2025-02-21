package np.com.ravikant.rv_hv.feature.detail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class DetailData(
    @SerialName("author") val by: String = "",
    @Transient val descendants: Int = 0,
    val id: Int = 0,
    @Transient val score: Int = 0,
    @Transient val time: Long = 0,
    val kids: List<Int> = emptyList(),
    @Transient val parent: Int = 0,
    @Transient val title: String = "",
    @Transient val type: String = "",
    @Transient val url: String = "",
    val text: String = "",
    @Transient var iconUrl: String = "",
    val replies: List<DetailData> = emptyList()
)
