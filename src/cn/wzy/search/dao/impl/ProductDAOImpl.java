package cn.wzy.search.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import cn.wzy.search.dao.ProductDAO;
import cn.wzy.search.pojo.Product;

@Repository
public class ProductDAOImpl extends SqlSessionDaoSupport implements ProductDAO{

	@Override
	public Product getProduct(int productCode) {
		System.out.println("Getting Product...");
		return this.getSqlSession().selectOne("ProductModel.getProduct", productCode);
	}

	@Override
	public List<Product> getAllProducts() {
		return this.getSqlSession().selectList("ProductModel.getAllProducts");
	}

}
