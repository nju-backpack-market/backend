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
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

const val JSON_MIME_TYPE = "application/json"
const val HAL_MIME_TYPE = "application/hal+json"

fun <T> notFoundResponse() = ResponseEntity<T>(HttpStatus.NOT_FOUND)

fun <T> insufficientStorageResponse() = ResponseEntity<T>(HttpStatus.INSUFFICIENT_STORAGE)

fun <T> noContentResponse() = ResponseEntity<T>(HttpStatus.NO_CONTENT)

fun <T> conflictResponse() = ResponseEntity<T>(HttpStatus.CONFLICT)

fun <T> badRequestResponse() = ResponseEntity<T>(HttpStatus.BAD_REQUEST)

@JvmOverloads
fun <T> T?.toResponse(status: HttpStatus = HttpStatus.OK) = ResponseEntity(this, status)

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

inline fun <reified T> api(name: String, endpoint: T.() -> Any)
        = linkTo(methodOn(T::class.java).endpoint()).withRel(name)

