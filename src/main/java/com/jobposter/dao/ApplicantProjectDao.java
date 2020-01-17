package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicantProject;

@Repository
public class ApplicantProjectDao extends CommonDao {

	@Transactional
	public void save (ApplicantProject appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(String id) {
		ApplicantProject appl = findById(id);
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantProject findById(String id) {
		List<ApplicantProject> list = super.entityManager
				.createQuery("from ApplicantProject where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantProject)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantProject> findAll(){
		List<ApplicantProject> list = super.entityManager
				.createQuery("from ApplicantProject")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantProject>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantProject findByBk(String Bk1, String Bk2, String Bk3) {
		List<ApplicantProject> list = super.entityManager
				.createQuery("from ApplicantProject where user.id =: bk1 and projectName =: bk2 and role =: bk3")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantProject)list.get(0);
	}
}
