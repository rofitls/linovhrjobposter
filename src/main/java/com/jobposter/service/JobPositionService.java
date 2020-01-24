package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.JobPositionDao;
import com.jobposter.entity.JobPosition;
import com.jobposter.exception.ErrorException;

@Service("jobPositionService")
public class JobPositionService {

	@Autowired
	private JobPositionDao jobPositionDao;

	public JobPosition findById(String id) {
		JobPosition jp = jobPositionDao.findById(id);
		return jp;
	}
	
	public void insert(JobPosition jp) throws ErrorException{
		jobPositionDao.save(jp);
	}
	
	public void update(JobPosition jp) throws ErrorException{
		jobPositionDao.save(jp);
	}
	
	public void delete(JobPosition jp) throws ErrorException{
		jobPositionDao.delete(jp);
	}
	
	public JobPosition findByBk(String Bk)  throws ErrorException {
		JobPosition jp = jobPositionDao.findByBk(Bk);
		return jp;
	}
	
	public List<JobPosition> findAll()  throws ErrorException {
		List<JobPosition> jp = jobPositionDao.findAll();
		return jp;
	}

}
