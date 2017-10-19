package cn.sansotta.market.domain.converter

import cn.sansotta.market.common.getCollectionType
import cn.sansotta.market.domain.ShoppingItem
import cn.sansotta.market.domain.Bill
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Component
class ShoppingItemConverter(@Autowired private val mapper: ObjectMapper)
    : Converter<String, Bill> {
    override fun convert(json: String?) =
            Bill(mapper.readValue<MutableList<ShoppingItem>>(
                    json, mapper.getCollectionType<ArrayList<*>, ShoppingItem>()))
}