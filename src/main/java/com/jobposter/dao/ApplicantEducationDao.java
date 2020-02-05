package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicantEducation;

@Repository
public class ApplicantEducationDao extends CommonDao {

	@Transactional
	public void save (ApplicantEducation appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(ApplicantEducation appl) {
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantEducation findById(String id) {
		List<ApplicantEducation> list = super.entityManager
				.createQuery("from ApplicantEducation where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantEducation)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantEducation> findAll(){
		List<ApplicantEducation> list = super.entityManager
				.createQuery("from ApplicantEducation")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantEducation>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantEducation> findAEUser(String id){
		List<ApplicantEducation> list = super.entityManager
				.createQuery("from ApplicantEducation ae where ae.user.id =: id order by ae.endDate")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantEducation>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantEducation findByBk(String Bk1, String Bk2, String Bk3) {
		List<ApplicantEducation> list = super.entityManager
				.createQuery("from ApplicantEducation where user.id =: bk1 and major.id =: bk2 and school =: bk3")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantEducation)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantEducation findRecentEducationApplicant(String id) {
		List<ApplicantEducation> list = super.entityManager
				.createQuery("from ApplicantEducation where user.id =: id order by endDate")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantEducation)list.get(list.size()-1);
	}
}
