package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.DocumentType;

@Repository
public class DocumentTypeDao extends CommonDao {

	@Transactional
	public void save (DocumentType dt) {
		super.entityManager.merge(dt);
	}
	
	@Transactional
	public void delete(String id) {
		DocumentType dt = findById(id);
		super.entityManager.remove(dt);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public DocumentType findById(String id) {
		List<DocumentType> list = super.entityManager
				.createQuery("from DocumentType where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (DocumentType)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<DocumentType> findAll(){
		List<DocumentType> list = super.entityManager
				.createQuery("from DocumentType")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<DocumentType>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public DocumentType findByBk(String Bk) {
		List<DocumentType> list = super.entityManager
				.createQuery("from DocumentType where documentTypeCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (DocumentType)list.get(0);
	}
}
