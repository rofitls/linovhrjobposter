package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.CityDao;
import com.jobposter.entity.City;
import com.jobposter.exception.ErrorException;

@Service("cityService")
public class CityService {

	@Autowired
	private CityDao cityDao;

	public City findById(String id) {
		City city = cityDao.findById(id);
		return city;
	}
	
	public void insert(City city) throws ErrorException{
		cityDao.save(city);
	}
	
	public void update(City city) throws ErrorException{
		cityDao.save(city);
	}
	
	public void delete(City city) throws ErrorException{
		cityDao.delete(city);
	}
	
	public City findByBk(String BK) throws ErrorException {
		City city = cityDao.findByBk(BK);
		return city;
	}
	
	public List<City> findAll() throws ErrorException{
		List<City> city = cityDao.findAll();
		return city;
	}
}
