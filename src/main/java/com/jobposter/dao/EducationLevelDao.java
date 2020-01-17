package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.EducationLevel;

@Repository
public class EducationLevelDao extends CommonDao {
	
	@Transactional
	public void save (EducationLevel el) {
		super.entityManager.merge(el);
	}
	
	@Transactional
	public void delete(String id) {
		EducationLevel el = findById(id);
		super.entityManager.remove(el);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public EducationLevel findById(String id) {
		List<EducationLevel> list = super.entityManager
				.createQuery("from EducationLevel where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (EducationLevel)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<EducationLevel> findAll(){
		List<EducationLevel> list = super.entityManager
				.createQuery("from EducationLevel where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<EducationLevel>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public EducationLevel findByBk(String Bk) {
		List<EducationLevel> list = super.entityManager
				.createQuery("from EducationLevel where educationLevelCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (EducationLevel)list.get(0);
	}

}
