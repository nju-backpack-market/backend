package cn.sansotta.market.controller.resource;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
public class StaticResource  {
    String path;
    String type;

    public StaticResource() {
    }

    public StaticResource(String path, String type) {
        this.path = path;
        this.type = type;
    }
}
