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
@SpringBootApplication
public class Main implements CommandLineRunner  {
    public static void main(String... args) {
        SpringApplication.run(Main.class);
    }

    final private OrderMapper mapper;

    public Main(OrderMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void run(String... args) throws Exception {
        OrderEntity order = new OrderEntity(2, Double.valueOf(1111), OrderState.CREATE, LocalDateTime.now(), new DeliveryInfoEntity("hiki", "110", "email", "nj"), new ArrayList<>());
        this.mapper.updateOrder(order);

//        System.out.println(this.mapper.selectShoppingItemsByOrderId(1));
//        List<ShoppingItemEntity> list = new ArrayList<>();
//        list.add(new ShoppingItemEntity(1, 222, 1, new PriceEntity(1, Double.valueOf(1)), Double.valueOf(1) ));
//        this.mapper.insertShoppingItems(list);
//        this.mapper.deleteShoppingItems(1);

    }

}
