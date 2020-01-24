package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.SkillLevel;

@Repository
public class SkillLevelDao extends CommonDao {
	
	@Transactional
	public void save (SkillLevel sl) {
		super.entityManager.merge(sl);
	}
	
	@Transactional
	public void delete(SkillLevel sl) {
		super.entityManager.remove(sl);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public SkillLevel findById(String id) {
		List<SkillLevel> list = super.entityManager
				.createQuery("from SkillLevel where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return new SkillLevel();
		else
			return (SkillLevel)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<SkillLevel> findAll(){
		List<SkillLevel> list = super.entityManager
				.createQuery("from SkillLevel where activeState =: active")
				.setParameter("active",true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<SkillLevel>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public SkillLevel findByBk(String Bk) {
		List<SkillLevel> list = super.entityManager
				.createQuery("from SkillLevel where skillLevelCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (SkillLevel)list.get(0);
	}


}
