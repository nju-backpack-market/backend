package cn.sansotta.market;

import cn.sansotta.market.domain.entity.*;
import cn.sansotta.market.domain.value.OrderState;
import cn.sansotta.market.domain.value.ShoppingItem;
import cn.sansotta.market.mapper.ProductMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cn.sansotta.market.common.MybatisUtils;
import cn.sansotta.market.mapper.OrderMapper;
import cn.sansotta.market.mapper.ShoppingItemMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
/**
 * @author <a href="mailto:tinker19981@hotmail.com">tinker</a>
 */
@SpringBootApplication
public class Main {
	public static void main(String... args) {
		SpringApplication.run(Main.class);
	}
}
