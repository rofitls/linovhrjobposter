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

import com.jobposter.entity.MaritalStatus;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.MaritalStatusService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class MaritalStatusController {
	
	@Autowired
	private MaritalStatusService maritalStatusService;
	
	@PostMapping("/marital-status")
	public ResponseEntity<?> insert(@RequestBody MaritalStatus ms) throws ErrorException{
		try {
			valIdNull(ms);
			valBkNotNull(ms);
			valBkNotExist(ms);
			valNonBk(ms);
			ms.setActiveState(true);
			maritalStatusService.insert(ms);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(ms);
	}
	
	@PutMapping("/marital-status")
	public ResponseEntity<?> update(@RequestBody MaritalStatus ms) throws ErrorException{
		try {
			valIdNotNull(ms);
			valIdExist(ms.getId());
			valBkNotNull(ms);
			valBkNotChange(ms);
			valNonBk(ms);
			ms.setActiveState(false);
			maritalStatusService.update(ms);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(ms);
	}
	
	@PutMapping("/marital-status/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			MaritalStatus ms = maritalStatusService.findById(id);
			ms.setActiveState(false);
			maritalStatusService.update(ms);
			return ResponseEntity.ok(ms);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/marital-status/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(maritalStatusService.findById(id));
	}
	
	@GetMapping("/marital-status")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(maritalStatusService.findAll());
	}
	
	private Exception valIdNull(MaritalStatus ms) throws Exception {
		if(ms.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(MaritalStatus ms) throws Exception{
		if(ms.getId()==null) {
			throw new Exception("Marital status doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(maritalStatusService.findById(id)==null) {
			throw new Exception("Marital status already exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(MaritalStatus ms) throws Exception{
		if(ms.getMaritalStatusCode()==null) {
			throw new Exception("Marital status code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (MaritalStatus ms) throws Exception{
		if(maritalStatusService.findByBk(ms.getMaritalStatusCode())!=null) {
			throw new Exception("Marital status already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(MaritalStatus ms) throws Exception{
		if(!ms.getMaritalStatusCode().equalsIgnoreCase(maritalStatusService.findById(ms.getId()).getMaritalStatusCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(MaritalStatus ms) throws Exception {
		if(ms.getMaritalStatusName() == null) {
			throw new Exception("Marital status name must be filled");
		}
		return null;
	}
}
