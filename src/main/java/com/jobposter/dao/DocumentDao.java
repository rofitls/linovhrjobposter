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
	public void delete(Document doc) {
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
	public List<Document> findADUser(String id){
		List<Document> list = super.entityManager
				.createQuery("from Document ad where ad.user.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Document>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Document findADUser(String id, String idDoc){
		List<Document> list = super.entityManager
				.createQuery("from Document ad where ad.user.id =: id and ad.docType.id =: id2")
				.setParameter("id", id)
				.setParameter("id2", idDoc)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Document)list.get(0);
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
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Document findCVApplicant(String id) {
		List<Document> list = super.entityManager
				.createQuery("from Document where user.id =: id and docType.docTypeName =: type")
				.setParameter("id", id)
				.setParameter("type", "CV")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Document)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long filterDoc(String id) {
		StringBuilder query = new StringBuilder();
		query.append("select count(d) from Document d where d.docType.flag =: flag and d.user.id =: id");
		List<Long> list = super.entityManager
				.createQuery(query.toString())
				.setParameter("flag", true)
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0) 
			return null;
		else
			return list.get(0);
	}
}
