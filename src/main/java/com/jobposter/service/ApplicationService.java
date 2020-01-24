package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicationDao;
import com.jobposter.entity.Application;
import com.jobposter.exception.ErrorException;

@Service("applicationService")
public class ApplicationService {

	@Autowired
	private ApplicationDao applDao;

	public Application findById(String id) throws ErrorException {
		Application appl = applDao.findById(id);
		return appl;
	}
	
	public void insert(Application appl) throws ErrorException{
		applDao.save(appl);
	}
	
	public void update(Application appl) throws ErrorException{
		applDao.save(appl);
	}
	
	public void delete(Application appl) throws ErrorException{
		applDao.delete(appl);
	}
	
	public Application findByBk(String Bk1, String Bk2)  throws ErrorException {
		Application appl = applDao.findByBk(Bk1, Bk2);
		return appl;
	}
	
	public List<Application> findAll()  throws ErrorException{
		List<Application> appl = applDao.findAll();
		return appl;
	}
	
	public Application findAppByApplicant(String id) throws ErrorException {
		return applDao.findAppByApplicant(id);
	}
	
	public Long countApplicationByJobPosting(String id) throws ErrorException {
		return applDao.countApplicationByJobPosting(id);
	}
}
