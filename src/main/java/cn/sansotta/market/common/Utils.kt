@file:JvmName("Utils")

package cn.sansotta.market.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.pagehelper.PageInfo
import org.slf4j.Logger
import java.nio.charset.Charset
import java.util.concurrent.Executors

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

fun ObjectMapper.getCollectionType(collectionType: Class<out Collection<*>>, elementType: Class<*>)
        = typeFactory.constructCollectionType(collectionType, elementType)

inline fun <reified C : Collection<*>, reified E>
        ObjectMapper.getCollectionType() = getCollectionType(C::class.java, E::class.java)

fun <T, R> copyPageInfo(pageInfo: PageInfo<T>, listConverter: (T) -> R) =
        PageInfo<R>().apply {
            pageNum = pageInfo.pageNum
            pageSize = pageInfo.pageSize
            size = pageInfo.size
            startRow = pageInfo.startRow
            endRow = pageInfo.endRow
            total = pageInfo.total
            pages = pageInfo.pages
            prePage = pageInfo.prePage
            nextPage = pageInfo.nextPage
            navigatePages = pageInfo.navigatePages
            navigatepageNums = pageInfo.navigatepageNums
            navigateFirstPage = pageInfo.navigateFirstPage
            navigateLastPage = pageInfo.navigateLastPage
            isIsFirstPage = pageInfo.isIsFirstPage
            isIsLastPage = pageInfo.isIsLastPage
            isHasPreviousPage = pageInfo.isHasPreviousPage
            isHasNextPage = pageInfo.isHasNextPage
            list = pageInfo.list.map { listConverter(it) }
        }

inline fun <T> hazard(logger: Logger, method: String, defaultVal: T, func: () -> T) =
        try {
            func()
        } catch (ex: RuntimeException) {
            logger.error("Exception when $method caused by $ex")
            defaultVal
        }

fun string2ByteArray(str: String) = str.split(" ").map { it.toByte() }.toByteArray()

fun Boolean.ifTrue(func: () -> Any): Boolean {
    if (!this) return false
    func()
    return true
}

fun Double.toMoneyAmount() = String.format("%.2f", this)

fun String.getBytes(charset: Charset): ByteArray {
    val stream = this.byteInputStream(charset)
    return stream.readBytes(stream.available())
}

fun millisOfDays(day: Int) = 1000L * 60 * 60 * 24 * day

val commonPool = Executors.newFixedThreadPool(10)

inline fun retry(times: Int = 5, func: () -> Boolean): Boolean {
    for (i in 0..times) if (safe(func) ?: continue) return true

    return false
}

inline fun <T> retry(times: Int = 5, judge: (T) -> Boolean, func: () -> T): T? {
    for (i in 0..times) return safe(func)?.takeIf(judge) ?: continue
    return null
}

inline fun <T> safe(func: () -> T) =
        try {
            func()
        } catch (ex: Exception) {
            null
        }
