package cn.sansotta.market.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
class ObjectMapperConfiguration {
    @Bean
    fun objectMapper() = ObjectMapper().
            apply { propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE }
}