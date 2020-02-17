package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobDescription;

@Repository
public class JobDescriptionDao extends CommonDao {

	@Transactional
	public void save (JobDescription jdesc) {
		super.entityManager.merge(jdesc);
	}
	
	@Transactional
	public void delete(JobDescription jdesc) {
		super.entityManager.remove(jdesc);
	}
	
	@Transactional
	public void deleteByJob(String id) {
		super.entityManager
				.createQuery("delete from JobDescription where jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDescription findById(String id) {
		List<JobDescription> list = super.entityManager
				.createQuery("from JobDescription where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobDescription)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobDescription> findAll(){
		List<JobDescription> list = super.entityManager
				.createQuery("from JobDescription")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobDescription>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobDescription findByBk(String Bk) {
		List<JobDescription> list = super.entityManager
				.createQuery("from JobDescription where jobDescriptionCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobDescription)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobDescription> findDescriptionByJobPosting(String id){
		List<JobDescription> list = super.entityManager
				.createQuery("from JobDescription where jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobDescription>)list;
	}
	
}
