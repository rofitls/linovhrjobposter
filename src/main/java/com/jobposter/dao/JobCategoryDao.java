package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobCategory;

@Repository
public class JobCategoryDao extends CommonDao {

	@Transactional
	public void save (JobCategory jc) {
		super.entityManager.merge(jc);
	}
	
	@Transactional
	public void delete(JobCategory jc) {
		super.entityManager.remove(jc);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobCategory findById(String id) {
		List<JobCategory> list = super.entityManager
				.createQuery("from JobCategory where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobCategory)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobCategory> findAll(){
		List<JobCategory> list = super.entityManager
				.createQuery("from JobCategory where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobCategory>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobCategory findByBk(String Bk) {
		List<JobCategory> list = super.entityManager
				.createQuery("from JobCategory where jobCategoryCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobCategory)list.get(0);
	}
}
