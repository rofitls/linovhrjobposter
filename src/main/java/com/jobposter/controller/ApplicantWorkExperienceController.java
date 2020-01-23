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

import com.jobposter.entity.ApplicantWorkExperience;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicantWorkExperienceService;
import com.jobposter.service.JobCategoryService;
import com.jobposter.service.JobLevelService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class ApplicantWorkExperienceController {
	
	@Autowired
	private ApplicantWorkExperienceService applService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobLevelService jobLevelService;
	
	@Autowired
	private JobCategoryService jobCategoryService;
	
	@PostMapping("/apl-work-exp")
	public ResponseEntity<?> insert(@RequestBody ApplicantWorkExperience appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			valNonBk(appl);
			applService.insert(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/apl-work-exp")
	public ResponseEntity<?> update(@RequestBody ApplicantWorkExperience appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			valNonBk(appl);
			applService.update(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@DeleteMapping("/apl-work-exp/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			applService.delete(id);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/apl-work-exp/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(applService.findById(id));
	}
	
	@GetMapping("/apl-work-exp")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(applService.findAll());
	}
	
	@GetMapping("/apl-work-exp/list/{id}")
	public ResponseEntity<?> getApplicantWorkExperienceByApplicant(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(applService.findAWEUser(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}

	private Exception valIdNull(ApplicantWorkExperience appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicantWorkExperience appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Applicant work experience doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Applicant work experience doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicantWorkExperience appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getCompany()==null) {
			throw new Exception("Company must be filled");
		}else if(appl.getJobCategory()==null) {
			throw new Exception("Job Category must be filled");
		}else if(appl.getJobLevel()==null) {
			throw new Exception("Job Level must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicantWorkExperience appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(),appl.getCompany(), appl.getJobLevel().getId(), appl.getJobCategory().getId())!=null) {
			throw new Exception("Applicant work experience already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(jobLevelService.findById(appl.getJobLevel().getId())==null || jobLevelService.findById(appl.getJobLevel().getId()).isActiveState()==false) {
			throw new Exception("Job Level doesn't exist");
		}else if(jobCategoryService.findById(appl.getJobCategory().getId())==null || jobCategoryService.findById(appl.getJobCategory().getId()).isActiveState()==false) {
			throw new Exception("Job Category doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicantWorkExperience appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getCompany().equalsIgnoreCase(applService.findById(appl.getId()).getCompany())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getJobCategory().getId().equalsIgnoreCase(jobCategoryService.findById(appl.getJobCategory().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getJobLevel().getId().equalsIgnoreCase(jobLevelService.findById(appl.getJobLevel().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(ApplicantWorkExperience appl) throws Exception {
		if(appl.getJobTittle() == null) {
			throw new Exception("Job Tittle must be filled");
		}else if(appl.getSalary() == null) {
			throw new Exception("Salary must be filled");
		}else if(appl.getStartDate() == null) {
			throw new Exception("Start date must be filled");
		}else if(appl.getEndDate() == null) {
			throw new Exception("End date must be filled");
		}else if(appl.getDescription() == null) {
			throw new Exception("Description must be filled");
		}
		return null;
	}
}


