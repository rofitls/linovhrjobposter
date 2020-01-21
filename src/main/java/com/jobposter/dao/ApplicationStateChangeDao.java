package com.jobposter.dao;

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
	public ApplicationStateChange findByBk(String Bk1) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.id =: bk1")
				.setParameter("bk1", Bk1)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(list.size()-1);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findByApplicationNotViewed(String id) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.id =: id and state.stateName =: state")
				.setParameter("id", id)
				.setParameter("state", "Not Viewed")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long findApplicationHire(String id) {
		StringBuilder query = new StringBuilder();
		query.append("select count(ap) from ApplicationStateChange ap where ap.application.jobPosting.id =: id and state.stateName =: state");
		List<Long> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.setParameter("state", "Hire")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Long)list.get(0);
	}
}
