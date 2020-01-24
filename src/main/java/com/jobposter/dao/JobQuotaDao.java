package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;
import com.jobposter.entity.JobQuota;

@Repository
public class JobQuotaDao extends CommonDao {

	@Transactional
	public void save (JobQuota jq) {
		super.entityManager.merge(jq);
	}
	
	@Transactional
	public void delete(JobQuota jq) {
		super.entityManager.remove(jq);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobQuota findById(String id) {
		List<JobQuota> list = super.entityManager
				.createQuery("from JobQuota where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobQuota)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobQuota> findAll(){
		List<JobQuota> list = super.entityManager
				.createQuery("from JobQuota")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobQuota>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Integer findJobQuota(String id) {
		StringBuilder query = new StringBuilder();
		query.append("select jq.jobQuota from JobQuota jq where jq.jobPosting.id =: id");
		List<Integer> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return list.get(0);
	}
	
//	@SuppressWarnings("unchecked")
//	@Transactional
//	public JobPosting findByBk(String Bk1, String Bk2, String Bk3, Date Bk4, Date Bk5) {
//		List<JobPosting> list = super.entityManager
//				.createQuery("from JobPosting where user.id =: bk1 and jobPosition.id =: bk2 and city.id =: bk3 and startDate =: bk4 and endDate =: bk5")
//				.setParameter("bk1", Bk1)
//				.setParameter("bk2", Bk2)
//				.setParameter("bk3", Bk3)
//				.setParameter("bk4", Bk4)
//				.setParameter("bk5", Bk5)
//				.getResultList();
//		if(list.size()==0)
//			return null;
//		else
//			return (JobPosting)list.get(0);
//	}

}
