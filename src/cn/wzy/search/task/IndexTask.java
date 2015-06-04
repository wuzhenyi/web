package cn.wzy.search.task;

import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wzy.search.dao.ProductDAO;
import cn.wzy.search.pojo.Product;

public class IndexTask {
	
	public void work(){
		System.out.println("IndexTask is woking...");
		ApplicationContext ac = new ClassPathXmlApplicationContext("springmvc_servlet.xml");
		ProductDAO pd = (ProductDAO) ac.getBean("productDAOImpl");
		List<Product> prods = pd.getAllProducts();
		System.out.println(prods);
	}
	
}
