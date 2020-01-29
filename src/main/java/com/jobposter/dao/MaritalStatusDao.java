package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.MaritalStatus;

@Repository
public class MaritalStatusDao extends CommonDao {
	
	
	@Transactional
	public void save (MaritalStatus ms) {
		super.entityManager.merge(ms);
	}
	
	@Transactional
	public void delete(MaritalStatus ms) {
		super.entityManager.remove(ms);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public MaritalStatus findById(String id) {
		List<MaritalStatus> list = super.entityManager
				.createQuery("from MaritalStatus where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (MaritalStatus)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<MaritalStatus> findAll(){
		List<MaritalStatus> list = super.entityManager
				.createQuery("from MaritalStatus where activeState=:active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<MaritalStatus>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public MaritalStatus findByBk(String BK) {
		List<MaritalStatus> list = super.entityManager
				.createQuery("from Province where maritalStatusCode=:bk")
				.setParameter("bk", BK)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (MaritalStatus)list.get(0);
	}

}
