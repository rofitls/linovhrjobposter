package com.jobposter.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jobposter.dao.UserDao;
import com.jobposter.entity.Users;
import com.jobposter.exception.ErrorException;

@Service("userService")
public class UserService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PasswordEncoder bcryptEncoder;

	public Users findById(String id) {
		Users user = userDao.findById(id);
		return user;
	}
	
	public void insert(Users user) throws ErrorException{
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		userDao.save(user);
	}
	
	public void update(Users user) throws ErrorException{
		userDao.save(user);
	}
	
	public void delete(String id) throws ErrorException{
		userDao.delete(id);
	}
	
	public Users findByBk(String username, String phone)  throws ErrorException {
		Users user = userDao.findByBk(username,phone);
		return user;
	}
	
	public Users findByUsername(String username) {
		Users user = userDao.findByUsername(username);
		return user;
	}
	
	public List<Users> findAll()  throws ErrorException {
		List<Users> user = userDao.findAll();
		return user;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
//		Users user = userDao.findByUsername(email);
		Users user = findByUsername(email);
		if(user == null) {
			throw new UsernameNotFoundException("User not found with email: " + email);
		}
		return new User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}
}
