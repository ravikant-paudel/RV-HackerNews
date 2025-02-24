package np.com.ravikant.rv_hv.feature.detail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.ScreenState
import np.com.ravikant.rv_hv.feature.detail.data.DetailRepository
import org.jsoup.Jsoup
import org.jsoup.nodes.Element


class DetailViewModel : ViewModel() {

    private val _detailState = MutableStateFlow(DetailState())
    val detailState: StateFlow<DetailState> = _detailState.asStateFlow()

    fun fetchDetailApiCall(storyId: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val comments = withContext(Dispatchers.IO) {
                    val detailResponse = Jsoup.connect("https://news.ycombinator.com/item?id=$storyId").get()
                    val commentElements = detailResponse.select(".athing.comtr") // Selects all comment threads

                    // Extract comments inside IO thread to avoid main thread overload
                    commentElements.map { extractComments(it) }
                }

                // Convert to JSON (runs on Main thread, but it's lightweight)
                val jsonOutput = Json.encodeToString(comments)

                _detailState.value = _detailState.value.copy(
                    ScreenState.SUCCESS,
                    list = comments,
                )

                // Print JSON
                println(jsonOutput)

            } catch (e: Exception) {
                e.printStackTrace()
                _detailState.value = _detailState.value.copy(
                    ScreenState.ERROR,
                )
            }
        }
    }

    private fun extractComments(commentElement: Element, level: Int = 1): DetailData {
        val id = commentElement.attr("id").toIntOrNull() ?: commentElement.hashCode()
        val author = commentElement.select(".hnuser").text()
        val text = commentElement.select(".comment").text()

        // Extract indent value
        val index = commentElement.select("td.ind").attr("indent").toIntOrNull() ?: 0

        // Extract time
        val time = commentElement.select(".age").firstOrNull()?.text() ?: ""

        val kids = mutableListOf<Int>()

        // Extract replies (recursive)
        val replyElements = commentElement.select(".reply .athing.comtr")
        val replies = replyElements.map { extractComments(it, level + 1) }

        return DetailData(
            id = id,
            by = author,
            text = text,
            timeString = time,
            index = index,
            kids = kids,
            replies = replies
        )
    }

}

