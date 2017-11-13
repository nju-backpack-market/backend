package cn.sansotta.market.controller;

import com.github.pagehelper.PageInfo;

import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import cn.sansotta.market.controller.resource.ProductAssembler;
import cn.sansotta.market.controller.resource.ProductResource;
import cn.sansotta.market.domain.value.Product;
import cn.sansotta.market.service.Authorized;
import cn.sansotta.market.service.ProductService;

import static cn.sansotta.market.common.WebUtils.HAL_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.JSON_MIME_TYPE;
import static cn.sansotta.market.common.WebUtils.notFoundResponse;
import static cn.sansotta.market.common.WebUtils.pagedResourcesBatch;
import static cn.sansotta.market.common.WebUtils.toResponse;
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
    public ResponseEntity<ProductResource>
    product(@PathVariable("id") long id) {
        Product product = productService.product(id, true);
        return product == null ? notFoundResponse() : toResponse(assembleResource(product));
    }

    @Authorized(intercept = false)
    @GetMapping(produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<ProductResource>>
    allProducts(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                @RequestParam(value = "all", required = false, defaultValue = "false") boolean all,
                @RequestAttribute("authorized") boolean authorized) {
        PageInfo<Product> pageInfo = productService.allProducts(page, all && authorized);
        return pageInfo == null ? notFoundResponse() : toResponse(assembleResources(pageInfo));
    }

    @Authorized(intercept = false)
    @GetMapping(value = "/name/{name}", produces = HAL_MIME_TYPE)
    public ResponseEntity<PagedResources<ProductResource>>
    products(@PathVariable(value = "name") String name,
             @RequestParam(value = "page", required = false, defaultValue = "0") int page,
             @RequestParam(value = "all") boolean all,
             @RequestAttribute(value = "authorized") boolean authorized) {
        PageInfo<Product> pageInfo = productService.products(name, page, all && authorized);
        return pageInfo == null ? notFoundResponse() : toResponse(assembleResources(pageInfo));
    }

    @Authorized
    @PostMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<List<ProductResource>>
    newProducts(@RequestBody List<Product> products) {
        return toResponse(assembleResources(productService.newProducts(products)), HttpStatus.CREATED);
    }

    @Authorized
    @PutMapping(consumes = JSON_MIME_TYPE, produces = HAL_MIME_TYPE)
    public ResponseEntity<List<ProductResource>>
    modifyProducts(@RequestBody List<Product> products) {
        return toResponse(assembleResources(productService.modifyProducts(products)));
    }

    @Authorized
    @DeleteMapping("/{id}")
    public ResponseEntity<List<Long>>
    removeProducts(@PathVariable("id") List<Long> ids) {
        return toResponse(productService.pullOffProducts(ids));
    }

    private ProductResource assembleResource(Product product) {
        ProductResource resource = assembler.toResource(product);
        resource.add(link.linkToCollectionResource(Product.class).withRel("collection"));
        return resource;
    }

    private List<ProductResource> assembleResources(List<Product> products) {
        return assembler.toResources(products);
    }

    private PagedResources<ProductResource> assembleResources(PageInfo<Product> info) {
        PagedResources<ProductResource> resources = pagedResourcesBatch(info, assembler::toResources);
        resources.add(
                linkTo(methodOn(getClass()).allProducts(info.getPageNum(), false, true)).withSelfRel(),
                linkTo(methodOn(getClass()).allProducts(info.getNavigateFirstPage(), false, true))
                        .withRel("first"),
                linkTo(methodOn(getClass()).allProducts(info.getNavigateLastPage(), false, true))
                        .withRel("last")
        );
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).allProducts(info.getPrePage(), false, true))
                    .withRel("prev"));
        if(info.isHasNextPage())
            resources.add(linkTo(methodOn(getClass()).allProducts(info.getNextPage(), false, true))
                    .withRel("next"));
        return resources;
    }
}
