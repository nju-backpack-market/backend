package cn.sansotta.market.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
public class ObjectMapperConfiguration {
    @Bean
    public static ObjectMapper objectMapper() {
       ObjectMapper mapper=new ObjectMapper();
       mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
       return mapper;
    }
}
