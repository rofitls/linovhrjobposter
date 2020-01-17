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

import com.jobposter.entity.City;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.CityService;
import com.jobposter.service.ProvinceService;

@RestController
@RequestMapping("/admin")
public class CityController {
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@PostMapping("/city")
	public ResponseEntity<?> insert(@RequestBody City city) throws ErrorException{
		try {
			valIdNull(city);
			valBkNotNull(city);
			valBkNotExist(city);
			valNonBk(city);
			cityService.insert(city);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body("Model Berhasil Ditambah");
	}
	
	@PutMapping("/city")
	public ResponseEntity<?> update(@RequestBody City city) throws ErrorException{
		try {
			valIdNotNull(city);
			valIdExist(city.getId());
			valBkNotNull(city);
			valBkNotChange(city);
			valNonBk(city);
			cityService.update(city);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Diperbarui");
	}
	
	@PutMapping("/city/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			City city = cityService.findById(id);
			city.setActiveState(false);
			cityService.update(city);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body("Model Berhasil Dihapus");
	}
	
	@GetMapping("/city/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(cityService.findById(id));
	}
	
	@GetMapping("/city")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(cityService.findAll());
	}
	
	private Exception valIdNull(City city) throws Exception {
		if(city.getId()!=null) {
			throw new Exception("Register Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(City city) throws Exception{
		if(city.getId()==null) {
			throw new Exception("City doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(cityService.findById(id)==null) {
			throw new Exception("City doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(City city) throws Exception{
		if(city.getCityCode()==null) {
			throw new Exception("City code must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (City city) throws Exception{
		if(cityService.findByBk(city.getCityCode())!=null) {
			throw new Exception("City already exists");
		}else if(provinceService.findById(city.getProvince().getId())== null || provinceService.findById(city.getProvince().getId()).isActiveState()==false) {
			throw new Exception("Province doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(City city) throws Exception{
		if(!city.getCityCode().equalsIgnoreCase(cityService.findById(city.getId()).getCityCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(City city) throws Exception {
		if(city.getCityName() == null) {
			throw new Exception("City name must be filled");
		}
		return null;
	}
}

