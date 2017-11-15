package cn.sansotta.market.service.impl

import cn.sansotta.market.common.millisOfDays
import cn.sansotta.market.service.ExchangeRatingService
import com.jayway.jsonpath.JsonPath
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.concurrent.fixedRateTimer

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
class ExchangeRatingManager : ExchangeRatingService {
    @Volatile private var rating = 0.0
    @Volatile private var updating = true
    private val url = URL("https://api.fixer.io/latest?base=USD&symbols=CNY")
    private val timer =
            fixedRateTimer("update exchange rates", true, 1000L, millisOfDays(1))
            { this@ExchangeRatingManager.updateRating() }
    private val sslFactory: SSLSocketFactory

    init {
        val sslContext = SSLContext.getInstance("TLSv1.2")
        sslContext.init(null,
                arrayOf(object : X509TrustManager {
                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}

                    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
                }),
                SecureRandom())
        sslFactory = sslContext.socketFactory
    }

    override fun getRating(): Double {
        while (updating) {
        }
        return rating
    }

    private fun updateRating() {
        updating = true
        while (true) fetchRating()?.let { rating = it } ?: break
        updating = false
    }

    private fun fetchRating() =
            try {
                fetch()?.let(this::parseJson)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

    private fun parseJson(json: String) = JsonPath.read<Double>(json, "$.rates.CNY")

    private fun fetch() =
            try {
                val conn = url.openConnection() as HttpsURLConnection
                conn.sslSocketFactory = sslFactory
                conn.requestMethod = "GET"
                conn.connect()
                if (conn.responseCode == -1) throw Exception()
                conn.responseMessage
                val reader = conn.inputStream.bufferedReader()
                reader.useLines { lines ->
                    lines.joinToString(separator = "")
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }
}

fun main(vararg args: String) {
    println(ExchangeRatingManager().getRating())
}