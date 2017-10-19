package cn.sansotta.market.domain

/**
 * Information about delivery.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class DeliveryInfo (
        /**
         * Customer's name.
         * */
        var name: String,
        /**
         * Customer's phone number.
         *  */
        var phoneNumber: String,
        /**
         * Customer's email.
         * */
        var email: String,
        /**
         * Customer's address.
         * */
        var address: String
) {
    constructor() : this("", "", "", "")
}