package com.my.test.mongo.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.my.test.mongo.dao.UserRepository;
import com.my.test.mongo.model.User;

@Service
public class UserRepositoryImpl implements UserRepository {

	@Autowired
	private MongoOperations mongo;
	
	public List<User> getAll() {
		return mongo.findAll(User.class);
	}

	public List<User> getByName(String name) {
		Criteria criteria = Criteria.where("name").is(name);
		Query query = Query.query(criteria);
		return mongo.find(query, User.class);
	}

}
