package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicantSalaryDao;
import com.jobposter.entity.ApplicantSalary;
import com.jobposter.exception.ErrorException;

@Service("applicantSalaryService")
public class ApplicantSalaryService {

	@Autowired
	private ApplicantSalaryDao applSalaryDao;

	public ApplicantSalary findById(String id) {
		ApplicantSalary appl = applSalaryDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicantSalary appl) throws ErrorException{
		applSalaryDao.save(appl);
	}
	
	public void update(ApplicantSalary appl) throws ErrorException{
		applSalaryDao.save(appl);
	}
	
	public void delete(ApplicantSalary appl) throws ErrorException{
		applSalaryDao.delete(appl);
	}
	
	public ApplicantSalary findByBk(String Bk1) throws ErrorException {
		ApplicantSalary appl = applSalaryDao.findByBk(Bk1);
		return appl;
	}
	
	public List<ApplicantSalary> findAll() throws ErrorException {
		List<ApplicantSalary> appl = applSalaryDao.findAll();
		return appl;
	}
	
}
