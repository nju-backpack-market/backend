package cn.sansotta.market.common

import com.github.pagehelper.PageInfo
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

inline fun <reified T> T?.toResponse(status: HttpStatus = HttpStatus.OK)
        = ResponseEntity(this, status)

inline fun <reified T> methodOn() = ControllerLinkBuilder.methodOn(T::class.java)

inline fun <reified T> linkTo() = ControllerLinkBuilder.linkTo(T::class.java)

inline fun <reified T> EntityLinks.linkToSingleResource(id: Any?) =
        linkToSingleResource(T::class.java, id)

fun <T> pagedResources(pageInfo: PageInfo<T>) =
        PagedResources(
                pageInfo.list,
                PagedResources.PageMetadata(pageInfo.pageSize.toLong(), pageInfo.size.toLong(),
                        pageInfo.total, pageInfo.pages.toLong()))
