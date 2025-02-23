package np.com.ravikant.rv_hv.core

import io.ktor.client.HttpClient
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import np.com.ravikant.rv_hv.core.model.Failure
import java.net.SocketException
import java.util.concurrent.TimeoutException

/**
 * Provides api client to access the api request
 *
 * This is singleton class
 */
class ApiClient {

    companion object {
        private var instance: ApiClient? = null
        val BASE_URL: String = "https://hacker-news.firebaseio.com/v0/"

        val client: HttpClient = HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        explicitNulls = false
                    }
                )
            }
            // logging
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            defaultRequest {
                url(BASE_URL)
            }
        }


        suspend inline fun <reified T> get(path: String): T = safeRequest {
            client.get(path).body<T>()
        }

        suspend inline fun <reified T> post(path: String, body: Any?): T = safeRequest {
            client.post(path) {
                setBody(body)
            }.body<T>()
        }

        suspend inline fun <reified T> update(path: String, body: Any?): T = safeRequest {
            client.put(path) {
                setBody(body)
            }.body<T>()
        }

        suspend fun delete(path: String) = safeRequest { client.delete(path) }

        inline fun <reified T> safeRequest(
            block: () -> T
        ): T =
            try {
                block()
            } catch (e: ClientRequestException) {
                when (e.response.status) {
                    HttpStatusCode.Unauthorized -> throw Failure.Unauthorized("Unauthorized")
                    HttpStatusCode.MethodNotAllowed -> throw Failure.API("Method not allowed")
                    else -> throw Failure.API("Status Code: ${e.response.status.value}")
                }
            } catch (e: ServerResponseException) {
                throw Failure.API("Server Error")
            } catch (e: TimeoutException) {
                throw Failure.API("Timeout")
            } catch (e: SocketException) {
                throw Failure.API("Timeout")
            } catch (e: NoTransformationFoundException) {
                throw Failure.Unknown("Unable to parse the response")
            } catch (e: Exception) {
                throw Failure.Unknown("${e.message}")
            }
    }


}