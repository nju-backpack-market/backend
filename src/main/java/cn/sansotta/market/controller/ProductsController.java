package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.sansotta.market.controller.resource.ProductAssembler;
import cn.sansotta.market.controller.resource.ProductResource;
import cn.sansotta.market.domain.value.Product;
import cn.sansotta.market.service.ProductService;
import static cn.sansotta.market.common.HateoasUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.HateoasUtils.notFoundEntity;
import static cn.sansotta.market.common.HateoasUtils.pagedResourcesBatch;
import static cn.sansotta.market.common.HateoasUtils.toResponse;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@RestController
@ExposesResourceFor(Product.class)
@RequestMapping("/products")
public class ProductsController {
    private final ProductService productService;
    private final EntityLinks link;
    private final ProductAssembler assembler = new ProductAssembler();

    public ProductsController(ProductService productService, EntityLinks link) {
        this.productService = productService;
        this.link = link;
    }

    @GetMapping(value = "/{id}", produces = HAL_MIME_TYPE)
    public ResponseEntity<ProductResource> product(@PathVariable("id") long id) {

        Product product = productService.product(id);
        return product == null ? notFoundEntity() : toResponse(assembleResource(product));
    }

    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<ProductResource>>
    allProducts(@RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        PageInfo<Product> pageInfo = productService.allProducts(page);
        return pageInfo == null ? notFoundEntity() : toResponse(assembleResources(pageInfo));
    }

    private ProductResource assembleResource(Product product) {
        ProductResource resource = assembler.toResource(product);
        resource.add(link.linkToCollectionResource(Product.class).withRel("collection"));
        return resource;
    }

    private PagedResources<ProductResource> assembleResources(PageInfo<Product> info) {
        PagedResources<ProductResource> resources = pagedResourcesBatch(info, assembler::toResources);
        resources.add(
                linkTo(methodOn(getClass()).allProducts(info.getPageNum())).withSelfRel(),
                linkTo(methodOn(getClass()).allProducts(info.getNavigateFirstPage())).withRel("first"),
                linkTo(methodOn(getClass()).allProducts(info.getNavigateLastPage())).withRel("last")
        );
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).allProducts(info.getPrePage())).withRel("prev"));
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).allProducts(info.getNextPage())).withRel("next"));
        return resources;
    }
}
