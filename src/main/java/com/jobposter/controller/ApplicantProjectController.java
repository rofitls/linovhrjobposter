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

import com.jobposter.entity.ApplicantProject;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicantProjectService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class ApplicantProjectController {
	
	@Autowired
	private ApplicantProjectService applService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/apl-proj")
	public ResponseEntity<?> insert(@RequestBody ApplicantProject appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			valNonBk(appl);
			applService.insert(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(appl);
	}
	
	@PutMapping("/apl-proj")
	public ResponseEntity<?> update(@RequestBody ApplicantProject appl) throws ErrorException{
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
		return ResponseEntity.status(HttpStatus.OK).body(appl);
	}
	
	@DeleteMapping("/apl-proj/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicantProject appl = applService.findById(id);
			applService.delete(appl);
			return ResponseEntity.ok(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/apl-proj/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(applService.findById(id));
	}
	
	@GetMapping("/apl-proj")
	public ResponseEntity<?> getAll() throws ErrorException {
		return ResponseEntity.ok(applService.findAll());
	}
	
	@GetMapping("/apl-proj/list/{id}")
	public ResponseEntity<?> getApplicantProjectByApplicant(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.status(HttpStatus.OK).body(applService.findAPUser(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(ApplicantProject appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicantProject appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Applicant project doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Applicant project doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicantProject appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getProjectName()==null) {
			throw new Exception("Project Name must be filled");
		}else if(appl.getRole()==null) {
			throw new Exception("Role must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicantProject appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(),appl.getProjectName(), appl.getRole())!=null) {
			throw new Exception("Applicant education already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicantProject appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getProjectName().equalsIgnoreCase(applService.findById(appl.getId()).getProjectName())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getRole().equalsIgnoreCase(applService.findById(appl.getId()).getRole())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(ApplicantProject appl) throws Exception {
		if(appl.getDescription() == null) {
			throw new Exception("Description must be filled");
		}else if(appl.getStartDate() == null) {
			throw new Exception("Start date must be filled");
		}else if(appl.getEndDate() == null) {
			throw new Exception("End date must be filled");
		}
		return null;
	}
}
