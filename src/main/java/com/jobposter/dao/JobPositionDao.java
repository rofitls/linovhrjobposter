package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobPosition;

@Repository
public class JobPositionDao extends CommonDao {
	
	@Transactional
	public void save (JobPosition jp) {
		super.entityManager.merge(jp);
	}
	
	@Transactional
	public void delete(JobPosition jp) {
		super.entityManager.remove(jp);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobPosition findById(String id) {
		List<JobPosition> list = super.entityManager
				.createQuery("from JobPosition where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobPosition)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobPosition> findAll(){
		List<JobPosition> list = super.entityManager
				.createQuery("from JobPosition where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobPosition>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobPosition findByBk(String Bk) {
		List<JobPosition> list = super.entityManager
				.createQuery("from JobPosition where jobPositionCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobPosition)list.get(0);
	}

}
