package com.jobposter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jobposter.entity.ApplicationState;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicationStateService;

@RestController
@RequestMapping("/admin")
public class ApplicationStateController {
	
	@Autowired
	private ApplicationStateService stateService;
	
	@PostMapping("/application-state")
	public ResponseEntity<?> insert(@RequestBody ApplicationState state) throws ErrorException{
		try {
			valIdNull(state);
			valBkNotNull(state);
			valBkNotExist(state);
			valNonBk(state);
			stateService.insert(state);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/application-state")
	public ResponseEntity<?> update(@RequestBody ApplicationState state) throws ErrorException{
		try {
			valIdNotNull(state);
			valIdExist(state.getId());
			valBkNotNull(state);
			valBkNotChange(state);
			valNonBk(state);
			stateService.update(state);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/application-state/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicationState state = stateService.findById(id);
			state.setActiveState(false);
			stateService.update(state);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/application-state/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(stateService.findById(id));
	}
	
	@GetMapping("/application-state")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(stateService.findAll());
	}
	

	private Exception valIdNull(ApplicationState state) throws Exception {
		if(state.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicationState state) throws Exception{
		if(state.getId()==null) {
			throw new Exception("State doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(stateService.findById(id)==null) {
			throw new Exception("State doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicationState state) throws Exception{
		if(state.getStateCode()==null) {
			throw new Exception("State code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicationState state) throws Exception{
		if(stateService.findByBk(state.getStateCode())!=null) {
			throw new Exception("State already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicationState state) throws Exception{
		if(!state.getStateCode().equalsIgnoreCase(stateService.findById(state.getId()).getStateCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(ApplicationState state) throws Exception {
		if(state.getStateName() == null) {
			throw new Exception("State name must be filled");
		}
		return null;
	}
}



