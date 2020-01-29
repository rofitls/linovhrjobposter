package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Major;

@Repository
public class MajorDao extends CommonDao {
	

	@Transactional
	public void save (Major major) {
		super.entityManager.merge(major);
	}
	
	@Transactional
	public void delete(Major major) {
		super.entityManager.remove(major);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Major findById(String id) {
		List<Major> list = super.entityManager
				.createQuery("from Major where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Major)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Major> findAll(){
		List<Major> list = super.entityManager
				.createQuery("from City where activeState=:active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Major>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Major> findByEduLevel(String id){
		List<Major> list = super.entityManager
				.createQuery("from Major where eduLevel.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Major>)list;
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	public Major findByBk(String Bk) {
		List<Major> list = super.entityManager
				.createQuery("from City where cityCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Major)list.get(0);
	}

}
