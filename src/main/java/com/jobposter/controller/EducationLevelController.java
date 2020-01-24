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

import com.jobposter.entity.EducationLevel;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.EducationLevelService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class EducationLevelController {
	
	@Autowired
	private EducationLevelService educationLevelService;
	
	@PostMapping("/edu-level")
	public ResponseEntity<?> insert(@RequestBody EducationLevel el) throws ErrorException{
		try {
			valIdNull(el);
			valBkNotNull(el);
			valBkNotExist(el);
			valNonBk(el);
			el.setActiveState(true);
			educationLevelService.insert(el);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(el);
	}
	
	@PutMapping("/edu-level")
	public ResponseEntity<?> update(@RequestBody EducationLevel el) throws ErrorException{
		try {
			valIdNotNull(el);
			valIdExist(el.getId());
			valBkNotNull(el);
			valBkNotChange(el);
			valNonBk(el);
			educationLevelService.update(el);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(el);
	}
	
	@PutMapping("/edu-level/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			EducationLevel el = educationLevelService.findById(id);
			el.setActiveState(false);
			educationLevelService.update(el);
			return ResponseEntity.ok(el);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/edu-level/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(educationLevelService.findById(id));
	}
	
	@GetMapping("/edu-level")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(educationLevelService.findAll());
	}
	
	private Exception valIdNull(EducationLevel el) throws Exception {
		if(el.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(EducationLevel el) throws Exception{
		if(el.getId()==null) {
			throw new Exception("Education Level doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(educationLevelService.findById(id)==null) {
			throw new Exception("Education Level doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(EducationLevel el) throws Exception{
		if(el.getEducationLevelCode()==null) {
			throw new Exception("Education Level code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (EducationLevel el) throws Exception{
		if(educationLevelService.findByBk(el.getEducationLevelCode())!=null) {
			throw new Exception("Education Level already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(EducationLevel el) throws Exception{
		if(!el.getEducationLevelCode().equalsIgnoreCase(educationLevelService.findById(el.getId()).getEducationLevelCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(EducationLevel el) throws Exception {
		if(el.getEducationLevelName() == null) {
			throw new Exception("Education Level name must be filled");
		}
		return null;
	}
}

