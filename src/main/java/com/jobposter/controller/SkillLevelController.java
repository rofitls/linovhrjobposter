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

import com.jobposter.entity.SkillLevel;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.SkillLevelService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class SkillLevelController {
	
	@Autowired
	private SkillLevelService skillLevelService;
	
	@PostMapping("/skill-level")
	public ResponseEntity<?> insert(@RequestBody SkillLevel sl) throws ErrorException{
		try {
			valIdNull(sl);
			valBkNotNull(sl);
			valBkNotExist(sl);
			valNonBk(sl);
			skillLevelService.insert(sl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/skill-level")
	public ResponseEntity<?> update(@RequestBody SkillLevel sl) throws ErrorException{
		try {
			valIdNotNull(sl);
			valIdExist(sl.getId());
			valBkNotNull(sl);
			valBkNotChange(sl);
			valNonBk(sl);
			skillLevelService.update(sl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/skill-level/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			SkillLevel sl = skillLevelService.findById(id);
			sl.setActiveState(false);
			skillLevelService.update(sl);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/skill-level/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(skillLevelService.findById(id));
	}
	
	@GetMapping("/skill-level")
	public ResponseEntity<?> getAll()  throws ErrorException {
		return ResponseEntity.ok(skillLevelService.findAll());
	}
	
	private Exception valIdNull(SkillLevel sl) throws Exception {
		if(sl.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(SkillLevel sl) throws Exception{
		if(sl.getId()==null) {
			throw new Exception("Skill Level doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(skillLevelService.findById(id)==null) {
			throw new Exception("Skill Level doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(SkillLevel sl) throws Exception{
		if(sl.getSkillLevelCode()==null) {
			throw new Exception("Skill Level code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (SkillLevel sl) throws Exception{
		if(skillLevelService.findByBk(sl.getSkillLevelCode())!=null) {
			throw new Exception("Skill Level already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(SkillLevel sl) throws Exception{
		if(!sl.getSkillLevelCode().equalsIgnoreCase(skillLevelService.findById(sl.getId()).getSkillLevelCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(SkillLevel sl) throws Exception {
		if(sl.getSkillLevelName() == null) {
			throw new Exception("Skill Level name must be filled");
		}
		return null;
	}
}

