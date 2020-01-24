package com.jobposter.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.jobposter.entity.Role;


@Repository
public class RoleDao extends CommonDao {
	
	@Transactional
	public void save (Role role) {
		super.entityManager.merge(role);
	}
	
	@Transactional
	public void delete(Role role) {
		super.entityManager.remove(role);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Role findById(String id) {
		List<Role> list = super.entityManager
				.createQuery("from Role where id=:id")
				.setParameter("id", id)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Role)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Role> findAll(){
		List<Role> list = super.entityManager
				.createQuery("from Role where activeState =: active")
				.setParameter("active", true)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (List<Role>)list;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Role findByBK(String BK) {
		List<Role> list = super.entityManager
				.createQuery("from Role where roleCode=: bk")
				.setParameter("bk", BK)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Role)list.get(0);
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Role findByName(String name) {
		List<Role> list = super.entityManager
				.createQuery("from Role where roleName =: name")
				.setParameter("name", name)
				.getResultList();
		if(list.size()==0)
			return null;
		else
			return (Role)list.get(0);
	}

}
