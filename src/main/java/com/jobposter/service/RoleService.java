package com.jobposter.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobposter.dao.RoleDao;
import com.jobposter.entity.Role;
import com.jobposter.exception.ErrorException;

@Service("roleService")
public class RoleService {
	
	@Autowired
	private RoleDao roleDao;

	public Role findById(String id) {
		Role role = roleDao.findById(id);
		return role;
	}
	
	public void insert(Role role) throws ErrorException{
		roleDao.save(role);
	}
	
	public void update(Role role) throws ErrorException{
		roleDao.save(role);
	}
	
	public void delete(Role role) throws ErrorException{
		roleDao.delete(role);
	}
	
	public Role findByBk(String BK)  throws ErrorException {
		Role role = roleDao.findByBK(BK);
		return role;
	}
	
	public List<Role> findAll()  throws ErrorException {
		List<Role> role = roleDao.findAll();
		return role;
	}
	
	public Role findByName(String name) throws ErrorException {
		return roleDao.findByName(name);
	}
}
