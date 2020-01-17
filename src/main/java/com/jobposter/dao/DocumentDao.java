package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Document;

@Repository
public class DocumentDao extends CommonDao {

	@Transactional
	public void save (Document doc) {
		super.entityManager.merge(doc);
	}
	
	@Transactional
	public void delete(String id) {
		Document doc = findById(id);
		super.entityManager.remove(doc);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Document findById(String id) {
		List<Document> list = super.entityManager
				.createQuery("from Document where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Document)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Document> findAll(){
		List<Document> list = super.entityManager
				.createQuery("from Document")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Document>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Document findByBk(String Bk1, String Bk2) {
		List<Document> list = super.entityManager
				.createQuery("from Document where user.id =: bk1 and fileName =: bk2")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Document)list.get(0);
	}
}
