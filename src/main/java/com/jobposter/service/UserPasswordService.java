package com.jobposter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobposter.dao.UserPasswordDao;
import com.jobposter.entity.UserPassword;
import com.jobposter.exception.ErrorException;

@Service("userPasswordService")
public class UserPasswordService {

	@Autowired
	private UserPasswordDao userPasswordDao;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;
	
	public UserPassword findById(String id) {
		UserPassword user = userPasswordDao.findById(id);
		return user;
	}
	
	public void insert(UserPassword user) throws ErrorException{
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userPasswordDao.save(user);
	}
	
	public void update(UserPassword user) throws ErrorException{
		userPasswordDao.save(user);
	}
	
	public void delete(UserPassword user) throws ErrorException{
		userPasswordDao.delete(user);
	}
}
