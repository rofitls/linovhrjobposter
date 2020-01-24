package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.City;


@Repository
public class CityDao extends CommonDao {
	
	@Transactional
	public void save (City city) {
		super.entityManager.merge(city);
	}
	
	@Transactional
	public void delete(City city) {
		super.entityManager.remove(city);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public City findById(String id) {
		List<City> list = super.entityManager
				.createQuery("from City where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (City)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<City> findAll(){
		List<City> list = super.entityManager
				.createQuery("from City where activeState=:active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<City>)list;
	}
	

	@SuppressWarnings("unchecked")
	@Transactional
	public City findByBk(String Bk) {
		List<City> list = super.entityManager
				.createQuery("from City where cityCode=: bk")
				.setParameter("bk", Bk)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (City)list.get(0);
	}


}
