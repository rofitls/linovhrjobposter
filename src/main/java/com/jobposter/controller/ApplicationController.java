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

import com.jobposter.entity.Application;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicationService;
import com.jobposter.service.DocumentService;
import com.jobposter.service.JobPostingService;
import com.jobposter.service.UserService;
import com.jobposter.service.ApplicantEducationService;
import com.jobposter.service.ApplicantSkillService;
import com.jobposter.service.ApplicantWorkExperienceService;


@RestController
@RequestMapping("/admin")
public class ApplicationController {
	
	@Autowired
	private ApplicationService applService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JobPostingService jobPostingService;
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private ApplicantWorkExperienceService applWorkExpService;
	
	@Autowired
	private ApplicantEducationService applEduService;
	
	@Autowired
	private ApplicantSkillService applSkillService;
	
	@PostMapping("/application")
	public ResponseEntity<?> insert(@RequestBody Application appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			authenticateAppl(appl);
			//valNonBk(appl);
			applService.insert(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/application")
	public ResponseEntity<?> update(@RequestBody Application appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			//valNonBk(appl);
			applService.update(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@DeleteMapping("/application/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			applService.delete(id);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/application/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(applService.findById(id));
	}
	
	@GetMapping("/application")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(applService.findAll());
	}
	

	private Exception valIdNull(Application appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Application appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Application doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Application doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Application appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getJobPosting()==null) {
			throw new Exception("Job Posting must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Application appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(),appl.getJobPosting().getId())!=null) {
			throw new Exception("Application already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(jobPostingService.findById(appl.getJobPosting().getId())==null || jobPostingService.findById(appl.getJobPosting().getId()).isActiveState()==false) {
			throw new Exception("Job Posting doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(Application appl) throws Exception{
		if(!appl.getUser().equals(applService.findById(appl.getId()).getUser())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getJobPosting().equals(applService.findById(appl.getId()).getJobPosting())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception authenticateAppl(Application appl) throws Exception {
		if(applWorkExpService.findById(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form work experience terlebih dahulu");
		}else if(applEduService.findById(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form education terlebih dahulu");
		}else if(applSkillService.findById(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form skill terlebih dahulu");
		}else if(documentService.findById(appl.getUser().getId())==null) {
			throw new Exception("Harus mengisi form skill terlebih dahulu");
		}
		return null;
	}
	
//	private Exception valNonBk(Application appl) throws Exception {
//		return null;
//	}
}


