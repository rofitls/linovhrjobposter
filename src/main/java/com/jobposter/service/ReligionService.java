package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ReligionDao;
import com.jobposter.entity.Religion;
import com.jobposter.exception.ErrorException;

@Service("religionService")
public class ReligionService {

	@Autowired
	private ReligionDao religionDao;

	public Religion findById(String id) {
		Religion religion = religionDao.findById(id);
		return religion;
	}
	
	public void insert(Religion religion) throws ErrorException{
		religionDao.save(religion);
	}
	
	public void update(Religion religion) throws ErrorException{
		religionDao.save(religion);
	}
	
	public void delete(Religion religion) throws ErrorException{
		religionDao.delete(religion);
	}
	
	public Religion findByBk(String BK) throws ErrorException {
		Religion religion = religionDao.findByBk(BK);
		return religion;
	}
	
	public List<Religion> findAll()  throws ErrorException {
		List<Religion> religion = religionDao.findAll();
		return religion;
	}
}
