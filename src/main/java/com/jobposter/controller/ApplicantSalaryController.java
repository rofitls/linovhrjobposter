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

import com.jobposter.entity.ApplicantSalary;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicantSalaryService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class ApplicantSalaryController {
	
	@Autowired
	private ApplicantSalaryService applService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/apl-salary")
	public ResponseEntity<?> insert(@RequestBody ApplicantSalary appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			valNonBk(appl);
			applService.insert(appl);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(appl);
	}
	
	@PutMapping("/apl-salary")
	public ResponseEntity<?> update(@RequestBody ApplicantSalary appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			valNonBk(appl);
			applService.update(appl);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(appl);
	}
	
	@DeleteMapping("/apl-salary/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicantSalary appl = applService.findById(id);
			applService.delete(appl);
			appl.setUser(null);
			return ResponseEntity.ok(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl-salary/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		ApplicantSalary applSalary = applService.findById(id);
		applSalary.setUser(null);
		return ResponseEntity.ok(applSalary);
	}
	
	@GetMapping("/apl-salary/{id}")
	public ResponseEntity<?> getSalaryByApplicant(@PathVariable String id) throws ErrorException {
		ApplicantSalary applSalary = applService.findByBk(id);
		applSalary.setUser(null);
		return ResponseEntity.ok(applSalary);
	}
	
	@GetMapping("/apl-salary")
	public ResponseEntity<?> getAll() throws ErrorException {
		return ResponseEntity.ok(applService.findAll());
	}
	

	private Exception valIdNull(ApplicantSalary appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicantSalary appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Applicant salary doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Applicant salary doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicantSalary appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicantSalary appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId())!=null) {
			throw new Exception("Applicant salary already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicantSalary appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(ApplicantSalary appl) throws Exception {
		if(appl.getSalaryExpected() == null) {
			throw new Exception("Salary expected must be filled");
		}
		return null;
	}
}
