package cn.sansotta.market;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.mapper.OrderMapper;
import cn.sansotta.market.mapper.ShoppingItemMapper;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@SpringBootApplication
public class Main {
    public static void main(String... args) {
        SpringApplication.run(Main.class);
    }
}
