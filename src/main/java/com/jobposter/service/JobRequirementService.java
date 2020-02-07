package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobRequirementDao;
import com.jobposter.entity.JobRequirement;
import com.jobposter.exception.ErrorException;

@Service("jobRequirementService")
public class JobRequirementService {

	@Autowired
	private JobRequirementDao jobRequirementDao;

	public JobRequirement findById(String id) {
		JobRequirement jreq = jobRequirementDao.findById(id);
		return jreq;
	}
	
	public void insert(JobRequirement jreq) throws ErrorException{
		jobRequirementDao.save(jreq);
	}
	
	public void update(JobRequirement jreq) throws ErrorException{
		jobRequirementDao.save(jreq);
	}
	
	public void delete(JobRequirement jreq) throws ErrorException{
		jobRequirementDao.delete(jreq);
	}
	
	public void deleteByJob(String id) throws ErrorException {
		jobRequirementDao.deleteByJob(id);
	}
	
	public JobRequirement findByBk(String Bk)  throws ErrorException {
		JobRequirement jreq = jobRequirementDao.findByBk(Bk);
		return jreq;
	}
	
	public List<JobRequirement> findAll()  throws ErrorException {
		List<JobRequirement> jreq = jobRequirementDao.findAll();
		return jreq;
	}
	
	public List<JobRequirement> findRequirementByJobPosting(String id) throws ErrorException {
		return jobRequirementDao.findRequirementByJobPosting(id);
	}
}
