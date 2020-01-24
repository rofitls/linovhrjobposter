package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobQuotaDao;
import com.jobposter.entity.JobQuota;
import com.jobposter.exception.ErrorException;

@Service("jobQuotaService")
public class JobQuotaService {

	@Autowired
	private JobQuotaDao jobQuotaDao;

	public JobQuota findById(String id) {
		JobQuota jq = jobQuotaDao.findById(id);
		return jq;
	}
	
	public void insert(JobQuota jq) throws ErrorException{
		jobQuotaDao.save(jq);
	}
	
	public void update(JobQuota jq) throws ErrorException{
		jobQuotaDao.save(jq);
	}
	
	public void delete(JobQuota jq) throws ErrorException{
		jobQuotaDao.delete(jq);
	}
	
//	public JobPosting findByBk(String Bk1, String Bk2, String Bk3, Date Bk4, Date Bk5)  throws ErrorException {
//		JobPosting jpost = jobQuotaDao.findByBk(Bk1, Bk2, Bk3, Bk4, Bk5);
//		return jpost;
//	}
	
	public List<JobQuota> findAll()  throws ErrorException { 
		List<JobQuota> jq = jobQuotaDao.findAll();
		return jq;
	}
	
	public Integer findJobQuota(String id) throws ErrorException {
		return jobQuotaDao.findJobQuota(id);
	}
	
}
