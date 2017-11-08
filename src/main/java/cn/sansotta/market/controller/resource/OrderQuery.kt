package cn.sansotta.market.controller.resource

import cn.sansotta.market.domain.value.OrderStatus
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDate

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
data class OrderQuery(
        var id: Long?,
        var status: OrderStatus?,
        var fromDate: LocalDate?,
        var toDate: LocalDate?,
        var onDate: LocalDate?,
        var customerName: String?,
        var phoneNumber: String?,
        var productIds: List<Long>?,
        var fromPrice: Double?,
        var toPrice: Double?) {
    var queryId = -1

    constructor() : this(null, null, null, null, null, null, null, null, null, null)

    @JsonIgnore
    fun getRationalQuery(authorized: Boolean): OrderQuery? {
        if (!authorized && (customerName.isNullOrBlank() || phoneNumber.isNullOrBlank())) {
            queryId = -1
            return this
        }
        if (id != null) { // id has highest priority
            status = null
            fromDate = null
            toDate = null
            onDate = null
            customerName = null
            phoneNumber = null
            productIds = null
            fromPrice = null
            toPrice = null
            return this
        }
        if ((toDate != null || fromDate != null) && onDate != null)
            return null

        val tmpFromPrice = fromPrice
        val tmpToPrice = toPrice
        if (tmpFromPrice != null && tmpToPrice != null && tmpFromPrice > 0.0 && tmpFromPrice > tmpToPrice)
            return null

        val tmpFromDate = fromDate
        val tmpToDate = toDate
        if (tmpFromDate != null && tmpToDate != null && tmpFromDate.isAfter(tmpToDate))
            return null

        return this
    }
}