package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobLevelDao;
import com.jobposter.entity.JobLevel;
import com.jobposter.exception.ErrorException;

@Service("jobLevelService")
public class JobLevelService {
	
	@Autowired
	private JobLevelDao jobLevelDao;

	public JobLevel findById(String id) {
		JobLevel jl = jobLevelDao.findById(id);
		return jl;
	}
	
	public void insert(JobLevel jl) throws ErrorException{
		jobLevelDao.save(jl);
	}
	
	public void update(JobLevel jl) throws ErrorException{
		jobLevelDao.save(jl);
	}
	
	public void delete(String id) throws ErrorException{
		jobLevelDao.delete(id);
	}
	
	public JobLevel findByBk(String Bk)  throws ErrorException {
		JobLevel jl = jobLevelDao.findByBk(Bk);
		return jl;
	}
	
	public List<JobLevel> findAll()  throws ErrorException {
		List<JobLevel> jl = jobLevelDao.findAll();
		return jl;
	}

}
