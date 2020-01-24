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
			scheduleService.insert(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(schedule);
	}
	
	@PutMapping("/schedule")
	public ResponseEntity<?> update(@RequestBody InterviewTestSchedule schedule) throws ErrorException{
		try {
			scheduleService.update(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(schedule);
	}
	
	@DeleteMapping("/schedule/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			InterviewTestSchedule schedule = scheduleService.findById(id);
			scheduleService.delete(schedule);
			return ResponseEntity.ok(schedule);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/schedule/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(scheduleService.findById(id));
	}
}

