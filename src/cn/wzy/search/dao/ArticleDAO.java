package cn.wzy.search.dao;

import java.util.List;
import java.util.Map;

import cn.wzy.search.pojo.Article;

public interface ArticleDAO {
	List<Article> list(Map<String, Object> param);
	Article get(Map<String, Object> param);
}
