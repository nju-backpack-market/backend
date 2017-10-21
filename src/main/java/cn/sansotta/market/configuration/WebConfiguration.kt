package cn.sansotta.market.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.hateoas.config.EnableEntityLinks
import org.springframework.hateoas.config.EnableHypermediaSupport

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@Configuration
@EnableHypermediaSupport(type = arrayOf(EnableHypermediaSupport.HypermediaType.HAL))
class WebConfiguration