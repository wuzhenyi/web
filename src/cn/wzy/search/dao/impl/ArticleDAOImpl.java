package cn.wzy.search.dao.impl;

import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import cn.wzy.search.dao.ArticleDAO;
import cn.wzy.search.pojo.Article;

@Repository
public class ArticleDAOImpl extends SqlSessionDaoSupport implements ArticleDAO {

	@Override
	public List<Article> list(Map<String, Object> param) {
		return this.getSqlSession().selectList("articleModel" + "." + "list", param);
	}

	@Override
	public Article get(Map<String, Object> param) {
		return this.getSqlSession().selectOne("articleModel" + "." + "get", param);
	}

}
