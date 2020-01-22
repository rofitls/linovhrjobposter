package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobRequirement;

@Repository
public class JobRequirementDao extends CommonDao {

	@Transactional
	public void save (JobRequirement jreq) {
		super.entityManager.merge(jreq);
	}
	
	@Transactional
	public void delete(String id) {
		JobRequirement jreq = findById(id);
		super.entityManager.remove(jreq);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobRequirement findById(String id) {
		List<JobRequirement> list = super.entityManager
				.createQuery("from JobRequirement where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobRequirement)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobRequirement> findAll(){
		List<JobRequirement> list = super.entityManager
				.createQuery("from JobRequirement")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobRequirement>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobRequirement findByBk(String Bk) {
		List<JobRequirement> list = super.entityManager
				.createQuery("from JobRequirement where jobRequirementCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobRequirement)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobRequirement> findRequirementByJobPosting(String id){
		List<JobRequirement> list = super.entityManager
				.createQuery("from JobRequirement where jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobRequirement>)list;
	}
}
