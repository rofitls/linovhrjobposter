package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Province;

@Repository
public class ProvinceDao extends CommonDao {
	
	@Transactional
	public void save (Province province) {
		super.entityManager.merge(province);
	}
	
	@Transactional
	public void delete(Province province) {
		super.entityManager.remove(province);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Province findById(String id) {
		List<Province> list = super.entityManager
				.createQuery("from Province where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Province)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Province> findAll(){
		List<Province> list = super.entityManager
				.createQuery("from Province where activeState=:active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Province>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Province findByBk(String BK) {
		List<Province> list = super.entityManager
				.createQuery("from Province where provinceCode=:bk")
				.setParameter("bk", BK)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Province)list.get(0);
	}

}
