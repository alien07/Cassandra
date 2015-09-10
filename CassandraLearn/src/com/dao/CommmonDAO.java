package com.dao;

import java.util.List;

public interface CommmonDAO<T> {
	void save(T t) throws Exception;

	List<T> select(String query) throws Exception;
}
