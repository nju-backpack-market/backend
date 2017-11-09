package cn.sansotta.market.configuration

import com.paypal.base.rest.APIContext

/**
 * @author [tinker](mailto:tinker19981@hotmail.com)
 */
class PayPalApiContextFactory(private val clientId: String,
                              private val secret: String,
                              private val mode: String) {
    fun apiContext() = APIContext(clientId, secret, mode)
}
