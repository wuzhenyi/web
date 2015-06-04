package cn.wzy.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;

import cn.wzy.search.dao.MongoDAO;
import cn.wzy.search.pojo.User;
import cn.wzy.search.util.StringUtil;

public class MongoDBTest {
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("springmvc_servlet.xml");
		MongoDAO md = (MongoDAO) ac.getBean("mongoDAOImpl");
		User user = (User) md.findOne(Criteria.where("name").is("wzy"), User.class);
		if (!StringUtil.isNullOrBlank(user))
			System.out.println(user.getName());
	}
}
