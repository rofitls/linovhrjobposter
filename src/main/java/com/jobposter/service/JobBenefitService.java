package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobBenefitDao;
import com.jobposter.entity.JobBenefit;
import com.jobposter.exception.ErrorException;

@Service("jobBenefitService")
public class JobBenefitService {
	
	@Autowired
	private JobBenefitDao jobBenefitDao;

	public JobBenefit findById(String id) {
		JobBenefit jBenefit = jobBenefitDao.findById(id);
		return jBenefit;
	}
	
	public void insert(JobBenefit jBenefit) throws ErrorException{
		jobBenefitDao.save(jBenefit);
	}
	
	public void update(JobBenefit jBenefit) throws ErrorException{
		jobBenefitDao.save(jBenefit);
	}
	
	public void delete(JobBenefit jBenefit) throws ErrorException{
		jobBenefitDao.delete(jBenefit);
	}
	
	public void deleteByJob(String id) throws ErrorException {
		jobBenefitDao.deleteByJob(id);
	}
	
	public JobBenefit findByBk(String Bk) throws ErrorException {
		JobBenefit jdesc = jobBenefitDao.findByBk(Bk);
		return jdesc;
	}
	
	public List<JobBenefit> findAll() throws ErrorException {
		List<JobBenefit> jdesc = jobBenefitDao.findAll();
		return jdesc;
	}
	
	public List<JobBenefit> findBenefitByJobPosting(String id) throws ErrorException {
		return jobBenefitDao.findBenefitByJobPosting(id);
	}

}
