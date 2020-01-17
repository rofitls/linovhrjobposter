package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.ProvinceDao;
import com.jobposter.entity.Province;
import com.jobposter.exception.ErrorException;

@Service("provinceService")
public class ProvinceService {

	@Autowired
	private ProvinceDao provinceDao;

	public Province findById(String id) {
		Province province = provinceDao.findById(id);
		return province;
	}
	
	public void insert(Province province) throws ErrorException{
		provinceDao.save(province);
	}
	
	public void update(Province province) throws ErrorException{
		provinceDao.save(province);
	}
	
	public void delete(String id) throws ErrorException{
		provinceDao.delete(id);
	}
	
	public Province findByBk(String BK) throws ErrorException {
		Province province = provinceDao.findByBk(BK);
		return province;
	}
	
	public List<Province> findAll()  throws ErrorException {
		List<Province> province = provinceDao.findAll();
		return province;
	}
}
