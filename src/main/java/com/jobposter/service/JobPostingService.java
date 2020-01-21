package com.jobposter.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobPostingDao;
import com.jobposter.entity.JobPosting;
import com.jobposter.entity.JobPostingPojo;
import com.jobposter.exception.ErrorException;

@Service("jobPostingService")
public class JobPostingService {
	
	@Autowired
	private JobPostingDao jobPostingDao;

	public JobPosting findById(String id) {
		JobPosting jpost = jobPostingDao.findById(id);
		return jpost;
	}
	
	public void insert(JobPosting jpost) throws ErrorException{
		jobPostingDao.save(jpost);
	}
	
	public void update(JobPosting jpost) throws ErrorException{
		jobPostingDao.save(jpost);
	}
	
	public void delete(String id) throws ErrorException{
		jobPostingDao.delete(id);
	}
	
	public JobPosting findByBk(String Bk1, String Bk2, String Bk3, Date Bk4, Date Bk5)  throws ErrorException {
		JobPosting jpost = jobPostingDao.findByBk(Bk1, Bk2, Bk3, Bk4, Bk5);
		return jpost;
	}
	
	public List<JobPosting> findJobByRecruiter(String id) throws ErrorException {
		return jobPostingDao.findJobByRecruiter(id);
	}
	
	public List<JobPosting> findAll()  throws ErrorException { 
		List<JobPosting> jpost = jobPostingDao.findAll();
		return jpost;
	}
	
	public List<JobPostingPojo> filterJob(String city, String jobPosition, Double salaryMin, Double salaryMax) throws ErrorException {
		List<JobPostingPojo> jpost = jobPostingDao.filterJob(city, jobPosition, salaryMin, salaryMax);
		return jpost;
	}

}
