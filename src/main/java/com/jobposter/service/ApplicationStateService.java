package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ApplicationStateDao;
import com.jobposter.entity.ApplicationState;
import com.jobposter.exception.ErrorException;

@Service("applicationStateService")
public class ApplicationStateService {

	@Autowired
	private ApplicationStateDao stateDao;

	public ApplicationState findById(String id) {
		ApplicationState appl = stateDao.findById(id);
		return appl;
	}
	
	public void insert(ApplicationState state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void update(ApplicationState state) throws ErrorException{
		stateDao.save(state);
	}
	
	public void delete(String id) throws ErrorException{
		stateDao.delete(id);
	}
	
	public ApplicationState findByBk(String Bk) throws ErrorException {
		ApplicationState appl = stateDao.findById(Bk);
		return appl;
	}
	
	public List<ApplicationState> findAll() throws ErrorException{
		List<ApplicationState> state = stateDao.findAll();
		return state;
	}
}
