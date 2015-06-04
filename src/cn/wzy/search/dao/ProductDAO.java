package cn.wzy.search.dao;

import java.util.List;

import cn.wzy.search.pojo.Product;

public interface ProductDAO {
	Product getProduct(int productCode);
	List<Product> getAllProducts();
}
