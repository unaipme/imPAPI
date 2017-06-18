package com.unai.impapi.repo;

public interface PapiRepository<T> {
	
	public T findOne(String id);
	
}
