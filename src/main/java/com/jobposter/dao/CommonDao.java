package com.jobposter.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

@Repository
@Transactional
public abstract class CommonDao {
	
	@PersistenceContext
	protected EntityManager entityManager;

}
