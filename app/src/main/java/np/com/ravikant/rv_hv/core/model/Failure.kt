package np.com.ravikant.rv_hv.core.model



sealed class Failure: Throwable() {
    data class API(override val message: String): Failure()
    data class Unauthorized(override val message: String): Failure()
    data class Unknown(override val message: String): Failure()
}