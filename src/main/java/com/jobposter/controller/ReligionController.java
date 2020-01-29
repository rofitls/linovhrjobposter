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

import com.jobposter.entity.Religion;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ReligionService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class ReligionController {
	
	@Autowired
	private ReligionService religionService;
	
	@PostMapping("/religion")
	public ResponseEntity<?> insert(@RequestBody Religion religion) throws ErrorException{
		try {
			valIdNull(religion);
			valBkNotNull(religion);
			valBkNotExist(religion);
			valNonBk(religion);
			religion.setActiveState(true);
			religionService.insert(religion);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(religion);
	}
	
	@PutMapping("/religion")
	public ResponseEntity<?> update(@RequestBody Religion religion) throws ErrorException{
		try {
			valIdNotNull(religion);
			valIdExist(religion.getId());
			valBkNotNull(religion);
			valBkNotChange(religion);
			valNonBk(religion);
			religion.setActiveState(false);
			religionService.update(religion);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(religion);
	}
	
	@PutMapping("/religion/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Religion religion = religionService.findById(id);
			religion.setActiveState(false);
			religionService.update(religion);
			return ResponseEntity.ok(religion);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/religion/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(religionService.findById(id));
	}
	
	@GetMapping("/religion")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(religionService.findAll());
	}
	
	private Exception valIdNull(Religion religion) throws Exception {
		if(religion.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Religion religion) throws Exception{
		if(religion.getId()==null) {
			throw new Exception("Religion doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(religionService.findById(id)==null) {
			throw new Exception("Religion already exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Religion religion) throws Exception{
		if(religion.getReligionCode()==null) {
			throw new Exception("Religion code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Religion religion) throws Exception{
		if(religionService.findByBk(religion.getReligionCode())!=null) {
			throw new Exception("Religion already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(Religion religion) throws Exception{
		if(!religion.getReligionCode().equalsIgnoreCase(religionService.findById(religion.getId()).getReligionCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(Religion religion) throws Exception {
		if(religion.getReligionName() == null) {
			throw new Exception("Religion name must be filled");
		}
		return null;
	}
}
