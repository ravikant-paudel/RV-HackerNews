package np.com.ravikant.rv_hv.util

import java.util.concurrent.TimeUnit

class DateTimeUtil {

    companion object {
        fun getRelativeTime(timestamp: Long): String {
            val now = System.currentTimeMillis() / 1000 // Convert current time to seconds
            val diff = now - timestamp  // Difference in seconds

            return when {
                diff >= TimeUnit.DAYS.toSeconds(5) -> "5+ days" // More than 5 days
                diff >= TimeUnit.DAYS.toSeconds(1) -> "${diff / TimeUnit.DAYS.toSeconds(1)}days" // Days
                diff >= TimeUnit.HOURS.toSeconds(1) -> "${diff / TimeUnit.HOURS.toSeconds(1)}hrs" // Hours
                diff >= TimeUnit.MINUTES.toSeconds(1) -> "${diff / TimeUnit.MINUTES.toSeconds(1)}min" // Minutes
                else -> "${diff}sec" // Seconds
            }
        }
    }
}