package cn.sansotta.market.configuration

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@MapperScan("cn.sansotta.market.mapper")
class MybatisConfiguration