package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.UserPassword;

@Repository
public class UserPasswordDao extends CommonDao {

	@Transactional
	public void save (UserPassword user) {
		super.entityManager.merge(user);
	}
	
	@Transactional
	public void delete(UserPassword user) {
		super.entityManager.remove(user);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public UserPassword findById(String id) {
		List<UserPassword> list = super.entityManager
				.createQuery("from UserPassword where user.id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (UserPassword)list.get(0);
	}
	
}
