package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicantEducationDao;
import com.jobposter.entity.ApplicantEducation;
import com.jobposter.exception.ErrorException;

@Service("applicantEducationService")
public class ApplicantEducationService {
	
	@Autowired
	private ApplicantEducationDao applEducationDao;

	public ApplicantEducation findById(String id) {
		ApplicantEducation appl = applEducationDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicantEducation appl) throws ErrorException{
		applEducationDao.save(appl);
	}
	
	public void update(ApplicantEducation appl) throws ErrorException{
		applEducationDao.save(appl);
	}
	
	public void delete(ApplicantEducation appl) throws ErrorException{
		applEducationDao.delete(appl);
	}
	
	public ApplicantEducation findByBk(String Bk1, String Bk2, String Bk3)  throws ErrorException{
		ApplicantEducation appl = applEducationDao.findByBk(Bk1, Bk2, Bk3);
		return appl;
	}
	
	public List<ApplicantEducation> findAll() throws ErrorException{
		List<ApplicantEducation> appl = applEducationDao.findAll();
		return appl;
	}
	
	public List<ApplicantEducation> findAEUser(String id) throws ErrorException{
		List<ApplicantEducation> appl = applEducationDao.findAEUser(id);
		return appl;
	}
	
	public ApplicantEducation findRecentEducationApplicant(String id) throws ErrorException {
		return applEducationDao.findRecentEducationApplicant(id);
	}

}
