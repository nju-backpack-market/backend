package cn.sansotta.market.controller.resource;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import cn.sansotta.market.controller.ProductsController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
public class ApiInfoResource extends Resource<ApiInfo> {
    private static ApiInfoResource INSTANCE;

    public static ApiInfoResource getInstance() {
        if(INSTANCE == null)
            synchronized (ApiInfoResource.class) {
                if(INSTANCE == null) INSTANCE = new ApiInfoResource(API_INFO, LINKS);
            }
        return INSTANCE;
    }

    private static Link[] LINKS = {
            linkTo(methodOn(ProductsController.class).allProducts(0)).withRel("get_all_products"),
            linkTo(methodOn(ProductsController.class).product(1)).withRel("get_product")
    };

    private static ApiInfo API_INFO = new ApiInfo(
            "v0.0.1",
            "后端的API列表，附带例子和文档，点击左边的链接查看",
            "endpoint形如/{res_type}, res_type均为复数形式。直接GET返回所有资源，/{res_type}/[id]返回单个资源",
            "返回所有资源时默认分页，页大小20，用?page=[int]来指示页数，缺省则page=0",
            "GET方法没有文档，直接看例子",
            "POST成功默认状态值201，DELETE成功默认状态值204，未找到资源状态值404，无授权访问受限API状态值403");

    private ApiInfoResource(ApiInfo content, Link... links) {
        super(content, links);
    }
}

class ApiInfo {
    private String version;
    private String description;
    private String[] conventions;

    public ApiInfo(String version, String description, String... conventions) {
        this.version = version;
        this.description = description;
        this.conventions = conventions;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }

    public String[] getConventions() {
        return conventions;
    }
}
