package com.jobposter.service;

import java.util.List;

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
	
	public void delete(InterviewTestSchedule schedule) throws ErrorException{
		scheduleDao.delete(schedule);
	}
	
	public InterviewTestSchedule findByBk(String Bk1) throws ErrorException {
		return scheduleDao.findByBk(Bk1);
	}
	
	public InterviewTestSchedule findScheduleByApplication(String id) throws ErrorException {
		return scheduleDao.findScheduleByApplication(id);
	}
	
	public Long countSchedule() {
		return scheduleDao.countSchedule();
	}
	
	public List<InterviewTestSchedule> findRescheduleByJob(String id) throws ErrorException {
		return scheduleDao.findRescheduleByJob(id);
	}
	
	public List<InterviewTestSchedule> findScheduleByApplicant(String id) throws ErrorException {
		return scheduleDao.findScheduleByApplicant(id);
	}
	
	
}

