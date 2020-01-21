package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicationStateChangeDao;
import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.exception.ErrorException;

@Service("applicationStateChangeService")
public class ApplicationStateChangeService {

	@Autowired
	private ApplicationStateChangeDao stateDao;

	public ApplicationStateChange findById(String id) {
		ApplicationStateChange appl = stateDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicationStateChange state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void update(ApplicationStateChange state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void delete(String id) throws ErrorException{
		stateDao.delete(id);
	}
	
	public ApplicationStateChange findByBk(String Bk1) throws ErrorException {
		ApplicationStateChange appl = stateDao.findByBk(Bk1);
		return appl;
	}
	
	public List<ApplicationStateChange> findAll() throws ErrorException{
		List<ApplicationStateChange> appl = stateDao.findAll();
		return appl;
	}
	
	public ApplicationStateChange findByApplicantNotViewed(String id) throws Exception {
		return stateDao.findByApplicationNotViewed(id);
	}
	
	public Long findApplicationHire(String id) throws Exception {
		return stateDao.findApplicationHire(id);
	}
}
