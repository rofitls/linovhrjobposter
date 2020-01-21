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

import com.jobposter.entity.JobCategory;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobCategoryService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobCategoryController {
	
	@Autowired
	private JobCategoryService jobCategoryService;
	
	@PostMapping("/job-category")
	public ResponseEntity<?> insert(@RequestBody JobCategory jc) throws ErrorException{
		try {
			valIdNull(jc);
			valBkNotNull(jc);
			valBkNotExist(jc);
			valNonBk(jc);
			jobCategoryService.insert(jc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/job-category")
	public ResponseEntity<?> update(@RequestBody JobCategory jc) throws ErrorException{
		try {
			valIdNotNull(jc);
			valIdExist(jc.getId());
			valBkNotNull(jc);
			valBkNotChange(jc);
			valNonBk(jc);
			jobCategoryService.update(jc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/job-category/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobCategory jc = jobCategoryService.findById(id);
			jc.setActiveState(false);
			jobCategoryService.update(jc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/job-category/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(jobCategoryService.findById(id));
	}
	
	@GetMapping("/job-category")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(jobCategoryService.findAll());
	}
	private Exception valIdNull(JobCategory jc) throws Exception {
		if(jc.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobCategory jc) throws Exception{
		if(jc.getId()==null) {
			throw new Exception("Job Category doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobCategoryService.findById(id)==null) {
			throw new Exception("Job Category doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobCategory jc) throws Exception{
		if(jc.getJobCategoryCode()==null) {
			throw new Exception("Job Category code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobCategory jc) throws Exception{
		if(jobCategoryService.findByBk(jc.getJobCategoryCode())!=null) {
			throw new Exception("Job Category already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobCategory jc) throws Exception{
		if(!jc.getJobCategoryCode().equalsIgnoreCase(jobCategoryService.findById(jc.getId()).getJobCategoryCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobCategory jc) throws Exception {
		if(jc.getJobCategoryName() == null) {
			throw new Exception("Job Category name must be filled");
		}
		return null;
	}
}


