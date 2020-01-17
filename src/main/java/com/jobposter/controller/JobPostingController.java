package com.jobposter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobposter.entity.FilterJob;
import com.jobposter.entity.JobPosting;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.CityService;
import com.jobposter.service.JobPositionService;
import com.jobposter.service.JobPostingService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/admin")
public class JobPostingController {
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobPositionService jobPositionService;
	
	@Autowired
	private CityService cityService;
	
	@PostMapping("/job-posting")
	public ResponseEntity<?> insert(@RequestBody JobPosting jpost) throws ErrorException{
		try {
			valIdNull(jpost);
			valBkNotNull(jpost);
			valBkNotExist(jpost);
			valNonBk(jpost);
			jobPostingService.insert(jpost);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/job-posting")
	public ResponseEntity<?> update(@RequestBody JobPosting jpost) throws ErrorException{
		try {
			valIdNotNull(jpost);
			valIdExist(jpost.getId());
			valBkNotNull(jpost);
			valBkNotChange(jpost);
			valNonBk(jpost);
			jobPostingService.update(jpost);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@DeleteMapping("/job-posting/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			jobPostingService.delete(id);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/job-posting/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(jobPostingService.findById(id));
	}
	
	@GetMapping("/job-posting")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(jobPostingService.findAll());
	}
	
	@GetMapping("/job-posting/filter")
	public ResponseEntity<?> filterJob(@RequestBody FilterJob filter) throws ErrorException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(jobPostingService.filterJob(filter.getCityName(), filter.getJobPositionName(),filter.getSalaryMin(),filter.getSalaryMax()));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(JobPosting jpost) throws Exception {
		if(jpost.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobPosting jpost) throws Exception{
		if(jpost.getId()==null) {
			throw new Exception("Job Posting doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobPostingService.findById(id)==null) {
			throw new Exception("Job Posting doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobPosting jpost) throws Exception{
		if(jpost.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(jpost.getJobPosition()==null) {
			throw new Exception("Job Position must be filled");
		}else if(jpost.getCity()==null) {
			throw new Exception("City must be filled");
		}else if(jpost.getStartDate()==null) {
			throw new Exception("Start date must be filled");
		}else if(jpost.getEndDate()==null) {
			throw new Exception("End date must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobPosting jpost) throws Exception{
		if(jobPostingService.findByBk(jpost.getUser().getId(),jpost.getJobPosition().getId(),jpost.getCity().getId(), jpost.getStartDate(), jpost.getEndDate())!=null) {
			throw new Exception("Job Posting already exists");
		}else if(userService.findById(jpost.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(jobPositionService.findById(jpost.getJobPosition().getId())==null || jobPositionService.findById(jpost.getJobPosition().getId()).isActiveState()==false) {
			throw new Exception("Job position doesn't exist");
		}else if(cityService.findById(jpost.getCity().getId())==null || cityService.findById(jpost.getCity().getId()).isActiveState()==false) {
			throw new Exception("City doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobPosting jpost) throws Exception{
		if(!jpost.getUser().equals(jobPostingService.findById(jpost.getId()).getUser())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getJobPosition().equals(jobPostingService.findById(jpost.getId()).getJobPosition())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getCity().equals(jobPostingService.findById(jpost.getId()).getCity())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getStartDate().equals(jobPostingService.findById(jpost.getId()).getStartDate())) {
			throw new Exception("BK cannot change");
		}else if(!jpost.getEndDate().equals(jobPostingService.findById(jpost.getId()).getEndDate())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobPosting jpost) throws Exception {
		if(jpost.getJobTitleName() == null) {
			throw new Exception("Job Posting name must be filled");
		}else if(jpost.getAddress()==null) {
			throw new Exception("Address must be filled");
		}
		return null;
	}
}

