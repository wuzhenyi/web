package cn.wzy.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;

import cn.wzy.search.dao.MongoDAO;
import cn.wzy.search.dao.ProductDAO;
import cn.wzy.search.pojo.Product;

public class ProductTest {
	
	public static void find(MongoDAO md, String name){
		
		Product prod = (Product) md.findOne(Criteria.where("ProductName").regex(name), Product.class);
		if (prod != null) {
			System.out.println(prod.getProductName());
		}
	}
	
	public static String makeJsonString(List<Product> result) throws ParseException {
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.registerJsonValueProcessor(Date.class, new JsonValueProcessor() {
			
			private String format = "yyyy-MM-dd";
			
			@Override
			public Object processObjectValue(String key, Object value,
					JsonConfig jsonConfig) {
				return process(value);
			}
			
			@Override
			public Object processArrayValue(Object value, JsonConfig jsonConfig) {
				return process(value);
			}
			
			private Object process(Object value){  
		        if(value instanceof Date){    
		            SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);    
		            return sdf.format(value);  
		        }    
		        return value == null ? "" : value.toString();    
		    }
		});
		JSONArray jsonProds = new JSONArray();
		for(Product prod : result){
			JSONObject json = JSONObject.fromObject(prod, jsonConfig);
			jsonProds.add(json);
		}
		return jsonProds.toString();
	}
	
	public static JSONArray stringToJson(JSONArray jsons) throws ParseException {
		Iterator jsonProds = jsons.listIterator();
		while(jsonProds.hasNext()){
			JSONObject json = (JSONObject) jsonProds.next();
			String lastModified = (String) json.get("lastModified");
			Date date = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).parse(lastModified);
			System.out.println("lastModified--->"+date);
		}
		return null;
	}
	
	public static void main(String[] args) throws ParseException {
		ApplicationContext ac = new ClassPathXmlApplicationContext("springmvc_servlet.xml");
//		ProductDAO pd = (ProductDAO) ac.getBean("productDAOImpl");
//		MongoDAO md = (MongoDAO) ac.getBean("mongoDAOImpl");
//		System.out.println(pd.getClass());
//		Product prod = pd.getProduct(1);
//		
//		System.out.println(makeJsonString(Arrays.asList(prod)));
		
//		md.save(prod);
		
//		find(md, "联想");
		
//		ac.getBean("startQuarzt");
		
	}
}
