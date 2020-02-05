package com.jobposter.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.JobPosting;
import com.jobposter.entity.ReportSubReportPojo;
import com.jobposter.entity.ReportMasterPojo;
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
	public List<ReportMasterPojo> reportMaster(String id) {	
		//Query buat total upload job per recruiter
		
		List<JobPosting> jpsting = super.entityManager.createQuery("From JobPosting where user.id =: id")
				.setParameter("id", id)
				.getResultList();
		
		List<ReportMasterPojo> masterPojo = new ArrayList<ReportMasterPojo>();
		
		for(JobPosting jp : jpsting) {
			
			ReportMasterPojo reportPojo = new ReportMasterPojo();
			
			Long list5 =(Long)  super.entityManager.createQuery("select count(*) from JobPosting jp where jp.user.id =: id").setParameter("id", jp.getUser().getId()).getSingleResult();	
			
			Long list2 = (Long) super.entityManager.createQuery("select count(*) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id"
					+ " and ap.state.stateName =: state"
					+ " and ap.application.jobPosting.id =: id2")
					.setParameter("id", jp.getUser().getId())
					.setParameter("state", "Hire")
					.setParameter("id2", jp.getId())
					.getSingleResult();
			
			Long list3 = (Long) super.entityManager.createQuery("select count(*) from ApplicationStateChange ap where ap.application.jobPosting.user.id =: id"
					+ " and ap.state.stateName =: state"
					+ " and ap.application.jobPosting.id =: id2")
					.setParameter("id", jp.getUser().getId())
					.setParameter("state", "Interview")
					.setParameter("id2", jp.getId())
					.getSingleResult();
			
			Long list4 = (Long) super.entityManager.createQuery("select count(*) from Application ap where ap.jobPosting.user.id =: id"
					+ " and ap.jobPosting.id =: id2")
					.setParameter("id", jp.getUser().getId())
					.setParameter("id2", jp.getId())
					.getSingleResult();
			
			reportPojo.setJobPosting(jp.getJobTitleName());
			reportPojo.setRecruiterName(jp.getUser().getFirstName()+ " " +jp.getUser().getLastName());
			reportPojo.setCountHire(list2);
			reportPojo.setCountInterview(list3);
			reportPojo.setCountApplicant(list4);
			reportPojo.setTotalUploadJob(list5);
			masterPojo.add(reportPojo);
		}

		if(masterPojo.size() == 0) 
			return null;
		else
			return (List<ReportMasterPojo>)masterPojo;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ReportSubReportPojo> reportPerJob(String id) {
		
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
		
		List<ReportSubReportPojo> listRp = new ArrayList<ReportSubReportPojo>();
		
		for(int i = 0; i < list.size(); i++) {
			ReportSubReportPojo rPojo = new ReportSubReportPojo();
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
			return (List<ReportSubReportPojo>)listRp;
	}
}
