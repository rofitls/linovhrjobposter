package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicantSkillDao;
import com.jobposter.entity.ApplicantSkill;
import com.jobposter.exception.ErrorException;

@Service("applicantSkillService")
public class ApplicantSkillService {
	
	@Autowired
	private ApplicantSkillDao applSkillDao;

	public ApplicantSkill findById(String id) {
		ApplicantSkill appl = applSkillDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicantSkill appl) throws ErrorException{
		applSkillDao.save(appl);
	}
	
	public void update(ApplicantSkill appl) throws ErrorException{
		applSkillDao.save(appl);
	}
	
	public void delete(String id) throws ErrorException{
		applSkillDao.delete(id);
	}
	
	public ApplicantSkill findByBk(String Bk1, String Bk2, String Bk3) throws ErrorException {
		ApplicantSkill appl = applSkillDao.findByBk(Bk1, Bk2, Bk3);
		return appl;
	}
	
	public List<ApplicantSkill> findAll() throws ErrorException{
		List<ApplicantSkill> appl = applSkillDao.findAll();
		return appl;
	}
	
	public List<ApplicantSkill> findASUser(String id) throws ErrorException{
		List<ApplicantSkill> appl = applSkillDao.findASUser(id);
		return appl;
	}

}
