package cn.sansotta.market.common

import com.github.pagehelper.PageHelper
import com.github.pagehelper.PageInfo
import org.mybatis.spring.SqlSessionTemplate
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * Helper class for mybatis.
 *
 * It contains the [SqlSessionTemplate] singleton, provide convenient method to work with it.
 *
 * **NOTICE**: Don't call commit, rollback or close explicitly, since they are completed automatically.
 * Manually call causes exception.
 *
 * More shortcut method will be added later.
 *
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class MybatisUtils(val template: SqlSessionTemplate) {
    /**
     * Execute with a mapper in functional way.
     * */
    fun <T, R> withMapper(clazz: Class<T>, func: Function<T, R>) = func.apply(template.getMapper(clazz))

    /**
     * Wrap result with [PageInfo]. You should call [PageHelper.startPage] firstly.
     * */
    fun <T, E> withMapperPaged(clazz: Class<T>, func: Function<T, List<E>>)
            = PageInfo(func.apply(template.getMapper(clazz)))

    /**
     * Wrap result with [PageInfo], [PageHelper.startPage] has been called with [pageNum] and [pageSize].
     * */
    fun <T, E> withMapperPaged(clazz: Class<T>,
                               pageNum: Int, pageSize: Int,
                               func: Function<T, List<E>>): PageInfo<E> {
        PageHelper.startPage<E>(pageNum, pageSize)
        return PageInfo(func.apply(template.getMapper(clazz)))
    }

    /**
     * If a DAO only works with a single mapper class, use this method to retrieve a dedicated template
     * then free from passing [clazz] every time to use the mapper.
     *
     * In other words, return value is currying version of methods in this class.
     * */
    fun <T> mapperTemplate(clazz: Class<T>) = MapperTemplate(clazz, template)

    class MapperTemplate<T>(private val clazz: Class<T>, private val template: SqlSessionTemplate) {
        /**
         * Execute with mapper.
         * */
        fun <R> exec(func: Function<T, R>) = func.apply(template.getMapper(clazz))

        fun <P, R> exec(param: P, func: T.(P) -> R) = template.getMapper(clazz).func(param)

        fun <P1, P2, R> exec(p1: P1, p2: P2, func: T.(P1, P2) -> R)
                = template.getMapper(clazz).func(p1, p2)

        fun <P1, P2, P3, R> exec(p1: P1, p2: P2, p3: P3, func: T.(P1, P2, P3) -> R)
                = template.getMapper(clazz).func(p1, p2, p3)

        fun <P1, P2, P3, P4, R> exec(p1: P1, p2: P2, p3: P3, p4: P4, func: T.(P1, P2, P3, P4) -> R)
                = template.getMapper(clazz).func(p1, p2, p3, p4)

        /**
         * Execute with mapper and wrap result into [PageInfo]
         * */
        fun <E> paged(func: Function<T, List<E>>) = PageInfo(func.apply(template.getMapper(clazz)))

        fun <P, E> paged(param: P, func: T.(P) -> List<E>)
                = PageInfo(template.getMapper(clazz).func(param))

        fun <P1, P2, E> paged(p1: P1, p2: P2, func: T.(P1, P2) -> List<E>)
                = PageInfo(template.getMapper(clazz).func(p1, p2))

        fun <P1, P2, P3, E> paged(p1: P1, p2: P2, p3: P3, func: T.(P1, P2, P3) -> List<E>)
                = PageInfo(template.getMapper(clazz).func(p1, p2, p3))

        fun <P1, P2, P3, P4, E> paged(p1: P1, p2: P2, p3: P3, p4: P4, func: T.(P1, P2, P3, P4) -> List<E>)
                = PageInfo(template.getMapper(clazz).func(p1, p2, p3, p4))

        /**
         * Execute with mapper, start paging automatically and wrap result into [PageInfo].
         * */
        fun <E> paged(pageNum: Int, pageSize: Int, func: Function<T, List<E>>): PageInfo<E> {
            PageHelper.startPage<E>(pageNum, pageSize)
            return PageInfo(func.apply(template.getMapper(clazz)))
        }

        fun <P, E> paged(pageNum: Int, pageSize: Int, param: P, func: T.(P) -> List<E>): PageInfo<E> {
            PageHelper.startPage<E>(pageNum, pageSize)
            return PageInfo(template.getMapper(clazz).func(param))
        }

        fun <P1, P2, E> paged(pageNum: Int, pageSize: Int, p1: P1, p2: P2, func: T.(P1, P2) -> List<E>): PageInfo<E> {
            PageHelper.startPage<E>(pageNum, pageSize)
            return PageInfo(template.getMapper(clazz).func(p1, p2))
        }

        fun <P1, P2, P3, E> paged(pageNum: Int, pageSize: Int, p1: P1, p2: P2, p3: P3, func: T.(P1, P2, P3) -> List<E>): PageInfo<E> {
            PageHelper.startPage<E>(pageNum, pageSize)
            return PageInfo(template.getMapper(clazz).func(p1, p2, p3))
        }

        fun <P1, P2, P3, P4, E> paged(pageNum: Int, pageSize: Int, p1: P1, p2: P2, p3: P3, p4: P4, func: T.(P1, P2, P3, P4) -> List<E>): PageInfo<E> {
            PageHelper.startPage<E>(pageNum, pageSize)
            return PageInfo(template.getMapper(clazz).func(p1, p2, p3, p4))
        }

    }
}
