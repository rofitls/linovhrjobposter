package com.jobposter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobposter.entity.InterviewTestSchedule;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.InterviewTestScheduleService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class InterviewTestScheduleController {
	
	@Autowired
	private InterviewTestScheduleService scheduleService;
	
	@PostMapping("/schedule")
	public ResponseEntity<?> insert(@RequestBody InterviewTestSchedule schedule) throws ErrorException{
		try {
			valIdNull(schedule);
			valBkNotNull(schedule);
			valBkNotExist(schedule);
			valNonBk(schedule);
			scheduleService.insert(schedule);
			schedule.getApplication().setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
	}
	
	@PutMapping("/schedule")
	public ResponseEntity<?> update(@RequestBody InterviewTestSchedule schedule) throws ErrorException{
		try {
			valIdNotNull(schedule);
			valIdExist(schedule.getId());
			valBkNotNull(schedule);
			valBkNotChange(schedule);
			valNonBk(schedule);
			scheduleService.update(schedule);
			schedule.getApplication().setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(schedule);
	}
	
	@DeleteMapping("/schedule/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			InterviewTestSchedule schedule = scheduleService.findById(id);
			scheduleService.delete(schedule);
			schedule.getApplication().setUser(null);
			return ResponseEntity.ok(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/schedule/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			InterviewTestSchedule schedule = scheduleService.findById(id);
			schedule.getApplication().setUser(null);
			return ResponseEntity.ok(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/schedule/reschedule-list-per-job/{id}")
	public ResponseEntity<?> getRescheduleByJob(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.ok(scheduleService.findRescheduleByJob(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(InterviewTestSchedule schedule) throws Exception {
		if(schedule.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(InterviewTestSchedule schedule) throws Exception{
		if(schedule.getId()==null) {
			throw new Exception("Education Level doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(scheduleService.findById(id)==null) {
			throw new Exception("Schedule doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(InterviewTestSchedule schedule) throws Exception{
		if(schedule.getInterviewCode()==null) {
			throw new Exception("Schedule code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (InterviewTestSchedule schedule) throws Exception{
		if(scheduleService.findByBk(schedule.getInterviewCode())!=null) {
			throw new Exception("Education Level already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(InterviewTestSchedule schedule) throws Exception{
		if(!schedule.getInterviewCode().equalsIgnoreCase(scheduleService.findById(schedule.getId()).getInterviewCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(InterviewTestSchedule schedule) throws Exception {
		if(schedule.getInterviewDate()== null) {
			throw new Exception("Interview date must be filled");
		}else if(schedule.getInterviewTime()==null) {
			throw new Exception("Interview time must be filled");
		}
		return null;
	}
	
}

