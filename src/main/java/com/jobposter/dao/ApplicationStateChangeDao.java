package com.jobposter.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.Query;
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
	public List<ReportMasterPojo> reportMaster(String id, String year) {	
		//Query buat total upload job per recruiter
		
		StringBuilder query = new StringBuilder();
		query.append("From JobPosting where user.id =: id");
		
		if(year != null) {
			query.append(" and to_char(startDate,'YYYY') =: year or to_char(endDate,'YYYY') =: year");
		}
		
		Query queryExecuted = super.entityManager.createQuery(query.toString());

		queryExecuted.setParameter("id", id);

		if(year != null) {
			queryExecuted.setParameter("year", year);
		}
		
		List<JobPosting> jpsting = queryExecuted.getResultList();
		
//		= super.entityManager.createQuery()
//		.setParameter("id", id)
//		.getResultList();
		
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
			reportPojo.setYear(year);
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
	
}
