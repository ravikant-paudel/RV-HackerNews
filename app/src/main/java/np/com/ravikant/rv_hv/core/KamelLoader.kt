package np.com.ravikant.rv_hv.core

import org.jsoup.Jsoup

fun KamelLoader(url: String): String {
    return try {
        val doc = Jsoup.connect(url).get()
        val icon = doc.select("link[rel~=(?i)icon]").attr("href")
        if (icon.startsWith("http")) icon else "https://www.tuhs.org$icon"
    } catch (e: Exception) {
        ""
    }
}