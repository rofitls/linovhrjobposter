package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.EducationLevelDao;
import com.jobposter.entity.EducationLevel;
import com.jobposter.exception.ErrorException;

@Service("educationLevelService")
public class EducationLevelService {

	@Autowired
	private EducationLevelDao educationLevelDao;

	public EducationLevel findById(String id) {
		EducationLevel el = educationLevelDao.findById(id);
		return el;
	}
	
	public void insert(EducationLevel el) throws ErrorException{
		educationLevelDao.save(el);
	}
	
	public void update(EducationLevel el) throws ErrorException{
		educationLevelDao.save(el);
	}
	
	public void delete(String id) throws ErrorException{
		educationLevelDao.delete(id);
	}
	
	public EducationLevel findByBk(String Bk)  throws ErrorException {
		EducationLevel el = educationLevelDao.findByBk(Bk);
		return el;
	}
	
	public List<EducationLevel> findAll()  throws ErrorException {
		List<EducationLevel> el = educationLevelDao.findAll();	
		return el;
	}
}
