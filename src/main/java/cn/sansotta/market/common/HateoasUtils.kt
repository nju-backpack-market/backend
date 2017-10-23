@file:JvmName("HateoasUtils")

package cn.sansotta.market.common

import cn.sansotta.market.controller.resource.ResourceConverter
import cn.sansotta.market.controller.resource.ResourcesConverter
import com.github.pagehelper.PageInfo
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

const val HAL_MIME_TYPE = "application/hal+json"

fun <T> notFoundEntity() = ResponseEntity<T>(HttpStatus.NOT_FOUND)

@JvmOverloads
fun <T> T?.toResponse(status: HttpStatus = HttpStatus.OK) = ResponseEntity(this, status)

inline fun <reified T> methodOn() = ControllerLinkBuilder.methodOn(T::class.java)

inline fun <reified T> linkTo() = ControllerLinkBuilder.linkTo(T::class.java)

inline fun <reified T> EntityLinks.linkToSingleResource(id: Any?) =
        linkToSingleResource(T::class.java, id)

fun <T, R : Resource<T>> pagedResources(pageInfo: PageInfo<T>, converter: ResourceConverter<T, R>)
        : PagedResources<R> =
        PagedResources(pageInfo.list.map { converter.convert(it) },
                PagedResources.PageMetadata(pageInfo.pageSize.toLong(), pageInfo.size.toLong(),
                        pageInfo.total, pageInfo.pages.toLong()))

fun <T, R : Resource<T>> pagedResourcesBatch(pageInfo: PageInfo<T>, converter: ResourcesConverter<T, R>)
        : PagedResources<R> =
        PagedResources(converter.convert(pageInfo.list),
                PagedResources.PageMetadata(pageInfo.pageSize.toLong(), pageInfo.size.toLong(),
                        pageInfo.total, pageInfo.pages.toLong()))

fun ControllerLinkBuilder.query(name: String, vararg param: Any) =
        Link(this.toUriComponentsBuilder().queryParam(name, param).build().toUriString())
