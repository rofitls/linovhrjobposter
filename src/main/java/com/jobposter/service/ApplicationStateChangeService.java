package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicationStateChangeDao;
import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.ReportPerJobPojo;
import com.jobposter.entity.ReportPojo;
import com.jobposter.exception.ErrorException;

@Service("applicationStateChangeService")
public class ApplicationStateChangeService {

	@Autowired
	private ApplicationStateChangeDao stateDao;

	public ApplicationStateChange findById(String id) {
		ApplicationStateChange appl = stateDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicationStateChange state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void update(ApplicationStateChange state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void delete(ApplicationStateChange state) throws ErrorException{
		stateDao.delete(state);
	}
	
	public ApplicationStateChange findByBk(String Bk1) throws ErrorException {
		ApplicationStateChange appl = stateDao.findByBk(Bk1);
		return appl;
	}
	
	public List<ApplicationStateChange> findAll() throws ErrorException{
		List<ApplicationStateChange> appl = stateDao.findAll();
		return appl;
	}
	
	public List<ApplicationStateChange> listApplicationByUser(String id) throws Exception {
		return stateDao.listApplicationByUser(id);
	}
	
	public List<ApplicationStateChange> listApplicationByJob(String id)throws ErrorException {
		return stateDao.listApplicationByJob(id);
	}
	
	public ApplicationStateChange findByApplicantNotViewed(String id) throws Exception {
		return stateDao.findByApplicationNotViewed(id);
	}
	
	public Long findApplicationHire(String id) throws Exception {
		return stateDao.findApplicationHire(id);
	}
	
	public List<ReportPojo> reportMaster(String id) throws Exception {
		return stateDao.reportMaster(id);
	}
	
	public List<ReportPerJobPojo> reportPerJob(String id) throws ErrorException {
		return stateDao.reportPerJob(id);
	}
	
	public List<String> coba(String id) throws ErrorException {
		return stateDao.coba(id);
	}
	
	public List<Long> coba2(String id) throws ErrorException {
		return stateDao.coba2(id);
	}
	
	public List<Long> coba3(String id) throws ErrorException {
		return stateDao.coba3(id);
	}
	
	public List<Long> coba4(String id) throws ErrorException {
		return stateDao.coba4(id);
	}
}
