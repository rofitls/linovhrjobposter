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

import com.jobposter.entity.JobLevel;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobLevelService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobLevelController {
	
	@Autowired
	private JobLevelService jobLevelService;
	
	@PostMapping("/job-level")
	public ResponseEntity<?> insert(@RequestBody JobLevel jl) throws ErrorException{
		try {
			valIdNull(jl);
			valBkNotNull(jl);
			valBkNotExist(jl);
			valNonBk(jl);
			jl.setActiveState(true);
			jobLevelService.insert(jl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/job-level")
	public ResponseEntity<?> update(@RequestBody JobLevel jl) throws ErrorException{
		try {
			valIdNotNull(jl);
			valIdExist(jl.getId());
			valBkNotNull(jl);
			valBkNotChange(jl);
			valNonBk(jl);
			jobLevelService.update(jl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/job-level/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobLevel jl = jobLevelService.findById(id);
			jl.setActiveState(false);
			jobLevelService.update(jl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/job-level/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(jobLevelService.findById(id));
	}
	
	@GetMapping("/job-level")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(jobLevelService.findAll());
	}
	
	private Exception valIdNull(JobLevel jl) throws Exception {
		if(jl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobLevel jl) throws Exception{
		if(jl.getId()==null) {
			throw new Exception("Job Level doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobLevelService.findById(id)==null) {
			throw new Exception("Job Level doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobLevel jl) throws Exception{
		if(jl.getJobLevelCode()==null) {
			throw new Exception("Job Level code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobLevel jl) throws Exception{
		if(jobLevelService.findByBk(jl.getJobLevelCode())!=null) {
			throw new Exception("Job Level already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobLevel jl) throws Exception{
		if(!jl.getJobLevelCode().equalsIgnoreCase(jobLevelService.findById(jl.getId()).getJobLevelCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobLevel jl) throws Exception {
		if(jl.getJobLevelName() == null) {
			throw new Exception("Job Level name must be filled");
		}
		return null;
	}
}
