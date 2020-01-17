package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.DocumentTypeDao;
import com.jobposter.entity.DocumentType;
import com.jobposter.exception.ErrorException;

@Service("documentTypeService")
public class DocumentTypeService {

	@Autowired
	private DocumentTypeDao documentTypeDao;

	public DocumentType findById(String id) {
		DocumentType dt = documentTypeDao.findById(id);
		return dt;
	}
	
	public void insert(DocumentType dt) throws ErrorException{
		documentTypeDao.save(dt);
	}
	
	public void update(DocumentType dt) throws ErrorException{
		documentTypeDao.save(dt);
	}
	
	public void delete(String id) throws ErrorException{
		documentTypeDao.delete(id);
	}
	
	public DocumentType findByBk(String Bk)  throws ErrorException {
		DocumentType dt = documentTypeDao.findByBk(Bk);
		return dt;
	}
	
	public List<DocumentType> findAll()  throws ErrorException {
		List<DocumentType> dt = documentTypeDao.findAll();
		return dt;
	}
	
	public Long filterDoc() throws ErrorException {
		return documentTypeDao.filterDoc();
	}
}
