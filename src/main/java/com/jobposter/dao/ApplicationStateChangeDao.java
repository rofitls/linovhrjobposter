package com.jobposter.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.JobPosting;
import com.jobposter.entity.ReportPerJobPojo;
import com.jobposter.entity.ReportPojo;
import com.jobposter.entity.Users;

@Repository
public class ApplicationStateChangeDao extends CommonDao {

	@Transactional
	public void save (ApplicationStateChange state) {
		super.entityManager.merge(state);
	}
	
	@Transactional
	public void delete(ApplicationStateChange state) {
		super.entityManager.remove(state);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findById(String id) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicationStateChange> findAll(){
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationStateChange>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findByBk(String Bk1) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.id =: bk1")
				.setParameter("bk1", Bk1)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(list.size()-1);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicationStateChange> listApplicationByUser(String id){
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.user.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationStateChange>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicationStateChange> listApplicationByJob(String id){
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.jobPosting.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationStateChange>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicationStateChange findByApplicationNotViewed(String id) {
		List<ApplicationStateChange> list = super.entityManager
				.createQuery("from ApplicationStateChange where application.id =: id and state.stateName =: state")
				.setParameter("id", id)
				.setParameter("state", "Not Viewed")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicationStateChange)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long findApplicationHire(String id) {
		StringBuilder query = new StringBuilder();
		query.append("select count(ap) from ApplicationStateChange ap where ap.application.jobPosting.id =: id and ap.state.stateName =: state");
		List<Long> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.setParameter("state", "Hire")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Long)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ReportPojo> reportMaster(String id) {	
		//Query buat total upload job per recruiter
		StringBuilder query5 = new StringBuilder();
		query5.append("select count(jp) from JobPosting jp where jp.user.id =: id");
		List<Long> list5 =  super.entityManager
				.createQuery(query5.toString())
				.setParameter("id", id)
				.getResultList();
		ReportPojo reportPojo = new ReportPojo();
		reportPojo.setTotalUploadJob(list5.get(0));
		
		List<ReportPojo> listRp = new ArrayList<ReportPojo>();
		listRp.add(reportPojo);

		if(listRp.size() == 0) 
			return null;
		else
			return (List<ReportPojo>)listRp;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<String> coba(String id) {
		
		//Query buat total hire
		StringBuilder query = new StringBuilder();
		query.append("select ap.application.jobPosting.jobTitleName from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id group by ap.application.jobPosting.jobTitleName");
		List<String> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.getResultList();
		if(list.size() == 0) 
			return null;
		else
			return (List<String>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Long> coba2(String id) {
		
		//Query buat total hire
				StringBuilder query2 = new StringBuilder();
				query2.append("select count(ap.application.jobPosting.jobTitleName) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id and ap.state.stateName =: state group by ap.application.jobPosting.jobTitleName");
				List<Long> list = super.entityManager
						.createQuery(query2.toString())
						.setParameter("id", id)
						.setParameter("state", "Hire")
						.getResultList();
				if(list.size() == 0) 
					return null;
				else
					return (List<Long>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Long> coba3(String id) {
		
		//Query buat total interview
				StringBuilder query3 = new StringBuilder();
				query3.append("select count(ap.application.jobPosting.jobTitleName) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id and ap.state.stateName =: state group by ap.application.jobPosting.jobTitleName");
				List<Long> list = super.entityManager
						.createQuery(query3.toString())
						.setParameter("id", id)
						.setParameter("state", "Interview")
						.getResultList();
				if(list.size() == 0) 
					return null;
				else
					return (List<Long>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Long> coba4(String id) {
		
		//Query buat total applicant per job
				StringBuilder query4 = new StringBuilder();
				query4.append("select count(ap.jobPosting.jobTitleName) from Application ap where ap.jobPosting.user.id =: id group by ap.jobPosting.jobTitleName");
				List<Long> list = super.entityManager
						.createQuery(query4.toString())
						.setParameter("id", id)
						.getResultList();
				if(list.size() == 0) 
					return null;
				else
					return (List<Long>)list;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ReportPerJobPojo> reportPerJob(String id) {
		
		//Query buat total hire
		StringBuilder query = new StringBuilder();
		query.append("select ap.application.jobPosting.jobTitleName from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id group by ap.application.jobPosting.jobTitleName");
		List<String> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.getResultList();
		
		//Query buat total hire
		StringBuilder query2 = new StringBuilder();
		query2.append("select count(ap.application.jobPosting.jobTitleName) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id and ap.state.stateName =: state group by ap.application.jobPosting.jobTitleName");
		List<Long> list2 = super.entityManager
				.createQuery(query2.toString())
				.setParameter("id", id)
				.setParameter("state", "Hire")
				.getResultList();
		
		//Query buat total interview
		StringBuilder query3 = new StringBuilder();
		query3.append("select count(ap.application.jobPosting.jobTitleName) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id and ap.state.stateName =: state group by ap.application.jobPosting.jobTitleName");
		List<Long> list3 = super.entityManager
				.createQuery(query3.toString())
				.setParameter("id", id)
				.setParameter("state", "Interview")
				.getResultList();
		
		//Query buat total applicant per job
		StringBuilder query4 = new StringBuilder();
		query4.append("select count(ap.jobPosting.jobTitleName) from Application ap where ap.jobPosting.user.id =: id group by ap.jobPosting.jobTitleName");
		List<Long> list4 = super.entityManager
				.createQuery(query4.toString())
				.setParameter("id", id)
				.getResultList();
		
		List<ReportPerJobPojo> listRp = new ArrayList<ReportPerJobPojo>();
		
		for(int i = 0; i < list.size(); i++) {
			ReportPerJobPojo rPojo = new ReportPerJobPojo();
			rPojo.setJobPosting(list.get(i));
			
			if(list2.size()<i+1) {
				rPojo.setCountHire(0L);
			}else {
				rPojo.setCountHire(list2.get(i));	
			}
			
			if(list3.size()<i+1) {
				rPojo.setCountInterview(0L);	
			}else {
				rPojo.setCountInterview(list3.get(i));
			}
			
			if(list4.size()<i+1) {
				rPojo.setCountApplicant(0L);	
			}else {
				rPojo.setCountApplicant(list4.get(i));
			}
			
			listRp.add(rPojo);
		}
		
		if(listRp.size() == 0) 
			return null;
		else
			return (List<ReportPerJobPojo>)listRp;
	}
}
