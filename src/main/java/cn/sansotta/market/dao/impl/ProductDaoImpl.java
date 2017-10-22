package cn.sansotta.market.dao.impl;

import cn.sansotta.market.dao.ProductDao;
import cn.sansotta.market.domain.entity.ProductEntity;
import cn.sansotta.market.mapper.ProductMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

/**
 * Created by Hiki on 2017/10/21.
 */
@Component
public class ProductDaoImpl implements ProductDao{

	private SqlSessionFactory sqlSessionFactory;

	public ProductDaoImpl(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	@Override
	public ProductEntity selectProductById(long id){
		ProductMapper mapper = sqlSessionFactory.openSession().getMapper(ProductMapper.class);
		return mapper.selectProductById(id);
	}
}
