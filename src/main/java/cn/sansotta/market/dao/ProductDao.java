package cn.sansotta.market.dao;

import cn.sansotta.market.domain.Order;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.mapper.ProductMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hiki on 2017/10/20.
 */
@Component
public interface ProductDao{

	public ProductEntity selectProductById(long id);

}
