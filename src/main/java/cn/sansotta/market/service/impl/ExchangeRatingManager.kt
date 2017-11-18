package cn.sansotta.market.service.impl

import cn.sansotta.market.common.millisOfDays
import cn.sansotta.market.service.ExchangeRatingService
import com.jayway.jsonpath.JsonPath
import org.apache.http.HttpStatus.SC_OK
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpGet
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory
import org.apache.http.impl.client.HttpClients
import org.apache.http.ssl.SSLContextBuilder
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import kotlin.concurrent.fixedRateTimer

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Service
class ExchangeRatingManager : ExchangeRatingService {
    @Volatile private var rating = 0.0
    @Volatile private var updating = true
    private val timer =
            fixedRateTimer("daily update", true, 1000L, millisOfDays(1))
            { this@ExchangeRatingManager.updateRating() }
    private val url = "https://api.fixer.io/latest?base=USD&symbols=CNY"
    private val httpClient: HttpClient

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        val sslContext = SSLContextBuilder().apply { loadTrustMaterial(null) { _, _ -> true } }.build()
        val connFactory = SSLConnectionSocketFactory(sslContext,
                arrayOf("SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"), null,
                NoopHostnameVerifier.INSTANCE)
        httpClient = HttpClients.custom().setSSLSocketFactory(connFactory).build()
    }

    override fun getRating(): Double {
        while (updating) {
        }
        return rating
    }

    private fun updateRating() {
        if (logger.isInfoEnabled)
            logger.info("Daily update exchange rating...")

        updating = true
        var rating: Double?
        while (true) {
            rating = fetchRating()
            if (rating != null) {
                this.rating = rating
                break
            }
        }
        updating = false
        if (logger.isInfoEnabled)
            logger.info("Daily exchange rating update succeed! Today rating is $rating.")
    }

    private fun fetchRating() =
            try {
                fetch()?.let(this::parseJson)
            } catch (ex: Exception) {
                ex.printStackTrace()
                null
            }

    private fun parseJson(json: String) = JsonPath.read<Double>(json, "$.rates.CNY")

    private fun fetch(): String? {
        return try {
            val response = httpClient.execute(HttpGet(url))
                    ?.takeIf { it.statusLine.statusCode == SC_OK } ?: return null
            val content = response.entity?.let { EntityUtils.toString(it) } ?: return null
            EntityUtils.consume(response.entity)
            content
        } catch (ex: Exception) {
            null
        }
    }
}

fun main(vararg args: String) {
    println(ExchangeRatingManager().getRating())
}