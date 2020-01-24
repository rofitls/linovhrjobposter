package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicantSkill;

@Repository
public class ApplicantSkillDao extends CommonDao {
	
	@Transactional
	public void save (ApplicantSkill appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(ApplicantSkill appl) {
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantSkill findById(String id) {
		List<ApplicantSkill> list = super.entityManager
				.createQuery("from ApplicantSkill where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantSkill)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantSkill> findAll(){
		List<ApplicantSkill> list = super.entityManager
				.createQuery("from ApplicantSkill")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantSkill>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantSkill> findASUser(String id){
		List<ApplicantSkill> list = super.entityManager
				.createQuery("from ApplicantSkill asu where asu.user.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantSkill>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantSkill findByBk(String Bk1, String Bk2, String Bk3) {
		List<ApplicantSkill> list = super.entityManager
				.createQuery("from ApplicantSkill where user.id =: bk1 and skillLevel.id =: bk2 and skillName =: bk3")
				.setParameter("bk1", Bk1)
				.setParameter("bk2", Bk2)
				.setParameter("bk3", Bk3)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantSkill)list.get(0);
	}

}
