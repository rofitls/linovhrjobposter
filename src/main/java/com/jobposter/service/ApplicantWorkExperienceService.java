package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicantWorkExperienceDao;
import com.jobposter.entity.ApplicantWorkExperience;
import com.jobposter.exception.ErrorException;

@Service("applicantWorkExperienceService")
public class ApplicantWorkExperienceService {

	@Autowired
	private ApplicantWorkExperienceDao applWorkExpDao;

	public ApplicantWorkExperience findById(String id) {
		ApplicantWorkExperience appl = applWorkExpDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicantWorkExperience appl) throws ErrorException{
		applWorkExpDao.save(appl);
	}
	
	public void update(ApplicantWorkExperience appl) throws ErrorException{
		applWorkExpDao.save(appl);
	}
	
	public void delete(String id) throws ErrorException{
		applWorkExpDao.delete(id);
	}
	
	public ApplicantWorkExperience findByBk(String Bk1, String Bk2, String Bk3, String Bk4) throws ErrorException {
		ApplicantWorkExperience appl = applWorkExpDao.findByBk(Bk1, Bk2, Bk3, Bk4);
		return appl;
	}
	
	public List<ApplicantWorkExperience> findAll() throws ErrorException{
		List<ApplicantWorkExperience> appl = applWorkExpDao.findAll();
		return appl;
	}
	
	public List<ApplicantWorkExperience> findAWEUser(String id) throws ErrorException{
		List<ApplicantWorkExperience> appl = applWorkExpDao.findAWEUser(id);
		return appl;
	}
	
}
