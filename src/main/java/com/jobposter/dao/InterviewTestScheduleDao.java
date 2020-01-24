package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.InterviewTestSchedule;

@Repository
public class InterviewTestScheduleDao extends CommonDao {

	@Transactional
	public void save (InterviewTestSchedule schedule) {
		super.entityManager.merge(schedule);
	}
	
	@Transactional
	public void delete(InterviewTestSchedule schedule) {
		super.entityManager.remove(schedule);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public InterviewTestSchedule findById(String id) {
		List<InterviewTestSchedule> list = super.entityManager
				.createQuery("from InterviewTestSchedule where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (InterviewTestSchedule)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<InterviewTestSchedule> findAll(){
		List<InterviewTestSchedule> list = super.entityManager
				.createQuery("from InterviewTestSchedule")
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<InterviewTestSchedule>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public InterviewTestSchedule findScheduleByApplication(String id) {
		List<InterviewTestSchedule> list = super.entityManager
				.createQuery("from InterviewTestSchedule where application.id =: id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (InterviewTestSchedule)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Long countSchedule() {
		StringBuilder query = new StringBuilder();
		query.append("select count(schedule) from InterviewTestSchedule schedule");
		List<Long> list = super.entityManager
				.createQuery(query.toString())
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Long)list.get(0);
	}
}
