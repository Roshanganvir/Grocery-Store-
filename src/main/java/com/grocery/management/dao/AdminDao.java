package com.grocery.management.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.grocery.management.entity.Admin;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class AdminDao {

	@PersistenceContext
	private EntityManager entityManager;

	public Boolean isAlreadyExits(String email) {
		try {
			Query query = entityManager.createQuery("from Admin a where a.email = :email and a.isDeleted =false");
			query.setParameter("email", email);
			query.getSingleResult();
			return true;
		} catch (Exception e) {

			return false;
		}
	}

	public Object save(Admin admin) {

		return entityManager.merge(admin);
	}

	public Object getbyId(String id) {

		try {
			Query query = entityManager.createQuery("from Admin a where a.id= :id and a.isDeleted = false");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Admin> getAll() {
		try {
			Query query = entityManager.createQuery("from Admin a where a.isDeleted = false");
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public Object deleteById(String id) {
		try {
			Query query = entityManager.createQuery("update Admin a set a.isDeleted = true where a.id = :id");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
