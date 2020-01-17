package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Application;

@Repository
public class ApplicationDao extends CommonDao {

	@Transactional
	public void save (Application appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(String id) {
		Application appl = findById(id);
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Application findById(String id) {
		List<Application> list = super.entityManager
				.createQuery("from Application where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Application)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Application> findAll(){
		List<Application> list = super.entityManager
				.createQuery("from Application")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Application>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Application findByBk(String Bk1, String Bk2) {
		List<Application> list = super.entityManager
				.createQuery("from Application where user.id =: bk1 and jobPosting.id =: bk2")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Application)list.get(0);
	}

}
