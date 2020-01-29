package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.MajorDao;
import com.jobposter.entity.Major;
import com.jobposter.exception.ErrorException;

@Service("majorService")
public class MajorService {

	@Autowired
	private MajorDao majorDao;

	public Major findById(String id) {
		Major major = majorDao.findById(id);
		return major;
	}
	
	public void insert(Major major) throws ErrorException{
		majorDao.save(major);
	}
	
	public void update(Major major) throws ErrorException{
		majorDao.save(major);
	}
	
	public void delete(Major major) throws ErrorException{
		majorDao.delete(major);
	}
	
	public Major findByBk(String BK) throws ErrorException {
		Major major = majorDao.findByBk(BK);
		return major;
	}
	
	public List<Major> findByEduLevel(String id) throws ErrorException {
		List<Major> major = majorDao.findByEduLevel(id);
		return major;
	}
	
	public List<Major> findAll()  throws ErrorException {
		List<Major> major = majorDao.findAll();
		return major;
	}
}
