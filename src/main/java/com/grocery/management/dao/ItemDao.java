package com.grocery.management.dao;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.grocery.management.entity.Item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class ItemDao {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public Boolean isAlreadyExit(String name, String brand) {
		try {
			Query query = entityManager.createQuery("from Item i where i.name = :name and i.brand = :brand and i.isDeleted = false");
			query.setParameter("name", name);
			query.setParameter("brand", brand);
			query.getSingleResult();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public Object save(Item item) {
		return entityManager.merge(item);
	}
	
	public Object getById(String id) {
		try {
			Query query = entityManager.createQuery("from Item i where i.id = :id and i.isDeleted = false");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Item> getAll(){
		try {
			Query query = entityManager.createQuery("from Item i where i.isDeleted = false");
			return query.getResultList();
		} catch (Exception e) {
			return null;
		}
	}
	
	public Object deleteById(String id) {
		try {
			Query query = entityManager.createQuery("update Item i set i.isDeleted = true where i.id = :id");
			query.setParameter("id", id);
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

}
