package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.SkillLevelDao;
import com.jobposter.entity.SkillLevel;
import com.jobposter.exception.ErrorException;

@Service("skillLevelService")
public class SkillLevelService {
	
	@Autowired
	private SkillLevelDao skillLevelDao;

	public SkillLevel findById(String id) {
		SkillLevel sl = skillLevelDao.findById(id);
		return sl;
	}
	
	public void insert(SkillLevel sl) throws ErrorException{
		skillLevelDao.save(sl);
	}
	
	public void update(SkillLevel sl) throws ErrorException{
		skillLevelDao.save(sl);
	}
	
	public void delete(String id) throws ErrorException{
		skillLevelDao.delete(id);
	}
	
	public SkillLevel findByBk(String Bk)  throws ErrorException {
		SkillLevel sl = skillLevelDao.findByBk(Bk);
		return sl;
	}
	
	public List<SkillLevel> findAll()  throws ErrorException {
		List<SkillLevel> sl = skillLevelDao.findAll();
		return sl;
	}

}
