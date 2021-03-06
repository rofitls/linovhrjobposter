package com.jobposter.controller;

import java.util.List;

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

import com.jobposter.entity.JobRequirement;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobPostingService;
import com.jobposter.service.JobRequirementService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobRequirementController {
	
	@Autowired
	private JobRequirementService jobRequirementService;
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@PostMapping("/job-requirement")
	public ResponseEntity<?> insert(@RequestBody List<JobRequirement> jreqs) throws ErrorException{
		try {
			for(JobRequirement jreq : jreqs) {
				jreq.setJobRequirementCode("jreq"+jreq.getJobPosting().getId());
				valIdNull(jreq);
				valBkNotNull(jreq);
				valBkNotExist(jreq);
				valNonBk(jreq);
				jobRequirementService.insert(jreq);
				jreq.getJobPosting().setUser(null);	
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(jreqs);
	}
	
	@PutMapping("/job-requirement")
	public ResponseEntity<?> update(@RequestBody List<JobRequirement> jreqs) throws ErrorException{
		try {
			for(JobRequirement jreq : jreqs) {
				valIdNotNull(jreq);
				valIdExist(jreq.getId());
				valBkNotNull(jreq);
				valBkNotChange(jreq);
				valNonBk(jreq);
				jobRequirementService.update(jreq);
				jreq.getJobPosting().setUser(null);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(jreqs);
	}
	
	@DeleteMapping("/job-requirement/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobRequirement jreq = jobRequirementService.findById(id);
			jobRequirementService.delete(jreq);
			jreq.getJobPosting().setUser(null);
			return ResponseEntity.ok(jreq);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/job-requirement/by-job/{id}")
	public ResponseEntity<?> deleteByJob(@PathVariable String id) throws ErrorException {
		try {
			jobRequirementService.deleteByJob(id);
			return ResponseEntity.ok(jobPostingService.findById(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/job-requirement/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobRequirement jreq = jobRequirementService.findById(id);
			jreq.getJobPosting().getUser().setImage(null);
			return ResponseEntity.ok(jreq);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-requirement")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(jobRequirementService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-requirement/list/{id}")
	public ResponseEntity<?> getRequirementByJobPosting(@PathVariable String id) throws ErrorException {
		try {
			List<JobRequirement> listJobRequirement = jobRequirementService.findRequirementByJobPosting(id);
			for(JobRequirement jr : listJobRequirement) {
				jr.getJobPosting().setUser(null);
			}
			return ResponseEntity.ok(listJobRequirement);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	private Exception valIdNull(JobRequirement jreq) throws Exception {
		if(jreq.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobRequirement jreq) throws Exception{
		if(jreq.getId()==null) {
			throw new Exception("Job Requirement doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobRequirementService.findById(id)==null) {
			throw new Exception("Job Requirement doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobRequirement jreq) throws Exception{
		if(jreq.getJobRequirementCode()==null) {
			throw new Exception("Job Requirement code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobRequirement jreq) throws Exception{
		if(jobPostingService.findById(jreq.getJobPosting().getId())== null || jobPostingService.findById(jreq.getJobPosting().getId()).isActiveState()==false) {
			throw new Exception("Job Posting doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobRequirement jreq) throws Exception{
		if(!jreq.getJobRequirementCode().equalsIgnoreCase(jobRequirementService.findById(jreq.getId()).getJobRequirementCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobRequirement jreq) throws Exception {
		if(jreq.getJobRequirementName() == null) {
			throw new Exception("Job Requirement name must be filled");
		}
		return null;
	}
}

