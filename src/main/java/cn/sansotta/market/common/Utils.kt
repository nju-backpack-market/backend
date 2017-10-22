@file:JvmName("Utils")

package cn.sansotta.market.common

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.pagehelper.PageInfo

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
            setHasNextPage(pageInfo.isHasNextPage)
            list = pageInfo.list.map { listConverter(it) }
        }
