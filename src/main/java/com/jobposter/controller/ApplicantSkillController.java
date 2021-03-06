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

import com.jobposter.entity.ApplicantSkill;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicantSkillService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class ApplicantSkillController {
	
	@Autowired
	private ApplicantSkillService applService;
	
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/apl-skill")
	public ResponseEntity<?> insert(@RequestBody ApplicantSkill appl) throws ErrorException{
		try {
			valIdNull(appl);
			valBkNotNull(appl);
			valBkNotExist(appl);
			//valNonBk(appl);
			applService.insert(appl);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(appl);
	}
	
	@PutMapping("/apl-skill")
	public ResponseEntity<?> update(@RequestBody ApplicantSkill appl) throws ErrorException{
		try {
			valIdNotNull(appl);
			valIdExist(appl.getId());
			valBkNotNull(appl);
			valBkNotChange(appl);
			//valNonBk(appl);
			applService.update(appl);
			appl.setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(appl);
	}
	
	@DeleteMapping("/apl-skill/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicantSkill appl = applService.findById(id);
			applService.delete(appl);
			appl.setUser(null);
			return ResponseEntity.ok(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl-skill/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicantSkill applSkill = applService.findById(id);
			applSkill.getUser().setImage(null);
			return ResponseEntity.status(HttpStatus.OK).body(applSkill);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl-skill")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(applService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/apl-skill/list/{id}")
	public ResponseEntity<?> getApplicantSkillByApplicant(@PathVariable String id) throws ErrorException {
		try {
			List<ApplicantSkill> listApplSkill = applService.findASUser(id);
			for(ApplicantSkill as : listApplSkill) {
				as.setUser(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(listApplSkill);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}

	private Exception valIdNull(ApplicantSkill appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicantSkill appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Applicant skill doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Applicant skill doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicantSkill appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getSkillName()==null) {
			throw new Exception("Skill name be filled");
		}else if(appl.getSkillLevel()==null) {
			throw new Exception("Skill Level must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicantSkill appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(), appl.getSkillName())!=null) {
			throw new Exception("Applicant skill already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicantSkill appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getSkillName().equalsIgnoreCase(applService.findById(appl.getId()).getSkillName())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
//	private Exception valNonBk(ApplicantSkill appl) throws Exception {
//		return null;
//	}
}