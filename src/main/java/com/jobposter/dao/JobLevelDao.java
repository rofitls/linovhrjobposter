package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobLevel;


@Repository
public class JobLevelDao extends CommonDao {
	
	@Transactional
	public void save (JobLevel jl) {
		super.entityManager.merge(jl);
	}
	
	@Transactional
	public void delete(String id) {
		JobLevel jl = findById(id);
		super.entityManager.remove(jl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobLevel findById(String id) {
		List<JobLevel> list = super.entityManager
				.createQuery("from JobLevel where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobLevel)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobLevel> findAll(){
		List<JobLevel> list = super.entityManager
				.createQuery("from JobLevel where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobLevel>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobLevel findByBk(String Bk) {
		List<JobLevel> list = super.entityManager
				.createQuery("from JobLevel where jobLevelCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobLevel)list.get(0);
	}

}
