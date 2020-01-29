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

import com.jobposter.entity.ApplicantEducation;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicantEducationService;
import com.jobposter.service.EducationLevelService;
import com.jobposter.service.MajorService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class ApplicantEducationController {
	
	@Autowired
	private ApplicantEducationService applService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MajorService majorService;
	
	@PostMapping("/apl-edu")
	public ResponseEntity<?> insert(@RequestBody ApplicantEducation appl) throws ErrorException{
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
	
	@PutMapping("/apl-edu")
	public ResponseEntity<?> update(@RequestBody ApplicantEducation appl) throws ErrorException{
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
	
	@DeleteMapping("/apl-edu/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicantEducation appl = applService.findById(id);
			applService.delete(appl);
			appl.setUser(null);
			return ResponseEntity.ok(appl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/apl-edu/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		ApplicantEducation applEdu = applService.findById(id);
		applEdu.setUser(null);
		return ResponseEntity.ok(applEdu);
	}
	
	@GetMapping("/apl-edu")
	public ResponseEntity<?> getAll() throws ErrorException {
		return ResponseEntity.ok(applService.findAll());
	}
	
	@GetMapping("/apl-edu/list/{id}")
	public ResponseEntity<?> getApplicantEduByApplicant(@PathVariable String id) throws ErrorException {
		try {
			List<ApplicantEducation> listApplEdu = applService.findAEUser(id);
			for(ApplicantEducation ae : listApplEdu) {
				ae.setUser(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(listApplEdu);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(ApplicantEducation appl) throws Exception {
		if(appl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicantEducation appl) throws Exception{
		if(appl.getId()==null) {
			throw new Exception("Applicant education doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(applService.findById(id)==null) {
			throw new Exception("Applicant education doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicantEducation appl) throws Exception{
		if(appl.getUser()==null) {
			throw new Exception("User must be filled");
		}else if(appl.getSchool()==null) {
			throw new Exception("School must be filled");
		}else if(appl.getMajor()==null) {
			throw new Exception("Major must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicantEducation appl) throws Exception{
		if(applService.findByBk(appl.getUser().getId(),appl.getMajor().getId(), appl.getSchool())!=null) {
			throw new Exception("Applicant education already exists");
		}else if(userService.findById(appl.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}else if(majorService.findById(appl.getMajor().getId())==null || majorService.findById(appl.getMajor().getId()).isActiveState()==false) {
			throw new Exception("Major doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicantEducation appl) throws Exception{
		if(!appl.getUser().getId().equalsIgnoreCase(userService.findById(appl.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getSchool().equalsIgnoreCase(applService.findById(appl.getId()).getSchool())) {
			throw new Exception("BK cannot change");
		}else if(!appl.getMajor().getId().equalsIgnoreCase(majorService.findById(appl.getMajor().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(ApplicantEducation appl) throws Exception {
		if(appl.getGpa() == null) {
			throw new Exception("GPA must be filled");
		}else if(appl.getStartDate() == null) {
			throw new Exception("Start date must be filled");
		}else if(appl.getEndDate() == null) {
			throw new Exception("End date must be filled");
		}
		return null;
	}
}
