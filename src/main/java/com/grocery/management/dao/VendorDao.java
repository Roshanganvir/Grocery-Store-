package com.grocery.management.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.grocery.management.entity.Vendor;
import com.grocery.management.playload.VendorPayload;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class VendorDao {

    @PersistenceContext
    private EntityManager entityManager;

    public Boolean isAlreadyExists(String email) {
        try {
            Query query = entityManager.createQuery("from Vendor v where v.email = :email and v.isDeleted = false");
            query.setParameter("email", email);
            query.getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Object save(Vendor vendor) {
        return entityManager.merge(vendor);
    }

    public Object getById(String id) {
        try {
            Query query = entityManager.createQuery("from Vendor v where v.id = :id and v.isDeleted = false");
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<VendorPayload> getAll() {
        try {
            Query query = entityManager.createQuery("from Vendor v where v.isDeleted = false");
            return query.getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    public Object deleteById(String id) {
        try {
            Query query = entityManager.createQuery("update Vendor v set v.isDeleted = true where v.id = :id");
            query.setParameter("id", id);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}
