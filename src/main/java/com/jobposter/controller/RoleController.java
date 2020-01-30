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

import com.jobposter.entity.Role;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.RoleService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class RoleController {
	
	@Autowired
	private RoleService roleService;
	
	@PostMapping("/role")
	public ResponseEntity<?> insert(@RequestBody Role role) throws ErrorException{
		try {
			valIdNull(role);
			valBkNotNull(role);
			valBkNotExist(role);
			valNonBk(role);
			role.setActiveState(true);
			roleService.insert(role);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(role);
	}
	
	@PutMapping("/role")
	public ResponseEntity<?> update(@RequestBody Role role) throws ErrorException{
		try {
			valIdNotNull(role);
			valIdExist(role.getId());
			valBkNotNull(role);
			valBkNotChange(role);
			valNonBk(role);
			roleService.update(role);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(role);
	}
	
	@PutMapping("/role/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException{
		try {
			valIdExist(id);
			Role role = roleService.findById(id);
			role.setActiveState(false);
			roleService.update(role);
			return ResponseEntity.ok(role);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/role/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			return ResponseEntity.ok(roleService.findById(id));
		}catch(Exception e ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}	
	}
	
	@GetMapping("/role")
	public ResponseEntity<?> getAll()  throws ErrorException {
		try {
			return ResponseEntity.ok(roleService.findAll());	
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/role/name/{name}")
	public ResponseEntity<?> getByName(@PathVariable String name) throws ErrorException {
		try {
			return ResponseEntity.ok(roleService.findByName(name));
		}catch(Exception e ) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}	
	}
	
	private Exception valIdNull(Role role) throws Exception {
		if(role.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Role role) throws Exception{
		if(role.getId()==null) {
			throw new Exception("Role doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(roleService.findById(id)==null) {
			throw new Exception("Role does'nt exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Role role) throws Exception{
		if(role.getRoleCode()==null) {
			throw new Exception("Role code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Role role) throws Exception{
		if(roleService.findByBk(role.getRoleCode())!=null) {
			throw new Exception("Role already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(Role role) throws Exception{
		if(!role.getRoleCode().equalsIgnoreCase(roleService.findById(role.getId()).getRoleCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(Role role) throws Exception {
		if(role.getRoleName() == null) {
			throw new Exception("Role name must be filled");
		}
		return null;
	}
}