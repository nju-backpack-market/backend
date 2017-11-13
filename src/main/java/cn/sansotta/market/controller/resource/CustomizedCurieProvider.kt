package cn.sansotta.market.controller.resource

import org.springframework.hateoas.IanaRels
import org.springframework.hateoas.UriTemplate
import org.springframework.hateoas.hal.DefaultCurieProvider
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
object CustomizedCurieProvider
    : DefaultCurieProvider(
        mapOf("get" to docTemplate,
                "post" to docTemplate,
                "put" to docTemplate,
                "delete" to docTemplate,
                "patch" to docTemplate)) {
    private val linkMethods = mutableMapOf<String, HttpMethod?>()

    fun registerMethod(rel: String, method: HttpMethod) {
        linkMethods[rel] = method
    }

    override fun getNamespacedRelFor(rel: String)
            = rel.takeIf { !IanaRels.isIanaRel(rel) && !rel.contains(":") }?.
            let { prefixOf(it) }?.let { "$it:$rel" } ?: rel

    private fun prefixOf(rel: String) = linkMethods.getOrDefault(rel, null)?.name?.toLowerCase()
}

private val docTemplate = UriTemplate("/doc/{rel}.html")