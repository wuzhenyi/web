package cn.wzy.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.wzy.search.dao.impl.ArticleDAOImpl;
import cn.wzy.search.pojo.Article;
import cn.wzy.search.util.JcsegUtil;

public class JcsegTest {
	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("springmvc_servlet.xml");
		ArticleDAOImpl articleDAOImpl = (ArticleDAOImpl) ac.getBean("articleDAOImpl");
		List<Article> articles = articleDAOImpl.list(null);
		JcsegUtil.facetIndex(articles, true);
//		List<Document> docs = JcsegUtil.search(new String[]{"title", "content"},  JcsegUtil.getWds("iphone"));
		List<Document> docs = JcsegUtil.facetSearch(new String[]{"title", "content"},  JcsegUtil.getWds("iphone"));
		for (Document doc : docs) {
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("id", doc.get("id"));
			Article article = articleDAOImpl.get(param);
			System.out.println(doc.get("title"));
//			System.out.println(JcsegUtil.filterHtml(article.getContent()));
		}
	}
}
