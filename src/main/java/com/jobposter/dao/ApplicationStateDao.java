package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicationState;

@Repository
public class ApplicationStateDao extends CommonDao {

	@Transactional
	public void save (ApplicationState state) {
		super.entityManager.merge(state);
	}
	
	@Transactional
	public void delete(String id) {
		ApplicationState state = findById(id);
		super.entityManager.remove(state);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationState findById(String id) {
		List<ApplicationState> list = super.entityManager
				.createQuery("from ApplicationState where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationState)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicationState> findAll(){
		List<ApplicationState> list = super.entityManager
				.createQuery("from ApplicationState where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationState>)list;
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationState findByBk(String Bk) {
		List<ApplicationState> list = super.entityManager
				.createQuery("from ApplicationState where stateCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationState)list.get(0);
	}
}
