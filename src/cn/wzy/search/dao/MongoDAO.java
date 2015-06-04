package cn.wzy.search.dao;

import org.springframework.data.mongodb.core.query.Criteria;

public interface MongoDAO<T> {
	
	public T findOne(Criteria criteria, Class<T> clazz);
	public void save(T entity);
}
