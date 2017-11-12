@file:JvmName("WebUtils")

package cn.sansotta.market.common

import cn.sansotta.market.controller.resource.ResourceConverter
import cn.sansotta.market.controller.resource.ResourcesConverter
import com.github.pagehelper.PageInfo
import org.springframework.core.env.Environment
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.Link
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.Resource
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.CollectionUtils
import java.net.InetAddress
import java.net.NetworkInterface

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

const val JSON_MIME_TYPE = "application/json"
const val HAL_MIME_TYPE = "application/hal+json"

fun <T> okResponse() = ResponseEntity<T>(HttpStatus.OK)

fun <T> notFoundResponse() = ResponseEntity<T>(HttpStatus.NOT_FOUND)

fun <T> insufficientStorageResponse() = ResponseEntity<T>(HttpStatus.INSUFFICIENT_STORAGE)

fun <T> noContentResponse() = ResponseEntity<T>(HttpStatus.NO_CONTENT)

fun <T> createdResponse() = ResponseEntity<T>(HttpStatus.CREATED)

fun <T> conflictResponse() = ResponseEntity<T>(HttpStatus.CONFLICT)

fun <T> badRequestResponse() = ResponseEntity<T>(HttpStatus.BAD_REQUEST)

fun <T> unauthorizedResponse() = ResponseEntity<T>(HttpStatus.UNAUTHORIZED)

fun <K, V> singletonHeader(key: K, value: V)
        = CollectionUtils.toMultiValueMap(mapOf(key to listOf(value)))

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

fun contextBaseUrl(env: Environment): String {
    var address = getIpAddress()
    val port = env.getProperty("server.port")
    var root = env.getProperty("server.context-path")

    if (root == null) root = "/"
    if (address.startsWith("192.168")) address = "localhost"
    return "https://$address:$port$root"
}

fun getIpAddress(): String {
    // Before we connect somewhere, we cannot be sure about what we'd be bound to; however,
    // we only connect when the message where client ID is, is long constructed. Thus,
    // just use whichever IP address we can find.
    val interfaces = NetworkInterface.getNetworkInterfaces()
    while (interfaces.hasMoreElements()) {
        val current = interfaces.nextElement()
        if (!current.isUp || current.isLoopback || current.isVirtual) continue
        val addresses = current.inetAddresses
        while (addresses.hasMoreElements()) {
            val addr = addresses.nextElement().takeUnless(InetAddress::isLoopbackAddress) ?: continue
            return addr.toString()
        }
    }
    return "192.168.1.1"
}

