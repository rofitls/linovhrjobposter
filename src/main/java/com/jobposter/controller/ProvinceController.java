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

import com.jobposter.entity.Province;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.ProvinceService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class ProvinceController {
	
	@Autowired
	private ProvinceService provinceService;
	
	@PostMapping("/province")
	public ResponseEntity<?> insert(@RequestBody Province province) throws ErrorException{
		try {
			valIdNull(province);
			valBkNotNull(province);
			valBkNotExist(province);
			valNonBk(province);
			province.setActiveState(true);
			provinceService.insert(province);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(province);
	}
	
	@PutMapping("/province")
	public ResponseEntity<?> update(@RequestBody Province province) throws ErrorException{
		try {
			valIdNotNull(province);
			valIdExist(province.getId());
			valBkNotNull(province);
			valBkNotChange(province);
			valNonBk(province);
			province.setActiveState(false);
			provinceService.update(province);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(province);
	}
	
	@PutMapping("/province/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Province province = provinceService.findById(id);
			province.setActiveState(false);
			provinceService.update(province);
			return ResponseEntity.ok(province);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/province/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(provinceService.findById(id));
	}
	
	@GetMapping("/province")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(provinceService.findAll());
	}
	
	private Exception valIdNull(Province prov) throws Exception {
		if(prov.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Province prov) throws Exception{
		if(prov.getId()==null) {
			throw new Exception("Province doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(provinceService.findById(id)==null) {
			throw new Exception("Province does'nt exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Province prov) throws Exception{
		if(prov.getProvinceCode()==null) {
			throw new Exception("Province code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Province prov) throws Exception{
		if(provinceService.findByBk(prov.getProvinceCode())!=null) {
			throw new Exception("Province already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(Province prov) throws Exception{
		if(!prov.getProvinceCode().equalsIgnoreCase(provinceService.findById(prov.getId()).getProvinceCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(Province prov) throws Exception {
		if(prov.getProvinceName() == null) {
			throw new Exception("Province name must be filled");
		}
		return null;
	}
}
