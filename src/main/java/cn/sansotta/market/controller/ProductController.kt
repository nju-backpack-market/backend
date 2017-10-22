package cn.sansotta.market.controller

import cn.sansotta.market.common.pagedResources
import cn.sansotta.market.common.toResponse
import cn.sansotta.market.controller.resource.ProductResource
import cn.sansotta.market.domain.value.Product
import cn.sansotta.market.service.ProductService
import com.github.pagehelper.PageInfo
import org.springframework.hateoas.EntityLinks
import org.springframework.hateoas.ExposesResourceFor
import org.springframework.hateoas.PagedResources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Product::class)
@RequestMapping("/product")
class ProductController(val productService: ProductService, val link: EntityLinks) {
    @GetMapping("/{id}", produces = arrayOf("application/hal+json"))
    fun product(@PathVariable("id") id: Long)
            = productService.product(id)?.let { ProductResource(it).addLinks(id).toResponse() }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)


    @GetMapping(produces = arrayOf("application/hal+json"))
    fun allProducts(@RequestParam("page", required = false, defaultValue = "0") page: Int)
            = productService.allProducts(page)?.let { pagedResources(it).addLinks(it).toResponse() }
            ?: ResponseEntity(HttpStatus.NOT_FOUND)

    private fun ProductResource.addLinks(id: Long): ProductResource {
        add(link.linkToSingleResource(Product::class.java, id).withSelfRel(),
                link.linkToCollectionResource(Product::class.java).withRel("collection"))
        return this
    }

    private fun PagedResources<Product>.addLinks(info: PageInfo<Product>): PagedResources<Product> {
        val method = ProductController::class.java.getMethod("allProducts", Int::class.java)
        add(
                linkTo(method, info.pageNum).withSelfRel(),
                linkTo(method, info.navigateLastPage).withRel("last"),
                linkTo(method, info.navigateFirstPage).withRel("first")
        )
        if (info.isHasPreviousPage) add(linkTo(method, info.prePage).withRel("prev"))
        if (info.isHasNextPage) add(linkTo(method, info.nextPage).withRel("next"))
        return this
    }
}