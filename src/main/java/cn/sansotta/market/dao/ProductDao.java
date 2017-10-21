package cn.sansotta.market.dao;

import cn.sansotta.market.domain.Order;
import cn.sansotta.market.domain.entity.ProductEntity;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Hiki on 2017/10/20.
 */
@Component
public class ProductDao {

	private final SqlSession sqlSession;

	public ProductDao(SqlSession sqlSession){
		this.sqlSession = sqlSession;
	}

	public ProductEntity selectProductById(long id){
		return this.sqlSession.selectOne("selectProductById", id);
	}


}
