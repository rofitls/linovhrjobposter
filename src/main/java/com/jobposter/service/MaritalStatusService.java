package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.MaritalStatusDao;
import com.jobposter.entity.MaritalStatus;
import com.jobposter.exception.ErrorException;

@Service("maritalStatusService")
public class MaritalStatusService {
	
	@Autowired
	private MaritalStatusDao maritalStatusDao;

	public MaritalStatus findById(String id) {
		MaritalStatus ms = maritalStatusDao.findById(id);
		return ms;
	}
	
	public void insert(MaritalStatus ms) throws ErrorException{
		maritalStatusDao.save(ms);
	}
	
	public void update(MaritalStatus ms) throws ErrorException{
		maritalStatusDao.save(ms);
	}
	
	public void delete(MaritalStatus ms) throws ErrorException{
		maritalStatusDao.delete(ms);
	}
	
	public MaritalStatus findByBk(String BK) throws ErrorException {
		MaritalStatus ms = maritalStatusDao.findByBk(BK);
		return ms;
	}
	
	public List<MaritalStatus> findAll()  throws ErrorException {
		List<MaritalStatus> ms = maritalStatusDao.findAll();
		return ms;
	}

}
