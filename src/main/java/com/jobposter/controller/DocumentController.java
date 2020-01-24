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

import com.jobposter.entity.Document;
import com.jobposter.exception.ErrorException;
import com.jobposter.service.DocumentService;
import com.jobposter.service.UserService;

@RestController
@RequestMapping("/apl")
@CrossOrigin("*")
public class DocumentController {
	
	@Autowired
	private DocumentService documentService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/docs")
	public ResponseEntity<?> insert(@RequestBody Document doc) throws ErrorException{
		try {
			valIdNull(doc);
			valBkNotNull(doc);
			valBkNotExist(doc);
			//valNonBk(doc);
			documentService.insert(doc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(doc);
	}
	
	@PutMapping("/docs")
	public ResponseEntity<?> update(@RequestBody Document doc) throws ErrorException{
		try {
			valIdNotNull(doc);
			valIdExist(doc.getId());
			valBkNotNull(doc);
			valBkNotChange(doc);
			//valNonBk(doc);
			documentService.update(doc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.OK).body(doc);
	}
	
	@DeleteMapping("/docs/{id}")
	public ResponseEntity<?> delete(@PathVariable String id) throws ErrorException {
		try {
			valIdExist(id);
			Document doc = documentService.findById(id);
			documentService.delete(doc);
			return ResponseEntity.ok(doc);
		}catch(Exception e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
		}
		
	}
	
	@GetMapping("/docs/id/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) throws ErrorException {
		return ResponseEntity.ok(documentService.findById(id));
	}
	
	@GetMapping("/docs")
	public ResponseEntity<?> getAll()  throws ErrorException{
		return ResponseEntity.ok(documentService.findAll());
	}
	
	private Exception valIdNull(Document doc) throws Exception {
		if(doc.getId()!=null) {
			throw new Exception("Insert Failed");
		}
		return null;
	}
	
	private Exception valIdNotNull(Document doc) throws Exception{
		if(doc.getId()==null) {
			throw new Exception("Document doesn't exist");
		}
		return null;
	}
	
	private Exception valIdExist(String id) throws Exception{
		if(documentService.findById(id)==null) {
			throw new Exception("Document doesn't exists");
		}
		return null;
	}
	
	private Exception valBkNotNull(Document doc) throws Exception{
		if(doc.getFileName()==null) {
			throw new Exception("File name must be filled");
		}else if(doc.getUser()==null) {
			throw new Exception("User must be filled");
		}
		return null;
	}
	
	private Exception valBkNotExist (Document doc) throws Exception{
		if(documentService.findByBk(doc.getUser().getId(),doc.getFileName())!=null) {
			throw new Exception("Document already exists");
		}else if(userService.findById(doc.getUser().getId())==null) {
			throw new Exception("User doesn't exist");
		}
		return null;
	}
	
	private Exception valBkNotChange(Document doc) throws Exception{
		if(!doc.getFileName().equalsIgnoreCase(documentService.findById(doc.getId()).getFileName())) {
			throw new Exception("BK cannot change");
		}else if(!doc.getUser().getId().equalsIgnoreCase(userService.findById(doc.getUser().getId()).getId())) {
			throw new Exception("BK cannot change");
		}
		return null;
	}
	
//	private Exception valNonBk(Document doc) throws Exception {
//		if(dt.getDocTypeName() == null) {
//			throw new Exception("Document Type Name must be filled");
//		}
//		return null;
//	}
}
