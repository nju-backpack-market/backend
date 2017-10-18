@file:JvmName("Utils")

package cn.sansotta.market.common

import com.fasterxml.jackson.databind.ObjectMapper

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */

fun ObjectMapper.getCollectionType(collectionType: Class<out Collection<*>>, elementType: Class<*>)
        = typeFactory.constructCollectionType(collectionType, elementType)

inline fun <reified C : Collection<*>, reified E>
        ObjectMapper.getCollectionType() = getCollectionType(C::class.java, E::class.java)
