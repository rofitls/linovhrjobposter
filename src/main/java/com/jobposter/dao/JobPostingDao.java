package com.jobposter.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Query;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.JobPosting;
import com.jobposter.entity.JobPostingPojo;

@Repository
public class JobPostingDao extends CommonDao {

	@Transactional
	public void save (JobPosting jpost) {
		super.entityManager.merge(jpost);
	}
	
	@Transactional
	public void delete(JobPosting jpost) {
		super.entityManager.remove(jpost);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobPosting findById(String id) {
		List<JobPosting> list = super.entityManager
				.createQuery("from JobPosting where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobPosting)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobPosting> findAll(){
		List<JobPosting> list = super.entityManager
				.createQuery("from JobPosting where activeState =: active order by startDate desc")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobPosting>)list;
	}
	
//	public List<JobPosting> findAllWithPagination(){
//		Query query = super.entityManager
//				.createQuery("from JobPosting where activeState =: active order by startDate desc")
//				.setParameter("active", true);
//		Query countPage= super.entityManager
//				.createQuery("select count(*) from JobPosting where activeState =: active order by startDate desc")
//				.setParameter("active", true);
//		Long countResult = (Long)countPage.getSingleResult();
//		int pageSize = 10;
//		int pageNumber = (int) ((countResult / pageSize) + 1);
//		query.setFirstResult((pageNumber-1) * pageSize); 
//		query.setMaxResults(pageSize);
//		
//	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public JobPosting findByBk(String Bk1, String Bk2, String Bk3, Date Bk4, Date Bk5, String Bk6) {
		List<JobPosting> list = super.entityManager
				.createQuery("from JobPosting where user.id =: bk1 and jobPosition.id =: bk2 and city.id =: bk3 and startDate =: bk4 and endDate =: bk5 and company =: bk6")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.setParameter("bk4", Bk4)
				.setParameter("bk5", Bk5)
				.setParameter("bk6", Bk6)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (JobPosting)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobPosting> findJobByRecruiter(String id){
		List<JobPosting> list = super.entityManager
				.createQuery("from JobPosting where user.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobPosting>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobPosting> recomendationJob(String jobCategory, Double salaryExpected){
		List<JobPosting> list = super.entityManager
				.createQuery("from JobPosting where jobPosition.jobCategory.jobCategoryName =: jobCategory or salary =: salaryExpected")
				.setParameter("jobCategory", jobCategory)
				.setParameter("salaryExpected", salaryExpected)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<JobPosting>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long reportUploadJobPerRecruiter(String id, String month, String year) {
		StringBuilder query = new StringBuilder();
		query.append("select count(jp) from JobPosting jp where jp.user.id =: id");
		if(month != null) {
			query.append(" group by month(:month)");
		}
		if(year != null) {
			query.append(" group by year(:year)");
		}
		
		Query queryExecuted = super.entityManager.createQuery(query.toString());
		
		queryExecuted.setParameter("id", id);
		if(month != null) {
			queryExecuted.setParameter("month", month);
		}
		if(year != null) {
			queryExecuted.setParameter("year", year);
		}
				
		List<Long> list = queryExecuted.getResultList();
		
		if(month == null && year == null) {
			return null;
		}else if(list.size() == 0) {
			return null;
		}else {
			return (Long)list.get(0);
		}
				
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<JobPosting> filterJob(String province, String jobCategory, Double salaryMin, Double salaryMax){
		
		StringBuilder query = new StringBuilder();
		//query.append("select jp.salary, jp.startDate, jp.endDate, jp.city.cityName, jp.jobPosition.jobPositionName from JobPosting jp where 1=1");
		query.append("from JobPosting jp where 1=1");
		if(province != null) {
			query.append(" and lower(jp.city.province.provinceName) like : field1");
		}
		if(jobCategory != null) {
			query.append(" and lower (jp.jobPosition.jobCategory.jobCategoryName) like : field2");
		}
		if(salaryMin != null) {
			query.append(" and jp.salary >= : field3");
		}
		if(salaryMax != null) {
			query.append(" and jp.salary <= : field4");
		}
		
		
		Query queryExecuted = super.entityManager.createQuery(query.toString());
		
		if(province != null) {
			queryExecuted.setParameter("field1", "%" + province.toLowerCase() + "%");
		}
		if(jobCategory != null) {
			queryExecuted.setParameter("field2", "%" + jobCategory.toLowerCase() + "%");
		}
		if(salaryMin != null) {
			queryExecuted.setParameter("field3", salaryMin);
		}
		if(salaryMax != null) {
			queryExecuted.setParameter("field4", salaryMax);
		}
		
		List<JobPosting> list = queryExecuted.getResultList();
//		List<JobPostingPojo> jPostPojoList = new ArrayList<JobPostingPojo>();
//		for(JobPosting jp : list) {
//			JobPostingPojo jPostPojo = new JobPostingPojo();
//			jPostPojo.setId(jp.getId());
//			jPostPojo.setCityName(jp.getCity().getCityName());
//			jPostPojo.setSalary(jp.getSalary());
//			jPostPojo.setUsername(jp.getUser().getUsername());
//			jPostPojo.setStartDate(jp.getStartDate());
//			jPostPojo.setEndDate(jp.getEndDate());
//			jPostPojo.setJobTitleName(jp.getJobTitleName());
//			jPostPojo.setJobPosition(jp.getJobPosition().getJobPositionName());
//			jPostPojoList.add(jPostPojo);
//		}
		if(list.size()==0)
			return null;
		else
			return (List<JobPosting>)list;
	}

}
