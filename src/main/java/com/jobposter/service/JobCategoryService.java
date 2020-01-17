package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobCategoryDao;
import com.jobposter.entity.JobCategory;
import com.jobposter.exception.ErrorException;

@Service("jobCategoryService")
public class JobCategoryService {
	
	@Autowired
	private JobCategoryDao jobCategoryDao;

	public JobCategory findById(String id) {
		JobCategory jc = jobCategoryDao.findById(id);
		return jc;
	}
	
	public void insert(JobCategory jc) throws ErrorException{
		jobCategoryDao.save(jc);
	}
	
	public void update(JobCategory jc) throws ErrorException{
		jobCategoryDao.save(jc);
	}
	
	public void delete(String id) throws ErrorException{
		jobCategoryDao.delete(id);
	}
	
	public JobCategory findByBk(String Bk) throws ErrorException {
		JobCategory jc = jobCategoryDao.findByBk(Bk);
		return jc;
	}

	public List<JobCategory> findAll() throws ErrorException {
		List<JobCategory> jc = jobCategoryDao.findAll();
		return jc;
	}
}
