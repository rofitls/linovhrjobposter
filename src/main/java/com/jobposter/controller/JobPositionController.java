package com.jobposter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobposter.entity.JobPosition;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobCategoryService;
import com.jobposter.service.JobPositionService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobPositionController {
	
	@Autowired
	private JobPositionService jobPositionService;
	
	@Autowired
	private JobCategoryService jobCategoryService;
	
	@PostMapping("/job-position")
	public ResponseEntity<?> insert(@RequestBody JobPosition jp) throws ErrorException{
		try {
			valIdNull(jp);
			valBkNotNull(jp);
			valBkNotExist(jp);
			valNonBk(jp);
			jobPositionService.insert(jp);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/job-position")
	public ResponseEntity<?> update(@RequestBody JobPosition jp) throws ErrorException{
		try {
			valIdNotNull(jp);
			valIdExist(jp.getId());
			valBkNotNull(jp);
			valBkNotChange(jp);
			valNonBk(jp);
			jobPositionService.update(jp);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/job-position/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobPosition jp = jobPositionService.findById(id);
			jp.setActiveState(false);
			jobPositionService.update(jp);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/job-position/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(jobPositionService.findById(id));
	}
	
	@GetMapping("/job-position")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(jobPositionService.findAll());
	}
	
	private Exception valIdNull(JobPosition jp) throws Exception {
		if(jp.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobPosition jp) throws Exception{
		if(jp.getId()==null) {
			throw new Exception("Job Position doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobPositionService.findById(id)==null) {
			throw new Exception("Job Position doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobPosition jp) throws Exception{
		if(jp.getJobPositionCode()==null) {
			throw new Exception("Job Position code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobPosition jp) throws Exception{
		if(jobPositionService.findByBk(jp.getJobPositionCode())!=null) {
			throw new Exception("Job Position already exists");
		}else if(jobCategoryService.findById(jp.getJobCategory().getId())== null || jobCategoryService.findById(jp.getJobCategory().getId()).isActiveState()==false) {
			throw new Exception("Job Category doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobPosition jp) throws Exception{
		if(!jp.getJobPositionCode().equalsIgnoreCase(jobPositionService.findById(jp.getId()).getJobPositionCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobPosition jp) throws Exception {
		if(jp.getJobPositionName() == null) {
			throw new Exception("Job Position name must be filled");
		}
		return null;
	}
}
