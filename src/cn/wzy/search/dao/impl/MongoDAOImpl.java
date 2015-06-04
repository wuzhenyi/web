package cn.wzy.search.dao.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import cn.wzy.search.dao.MongoDAO;

@Repository
public class MongoDAOImpl<T> implements MongoDAO<T> {
	
	@Resource private MongoTemplate mongoTemplate;
//	private MongoTemplate mongoTemplate;

	@Override
	public T findOne(Criteria criteria, Class<T> clazz) {
		return mongoTemplate.findOne(new Query(criteria), clazz, clazz.getSimpleName());
	}

	@Override
	public void save(T entity) {
		mongoTemplate.save(entity, entity.getClass().getSimpleName());
	}

}
