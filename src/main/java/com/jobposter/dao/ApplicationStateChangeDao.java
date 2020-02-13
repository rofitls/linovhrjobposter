package com.jobposter.dao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Application;
import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.InterviewTestSchedule;
import com.jobposter.entity.JobPosting;
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
	public List<ApplicationStateChange> findApplicationHireList(String id){
		StringBuilder query = new StringBuilder();
		query.append("from ApplicationStateChange ap where ap.application.jobPosting.id =: id and ap.state.stateName =: state");
		List<ApplicationStateChange> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("id", id)
				.setParameter("state", "Hire")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicationStateChange>)list;
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
			return 0L;
		else
			return (Long)list.get(0);
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ReportMasterPojo> reportPerJob(String job) {
		
		List<JobPosting> jpsting = super.entityManager
				.createQuery("from JobPosting where id =: id")
				.setParameter("id", job)
				.getResultList();
		
		Long totalApplicant = (Long) super.entityManager.createQuery("select count(*) from Application ap where ap.jobPosting.id =: id")
				.setParameter("id", job)
				.getSingleResult();
		
		Long countHire = (Long) super.entityManager.createQuery("select count(*) from ApplicationStateChange ap where ap.state.stateName =: state"
				+ " and ap.application.jobPosting.id =: id2")
				.setParameter("state", "Hire")
				.setParameter("id2", job)
				.getSingleResult();
		
		Long countInterview = (Long) super.entityManager.createQuery("select count(*) from ApplicationStateChange ap and ap.state.stateName =: state"
				+ " and ap.application.jobPosting.id =: id2")
				.setParameter("state", "Interview")
				.setParameter("id2", job)
				.getSingleResult();
		
		List<Application> application = super.entityManager
				.createQuery("from Application ap where ap.jobPosting.id =: id order by ap.user.firstName")
				.setParameter("id", job)
				.getResultList();
		
		List<String> firstName = super.entityManager
				.createQuery("select ap.user.firstName from Application ap where ap.jobPosting.id =: id order by ap.user.firstName")
				.setParameter("id", job)
				.getResultList();
		
		List<String> lastName = super.entityManager
				.createQuery("select ap.user.lastName from Application ap where ap.jobPosting.id =: id order by ap.user.firstName")
				.setParameter("id", job)
				.getResultList();
		
		List<String> state = super.entityManager
				.createQuery("select ap.state.stateName from ApplicationStateChange ap where ap.application.jobPosting.id =: id order by ap.application.user.firstName")
				.setParameter("id", job)
				.getResultList();
		
		List<ReportMasterPojo> reportPojo = new ArrayList<ReportMasterPojo>();
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		for(int i = 0; i < totalApplicant; i++) {
			ReportMasterPojo rp = new ReportMasterPojo();
			rp.setApplicantName(firstName.get(i) + " " + lastName.get(i));
			rp.setJobPosting(jpsting.get(0).getJobTitleName());
			rp.setCompanyName(jpsting.get(0).getCompany());
			rp.setStartDate(dateFormat.format(jpsting.get(0).getStartDate()));
			rp.setEndDate(dateFormat.format(jpsting.get(0).getEndDate()));
			rp.setCountApplicant(totalApplicant);
			rp.setCountHire(countHire);
			rp.setCountInterview(countInterview);
			rp.setState(state.get(i));
			List<InterviewTestSchedule> schedule = super.entityManager
					.createQuery("from InterviewTestSchedule ap where ap.application.id =: id")
					.setParameter("id", application.get(i).getId())
					.getResultList();
			if(schedule.size() == 0) {
				rp.setResult("-");	
			}else {
				rp.setResult(schedule.get(0).getInterviewResult());
			}
			reportPojo.add(rp);
		}
		
		if(reportPojo.size() == 0)
			return null;
		else
			return (List<ReportMasterPojo>)reportPojo;	
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ReportMasterPojo> reportMaster(String recruiter, String year) {	
		//Query buat total upload job per recruiter
		
		StringBuilder query = new StringBuilder();
		query.append("From JobPosting where user.id =: id");
		
		if(year != null && !year.equalsIgnoreCase("null")) {
			query.append(" and to_char(startDate,'YYYY') =: year or to_char(endDate,'YYYY') =: year");
		}
		
		Query queryExecuted = super.entityManager.createQuery(query.toString());

		queryExecuted.setParameter("id", recruiter);

		if(year != null && !year.equalsIgnoreCase("null")) {
			queryExecuted.setParameter("year", year);
		}
		
		List<JobPosting> jpsting = super.entityManager.createQuery(queryExecuted.toString())
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
			if(year != null) {
				reportPojo.setYear(year);
			}
			masterPojo.add(reportPojo);
		}

		if(masterPojo.size() == 0) 
			return null;
		else
			return (List<ReportMasterPojo>)masterPojo;
	}
	
}
