package com.jobposter.dao;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicationStateChange;

@Repository
public class ApplicationStateChangeDao extends CommonDao {

	@Transactional
	public void save (ApplicationStateChange state) {
		super.entityManager.merge(state);
	}
	
	@Transactional
	public void delete(String id) {
		ApplicationStateChange state = findById(id);
		super.entityManager.remove(state);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findById(String id) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicationStateChange> findAll(){
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationStateChange>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findByBk(String Bk1, String Bk2, Date Bk3) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.id =: bk1 and state.id =: bk2 and dateChanged =: bk3")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(0);
	}
}
