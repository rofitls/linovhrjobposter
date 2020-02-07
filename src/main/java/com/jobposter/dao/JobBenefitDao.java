package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobBenefit;

@Repository
public class JobBenefitDao extends CommonDao {

	@Transactional
	public void save (JobBenefit jBenefit) {
		super.entityManager.merge(jBenefit);
	}
	
	@Transactional
	public void delete(JobBenefit jBenefit) {
		super.entityManager.remove(jBenefit);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public void deleteByJob(String id) {
		List<JobBenefit> list = super.entityManager
				.createQuery("delete from JobBenefit where jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobBenefit findById(String id) {
		List<JobBenefit> list = super.entityManager
				.createQuery("from JobBenefit where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobBenefit)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobBenefit> findAll(){
		List<JobBenefit> list = super.entityManager
				.createQuery("from JobBenefit")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobBenefit>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobBenefit findByBk(String Bk) {
		List<JobBenefit> list = super.entityManager
				.createQuery("from JobBenefit where jobBenefitCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobBenefit)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobBenefit> findBenefitByJobPosting(String id){
		List<JobBenefit> list = super.entityManager
				.createQuery("from JobBenefit where jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobBenefit>)list;
	}
}
