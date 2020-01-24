package com.jobposter.controller;

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

import com.jobposter.entity.DocumentType;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.DocumentTypeService;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class DocumentTypeController {
	
	@Autowired
	private DocumentTypeService documentTypeService;
	
	@PostMapping("/doc-type")
	public ResponseEntity<?> insert(@RequestBody DocumentType dt) throws ErrorException{
		try {
			valIdNull(dt);
			valBkNotNull(dt);
			valBkNotExist(dt);
			valNonBk(dt);
			documentTypeService.insert(dt);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(dt);
	}
	
	@PutMapping("/doc-type")
	public ResponseEntity<?> update(@RequestBody DocumentType dt) throws ErrorException{
		try {
			valIdNotNull(dt);
			valIdExist(dt.getId());
			valBkNotNull(dt);
			valBkNotChange(dt);
			valNonBk(dt);
			documentTypeService.update(dt);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(dt);
	}
	
	@DeleteMapping("/doc-type/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			DocumentType dt = documentTypeService.findById(id);
			documentTypeService.delete(dt);
			return ResponseEntity.ok(dt);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
	}
	
	@GetMapping("/doc-type/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(documentTypeService.findById(id));
	}
	
	@GetMapping("/doc-type")
	public ResponseEntity<?> getAll()  throws ErrorException{
		System.out.println(documentTypeService.filterDoc());
		return ResponseEntity.ok(documentTypeService.findAll());
	}
	
	private Exception valIdNull(DocumentType dt) throws Exception {
		if(dt.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(DocumentType dt) throws Exception{
		if(dt.getId()==null) {
			throw new Exception("Applicant education doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(documentTypeService.findById(id)==null) {
			throw new Exception("Document type doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(DocumentType dt) throws Exception{
		if(dt.getDocTypeCode()==null) {
			throw new Exception("Document Type must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (DocumentType dt) throws Exception{
		if(documentTypeService.findByBk(dt.getDocTypeCode())!=null) {
			throw new Exception("Document type already exists");
		}
		return null;
	}
	
	private Exception valBkNotChange(DocumentType dt) throws Exception{
		if(!dt.getDocTypeCode().equalsIgnoreCase(documentTypeService.findById(dt.getId()).getDocTypeCode())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
	private Exception valNonBk(DocumentType dt) throws Exception {
		if(dt.getDocTypeName() == null) {
			throw new Exception("Document Type Name must be filled");
		}
		return null;
	}
}