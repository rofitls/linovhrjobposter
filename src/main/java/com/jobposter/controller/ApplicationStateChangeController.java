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

import com.jobposter.entity.ApplicationStateChange;
import com.jobposter.entity.ReportMasterPojo;
import com.jobposter.entity.Users;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ApplicationService;
import com.jobposter.service.ApplicationStateChangeService;
import com.jobposter.service.ApplicationStateService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class ApplicationStateChangeController {
	
	@Autowired
	private ApplicationStateChangeService appStateChangeService;
	
	@Autowired
	private ApplicationService applicationService;
	
	@Autowired
	private ApplicationStateService appStateService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/application-state-change")
	public ResponseEntity<?> insert(@RequestBody ApplicationStateChange state) throws ErrorException{
		try {
			valIdNull(state);
			valBkNotNull(state);
			valBkNotExist(state);
			//valNonBk(state);
			appStateChangeService.insert(state);
			state.getApplication().setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(state);
	}
	
	@PutMapping("/application-state-change")
	public ResponseEntity<?> update(@RequestBody ApplicationStateChange state) throws ErrorException{
		try {
			valIdNotNull(state);
			valIdExist(state.getId());
			valBkNotNull(state);
			valBkNotChange(state);
			//valNonBk(state);
			appStateChangeService.update(state);
			state.getApplication().setUser(null);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(state);
	}
	
	@DeleteMapping("/application-state-change/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicationStateChange state = appStateChangeService.findById(id);
			appStateChangeService.delete(state);
			state.getApplication().setUser(null);
			return ResponseEntity.ok(state);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/application-state-change/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			ApplicationStateChange asc = appStateChangeService.findById(id);
			asc.getApplication().getUser().setImage(null);
			return ResponseEntity.ok(asc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/application-state-change")
	public ResponseEntity<?> getAll()  throws ErrorException{
		try {
			return ResponseEntity.ok(appStateChangeService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/application-state-change/report/{id}")
	public ResponseEntity<?> report(@PathVariable String id) throws ErrorException {
		try {
			Users user = userService.findById(id);
			List<ReportMasterPojo> rp = appStateChangeService.reportMaster(id);
			rp.get(0).setRecruiterName(user.getFirstName()+" "+user.getLastName());
			return ResponseEntity.ok(rp);	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/application-state-change/list-by-user/{id}")
	public ResponseEntity<?> listApplicationByUser(@PathVariable String id)throws ErrorException {
		try {
			return ResponseEntity.ok(appStateChangeService.listApplicationByUser(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/application-state-change/list-by-job/{id}")
	public ResponseEntity<?> listApplicationByJob(@PathVariable String id)throws ErrorException {
		try {
			return ResponseEntity.ok(appStateChangeService.listApplicationByJob(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/application-state-change/list-application-hire/{id}")
	public ResponseEntity<?> listApplicationHire(@PathVariable String id) throws ErrorException {
		try {
			return ResponseEntity.ok(appStateChangeService.findApplicationHireList(id));
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	private Exception valIdNull(ApplicationStateChange state) throws Exception {
		if(state.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(ApplicationStateChange state) throws Exception{
		if(state.getId()==null) {
			throw new Exception("Application doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(appStateChangeService.findById(id)==null) {
			throw new Exception("Application doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(ApplicationStateChange state) throws Exception{
		if(state.getApplication()==null) {
			throw new Exception("Application must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (ApplicationStateChange state) throws Exception{
		if(appStateChangeService.findByBk(state.getApplication().getId())!=null) {
			throw new Exception("Application already exists");
		}else if(appStateService.findById(state.getState().getId())==null || appStateService.findById(state.getState().getId()).isActiveState()==false) {
			throw new Exception("Application state doesn't exists");
		}else if(applicationService.findById(state.getApplication().getId())==null) {
			throw new Exception("Application doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(ApplicationStateChange state) throws Exception{
		if(!state.getApplication().getId().equalsIgnoreCase(applicationService.findById(state.getApplication().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
//	private Exception valNonBk(ApplicationStateChange state) throws Exception {
//		if(state.getStateName() == null) {
//			throw new Exception("State name must be filled");
//		}
//		return null;
//	}
}



