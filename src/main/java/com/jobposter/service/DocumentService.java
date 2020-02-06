package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.DocumentDao;
import com.jobposter.entity.Document;
import com.jobposter.exception.ErrorException;

@Service("documentService")
public class DocumentService {

	@Autowired
	private DocumentDao documentDao;

	public Document findById(String id) {
		Document doc = documentDao.findById(id);
		return doc;
	}
	
	public void insert(Document doc) throws ErrorException{
		documentDao.save(doc);
	}
	
	public void update(Document doc) throws ErrorException{
		documentDao.save(doc);
	}
	
	public void delete(Document doc) throws ErrorException{
		documentDao.delete(doc);
	}
	
	public Document findByBk(String Bk1, String Bk2) throws ErrorException {
		Document doc = documentDao.findByBk(Bk1, Bk2);
		return doc;
	}
	
	public List<Document> findAll()  throws ErrorException {
		List<Document> doc = documentDao.findAll();
		return doc;
	}
	
	public List<Document> findADUser(String id)  throws ErrorException {
		List<Document> doc = documentDao.findADUser(id);
		return doc;
	}
	
	public List<Document> findADUser(String id, String idDoc)  throws ErrorException {
		List<Document> doc = documentDao.findADUser(id, idDoc);
		return doc;
	}
	
	public Long filterDoc(String id) throws ErrorException {
		return documentDao.filterDoc(id);
	}
}
