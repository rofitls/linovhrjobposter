package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.ApplicantSalary;

@Repository
public class ApplicantSalaryDao extends CommonDao {

	@Transactional
	public void save (ApplicantSalary appl) {
		super.entityManager.merge(appl);
	}
	
	@Transactional
	public void delete(ApplicantSalary appl) {
		super.entityManager.remove(appl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantSalary findById(String id) {
		List<ApplicantSalary> list = super.entityManager
				.createQuery("from ApplicantSalary where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantSalary)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<ApplicantSalary> findAll(){
		List<ApplicantSalary> list = super.entityManager
				.createQuery("from ApplicantSalary")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<ApplicantSalary>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public ApplicantSalary findByBk(String Bk) {
		List<ApplicantSalary> list = super.entityManager
				.createQuery("from ApplicantSalary where user.id =: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (ApplicantSalary)list.get(0);
	}
}
