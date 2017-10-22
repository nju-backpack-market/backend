package cn.sansotta.market.controller.resource;

import org.springframework.hateoas.Resource;

import java.util.List;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@FunctionalInterface
public interface ResourcesConverter<T, R extends Resource<T>> {
    List<R> convert(List<T> list);
}
