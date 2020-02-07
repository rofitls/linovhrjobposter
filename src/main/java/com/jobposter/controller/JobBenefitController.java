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

import com.jobposter.entity.JobBenefit;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.JobBenefitService;
import com.jobposter.service.JobPostingService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class JobBenefitController {
	
	@Autowired
	private JobBenefitService jobBenefitService;
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@PostMapping("/job-benefit")
	public ResponseEntity<?> insert(@RequestBody List<JobBenefit> jBenefits) throws ErrorException{
		try {
			for(JobBenefit jBenefit : jBenefits) {
				jBenefit.setJobBenefitCode("jdesc"+jBenefit.getJobPosting().getId());
				valIdNull(jBenefit);
				valBkNotNull(jBenefit);
				valBkNotExist(jBenefit);
				valNonBk(jBenefit);
				jobBenefitService.insert(jBenefit);
				jBenefit.getJobPosting().setUser(null);
			}
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(jBenefits);
	}
	
	@PutMapping("/job-benefit")
	public ResponseEntity<?> update(@RequestBody List<JobBenefit> jBenefits) throws ErrorException{
		try {
			for(JobBenefit jBenefit : jBenefits) {
				valIdNotNull(jBenefit);
				valIdExist(jBenefit.getId());
				valBkNotNull(jBenefit);
				valBkNotChange(jBenefit);
				valNonBk(jBenefit);
				jobBenefitService.update(jBenefit);
				jBenefit.getJobPosting().setUser(null);
			}
			
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(jBenefits);
	}
	
	@DeleteMapping("/job-benefit/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobBenefit jBenefits = jobBenefitService.findById(id);
			jobBenefitService.delete(jBenefits);
			jBenefits.getJobPosting().setUser(null);
			return ResponseEntity.ok(jBenefits);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@DeleteMapping("/job-benefit/by-job/{id}")
	public ResponseEntity<?> deleteByJob(@PathVariable String id) throws ErrorException {
		try {
			jobBenefitService.deleteByJob(id);
			Object obj = new Object();
			return ResponseEntity.ok(obj);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/job-benefit/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			JobBenefit jBenefits = jobBenefitService.findById(id);
			jBenefits.getJobPosting().getUser().setImage(null);
			return ResponseEntity.ok(jBenefits);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-benefit")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(jobBenefitService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/job-benefit/list/{id}")
	public ResponseEntity<?> getDescriptionByJobPosting(@PathVariable String id) throws ErrorException {
		try {
			List<JobBenefit> listJobBenefit = jobBenefitService.findBenefitByJobPosting(id);
			for(JobBenefit jd : listJobBenefit) {
				jd.getJobPosting().setUser(null);
			}
			return ResponseEntity.ok(listJobBenefit);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	private Exception valIdNull(JobBenefit jBenefit) throws Exception {
		if(jBenefit.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(JobBenefit jBenefit) throws Exception{
		if(jBenefit.getId()==null) {
			throw new Exception("Job Benefit doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(jobBenefitService.findById(id)==null) {
			throw new Exception("Job Benefit doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(JobBenefit jBenefit) throws Exception{
		if(jBenefit.getJobBenefitCode()==null) {
			throw new Exception("Job Benefit code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (JobBenefit jBenefit) throws Exception{
		if(jobPostingService.findById(jBenefit.getJobPosting().getId())== null || jobPostingService.findById(jBenefit.getJobPosting().getId()).isActiveState()==false) {
			throw new Exception("Job Posting doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(JobBenefit jBenefit) throws Exception{
		if(!jBenefit.getJobBenefitCode().equalsIgnoreCase(jobBenefitService.findById(jBenefit.getId()).getJobBenefitCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(JobBenefit jBenefit) throws Exception {
		if(jBenefit.getJobBenefitName() == null) {
			throw new Exception("Job Benefit name must be filled");
		}
		return null;
	}

}
