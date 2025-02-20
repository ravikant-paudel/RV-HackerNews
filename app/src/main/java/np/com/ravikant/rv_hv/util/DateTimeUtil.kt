package np.com.ravikant.rv_hv.util

import java.util.concurrent.TimeUnit

class DateTimeUtil {

    companion object {
        fun getRelativeTime(timestamp: Long): String {
            val now = System.currentTimeMillis() / 1000 // Convert current time to seconds
            val diff = now - timestamp  // Difference in seconds

            return when {
                diff >= TimeUnit.DAYS.toSeconds(5) -> "5d+" // More than 5 days
                diff >= TimeUnit.DAYS.toSeconds(1) -> "${diff / TimeUnit.DAYS.toSeconds(1)}d" // Days
                diff >= TimeUnit.HOURS.toSeconds(1) -> "${diff / TimeUnit.HOURS.toSeconds(1)}h" // Hours
                diff >= TimeUnit.MINUTES.toSeconds(1) -> "${diff / TimeUnit.MINUTES.toSeconds(1)}m" // Minutes
                else -> "${diff}s" // Seconds
            }
        }
    }
}