package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Users;

@Repository
public class UserDao extends CommonDao {

	@Transactional
	public void save (Users user) {
		super.entityManager.merge(user);
	}
	
	@Transactional
	public void delete(Users user) {
		super.entityManager.remove(user);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Users findById(String id) {
		List<Users> list = super.entityManager
				.createQuery("from Users where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Users)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Users> findAll(){
		List<Users> list = super.entityManager
				.createQuery("from Users where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Users>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Users findByBk(String username, String phone) {
		List<Users> list = super.entityManager
				.createQuery("from Users where username=:username and phone=:phone")
				.setParameter("username", username)
				.setParameter("phone", phone)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Users)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Users findByUsername(String username) {
		List<Users> list = super.entityManager
				.createQuery("from Users where username=:username")
				.setParameter("username", username)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Users)list.get(0);
	}
}
