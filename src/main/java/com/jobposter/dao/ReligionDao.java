package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Religion;

@Repository
public class ReligionDao extends CommonDao {

	@Transactional
	public void save (Religion religion) {
		super.entityManager.merge(religion);
	}
	
	@Transactional
	public void delete(Religion religion) {
		super.entityManager.remove(religion);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Religion findById(String id) {
		List<Religion> list = super.entityManager
				.createQuery("from Religion where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Religion)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Religion> findAll(){
		List<Religion> list = super.entityManager
				.createQuery("from Religion where activeState=:active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Religion>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Religion findByBk(String BK) {
		List<Religion> list = super.entityManager
				.createQuery("from Religion where religionCode=:bk")
				.setParameter("bk", BK)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Religion)list.get(0);
	}
}
