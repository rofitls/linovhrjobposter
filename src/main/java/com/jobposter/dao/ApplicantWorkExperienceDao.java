package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicantWorkExperience;

@Repository
public class ApplicantWorkExperienceDao extends CommonDao {


	@Transactional
	public void save (ApplicantWorkExperience appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(ApplicantWorkExperience appl) {
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantWorkExperience findById(String id) {
		List<ApplicantWorkExperience> list = super.entityManager
				.createQuery("from ApplicantWorkExperience where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantWorkExperience)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantWorkExperience> findAWEUser(String id) {
		List<ApplicantWorkExperience> list = super.entityManager
				.createQuery("from ApplicantWorkExperience awe where awe.user.id =: id order by awe.endDate")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantWorkExperience>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantWorkExperience> findAll(){
		List<ApplicantWorkExperience> list = super.entityManager
				.createQuery("from ApplicantWorkExperience")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantWorkExperience>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantWorkExperience findByBk(String Bk1, String Bk2, String Bk3, String Bk4) {
		List<ApplicantWorkExperience> list = super.entityManager
				.createQuery("from ApplicantWorkExperience where user.id =: bk1 and company =: bk2 and jobLevel.id =: bk3 and jobCategory.id =: bk4")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.setParameter("bk4", Bk4)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantWorkExperience)list.get(0);
	}
}
