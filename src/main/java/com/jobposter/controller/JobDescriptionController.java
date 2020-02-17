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

import com.jobposter.entity.JobDescription;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobDescriptionService;
import com.jobposter.service.JobPostingService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobDescriptionController {
	
	@Autowired
	private JobDescriptionService jobDescriptionService;
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@PostMapping("/job-description")
	public ResponseEntity<?> insert(@RequestBody List<JobDescription> jdescs) throws ErrorException{
		try {
			for(JobDescription jdesc : jdescs) {
				jdesc.setJobDescriptionCode("jdesc"+jdesc.getJobPosting().getId());
				valIdNull(jdesc);
				valBkNotNull(jdesc);
				valBkNotExist(jdesc);
				valNonBk(jdesc);
				jobDescriptionService.insert(jdesc);
				jdesc.getJobPosting().setUser(null);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(jdescs);
	}
	
	@PutMapping("/job-description")
	public ResponseEntity<?> update(@RequestBody List<JobDescription> jdescs) throws ErrorException{
		try {
			for(JobDescription jdesc : jdescs) {
				valIdNotNull(jdesc);
				valIdExist(jdesc.getId());
				valBkNotNull(jdesc);
				valBkNotChange(jdesc);
				valNonBk(jdesc);
				jobDescriptionService.update(jdesc);
				jdesc.getJobPosting().setUser(null);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(jdescs);
	}
	
	@DeleteMapping("/job-description/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobDescription jdesc = jobDescriptionService.findById(id);
			jobDescriptionService.delete(jdesc);
			jdesc.getJobPosting().setUser(null);
			return ResponseEntity.ok(jdesc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/job-description/by-job/{id}")
	public ResponseEntity<?> deleteByJob(@PathVariable String id) throws ErrorException {
		try {
			jobDescriptionService.deleteByJob(id);
			Object obj = new Object();
			obj = "Berhasil delete description";
			return ResponseEntity.ok(obj);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/job-description/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobDescription jdesc = jobDescriptionService.findById(id);
			jdesc.getJobPosting().getUser().setImage(null);
			return ResponseEntity.ok(jdesc);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-description")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(jobDescriptionService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-description/list/{id}")
	public ResponseEntity<?> getDescriptionByJobPosting(@PathVariable String id) throws ErrorException {
		try {
			List<JobDescription> listJobDescription = jobDescriptionService.findDescriptionByJobPosting(id);
			for(JobDescription jd : listJobDescription) {
				jd.getJobPosting().setUser(null);
			}
			return ResponseEntity.ok(listJobDescription);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	private Exception valIdNull(JobDescription jdesc) throws Exception {
		if(jdesc.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobDescription jdesc) throws Exception{
		if(jdesc.getId()==null) {
			throw new Exception("Job Description doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobDescriptionService.findById(id)==null) {
			throw new Exception("Job Description doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobDescription jdesc) throws Exception{
		if(jdesc.getJobDescriptionCode()==null) {
			throw new Exception("Job Description code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobDescription jdesc) throws Exception{
		if(jobPostingService.findById(jdesc.getJobPosting().getId())== null || jobPostingService.findById(jdesc.getJobPosting().getId()).isActiveState()==false) {
			throw new Exception("Job Posting doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobDescription jdesc) throws Exception{
		if(!jdesc.getJobDescriptionCode().equalsIgnoreCase(jobDescriptionService.findById(jdesc.getId()).getJobDescriptionCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobDescription jdesc) throws Exception {
		if(jdesc.getJobDescriptionName() == null) {
			throw new Exception("Job Description name must be filled");
		}
		return null;
	}
}
