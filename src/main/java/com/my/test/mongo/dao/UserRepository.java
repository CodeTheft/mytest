package com.my.test.mongo.dao;

import java.util.List;

import com.my.test.mongo.model.User;

public interface UserRepository {
	
	public List<User> getAll();

	public List<User> getByName(String name);
	
}
