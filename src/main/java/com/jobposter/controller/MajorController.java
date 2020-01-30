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

import com.jobposter.entity.Major;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.EducationLevelService;
import com.jobposter.service.MajorService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class MajorController {
	
	@Autowired
	private MajorService majorService;
	
	@Autowired
	private EducationLevelService eduLevelService;
	
	@PostMapping("/major")
	public ResponseEntity<?> insert(@RequestBody Major major) throws ErrorException{
		try {
			valIdNull(major);
			valBkNotNull(major);
			valBkNotExist(major);
			valNonBk(major);
			major.setActiveState(true);
			majorService.insert(major);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(major);
	}
	
	@PutMapping("/major")
	public ResponseEntity<?> update(@RequestBody Major major) throws ErrorException{
		try {
			valIdNotNull(major);
			valIdExist(major.getId());
			valBkNotNull(major);
			valBkNotChange(major);
			valNonBk(major);
			major.setActiveState(false);
			majorService.update(major);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(major);
	}
	
	@PutMapping("/major/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Major major = majorService.findById(id);
			major.setActiveState(false);
			majorService.update(major);
			return ResponseEntity.ok(major);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/major/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			return ResponseEntity.ok(majorService.findById(id));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/major")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(majorService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/major/edu-level/{id}")
	public ResponseEntity<?> getMajorByEduLevel(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.ok(majorService.findByEduLevel(id));	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	private Exception valIdNull(Major major) throws Exception {
		if(major.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Major major) throws Exception{
		if(major.getId()==null) {
			throw new Exception("Major doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(majorService.findById(id)==null) {
			throw new Exception("Major already exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Major major) throws Exception{
		if(major.getMajorCode()==null) {
			throw new Exception("Major code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Major major) throws Exception{
		if(majorService.findByBk(major.getMajorCode())!=null) {
			throw new Exception("Major already exists");
		}else if(eduLevelService.findById(major.getEduLevel().getId())== null || eduLevelService.findById(major.getEduLevel().getId()).isActiveState()==false) {
			throw new Exception("Education Level doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(Major major) throws Exception{
		if(!major.getMajorCode().equalsIgnoreCase(majorService.findById(major.getId()).getMajorCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(Major major) throws Exception {
		if(major.getMajorName() == null) {
			throw new Exception("Major name must be filled");
		}
		return null;
	}
}
