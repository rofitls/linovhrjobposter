package com.jobposter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.InterviewTestScheduleDao;
import com.jobposter.entity.InterviewTestSchedule;
import com.jobposter.exception.ErrorException;

@Service("interviewTestScheduleService")
public class InterviewTestScheduleService {

	@Autowired
	private InterviewTestScheduleDao scheduleDao;

	public InterviewTestSchedule findById(String id) {
		InterviewTestSchedule schedule = scheduleDao.findById(id);
		return schedule;
	}
	
	public void insert(InterviewTestSchedule schedule) throws ErrorException{
		scheduleDao.save(schedule);
	}
	
	public void update(InterviewTestSchedule schedule) throws ErrorException{
		scheduleDao.save(schedule);
	}
	
	public void delete(String id) throws ErrorException{
		scheduleDao.delete(id);
	}
}

