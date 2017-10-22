package cn.sansotta.market.controller.resource;

import org.springframework.hateoas.Resource;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@FunctionalInterface
public interface ResourceConverter<T, R extends Resource<T>> {
    R convert(T t);
}
