package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobDescriptionDao;
import com.jobposter.entity.JobDescription;
import com.jobposter.exception.ErrorException;

@Service("jobDescriptionService")
public class JobDescriptionService {

	@Autowired
	private JobDescriptionDao jobDescriptionDao;

	public JobDescription findById(String id) {
		JobDescription jdesc = jobDescriptionDao.findById(id);
		return jdesc;
	}
	
	public void insert(JobDescription jdesc) throws ErrorException{
		jobDescriptionDao.save(jdesc);
	}
	
	public void update(JobDescription jdesc) throws ErrorException{
		jobDescriptionDao.save(jdesc);
	}
	
	public void delete(String id) throws ErrorException{
		jobDescriptionDao.delete(id);
	}
	
	public JobDescription findByBk(String Bk) throws ErrorException {
		JobDescription jdesc = jobDescriptionDao.findByBk(Bk);
		return jdesc;
	}
	
	public List<JobDescription> findAll() throws ErrorException {
		List<JobDescription> jdesc = jobDescriptionDao.findAll();
		return jdesc;
	}
	
	public List<JobDescription> findDescriptionByJobPosting(String id) throws ErrorException {
		return jobDescriptionDao.findDescriptionByJobPosting(id);
	}
	
	public Long countJobDescription() {
		return jobDescriptionDao.countJobDescription();
	}
}
