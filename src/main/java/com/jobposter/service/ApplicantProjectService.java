package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicantProjectDao;
import com.jobposter.entity.ApplicantProject;
import com.jobposter.exception.ErrorException;

@Service("applicantProjectService")
public class ApplicantProjectService {

	@Autowired
	private ApplicantProjectDao applProjectDao;

	public ApplicantProject findById(String id) {
		ApplicantProject appl = applProjectDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicantProject appl) throws ErrorException{
		applProjectDao.save(appl);
	}
	
	public void update(ApplicantProject appl) throws ErrorException{
		applProjectDao.save(appl);
	}
	
	public void delete(String id) throws ErrorException{
		applProjectDao.delete(id);
	}
	
	public ApplicantProject findByBk(String Bk1, String Bk2, String Bk3)  throws ErrorException {
		ApplicantProject appl = applProjectDao.findByBk(Bk1, Bk2, Bk3);
		return appl;
	}
	
	public List<ApplicantProject> findAll() throws ErrorException {
		List<ApplicantProject> appl = applProjectDao.findAll();
		return appl;
	}
	
	public List<ApplicantProject> findAPUser(String id) throws ErrorException {
		List<ApplicantProject> appl = applProjectDao.findAPUser(id);
		return appl;
	}
}
