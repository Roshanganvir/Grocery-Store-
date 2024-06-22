package com.grocery.management.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.grocery.management.entity.Category;
import com.grocery.management.playload.CategoryPayload;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class CategoryDao {

	@PersistenceContext
	private EntityManager entityManager;

	public Boolean isAreadyExits(String name) {
		try {
			Query query = entityManager.createQuery("from Category c where c.name = :name and c.isDeleted = false");
			query.setParameter("name", name);
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Object getByName(String name) {
		try {
			Query query = entityManager.createQuery("from Category c where c.name = :name and c.isDeleted = false");
			query.setParameter("name", name);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Object save(Category category) {
		return entityManager.merge(category);
	}

	public Object getById(String id) {
		try {
			Query query = entityManager.createQuery("from Category c where c.id =:id and c.isDeleted = false");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CategoryPayload> getAll() {
		try {
			Query query = entityManager.createQuery("from Category c where c.isDeleted = false");
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}

	public Object deleteById(String id) {
		try {
			Query query = entityManager.createQuery("update Category c set c.isDeleted = true where c.id =:id");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
