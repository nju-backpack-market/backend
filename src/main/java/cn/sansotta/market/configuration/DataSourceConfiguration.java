package cn.sansotta.market.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@EnableTransactionManagement
public class DataSourceConfiguration {
    @Bean
    public static DataSource dataSource(DataSourceProperties properties){
        return properties.initializeDataSourceBuilder()
                .type(org.apache.tomcat.jdbc.pool.DataSource.class).build();

    }
}
