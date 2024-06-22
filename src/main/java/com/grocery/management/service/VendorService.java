package com.grocery.management.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.grocery.management.constants.ApplicationResponseCode;
import com.grocery.management.constants.Constants;
import com.grocery.management.constants.EntityTypes;
import com.grocery.management.dao.VendorDao;
import com.grocery.management.entity.Vendor;
import com.grocery.management.model.ResponseDetails;
import com.grocery.management.playload.VendorPayload;

import jakarta.transaction.Transactional;

@Service
public class VendorService {

    @Autowired
    private VendorDao vendorDao;

    @Transactional
    public ResponseEntity<Object> create(VendorPayload vendorPayload) {

        if (vendorDao.isAlreadyExists(vendorPayload.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDetails(ApplicationResponseCode.RESOURCE_CONFLICT, MessageFormat.format(
                            Constants.ALREADY_EXITS_NAME, EntityTypes.VENDOR.getTitle(), vendorPayload.getEmail())));
        }

        if (Objects.isNull(vendorPayload.getName()) || vendorPayload.getName().equalsIgnoreCase("null")
                || vendorPayload.getName().isEmpty() || vendorPayload.getName().trim().length() == 0) {
            return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
                    MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Name")));
        }

        if (vendorPayload.getName().length() >= 50) {
            return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
                    MessageFormat.format(Constants.NAME_SIZE_SHOULD_BE, vendorPayload.getName())));
        }

        if (Objects.isNull(vendorPayload.getEmail()) || vendorPayload.getEmail().equalsIgnoreCase("null")
                || vendorPayload.getEmail().isEmpty() || vendorPayload.getEmail().trim().length() == 0) {
            return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
                    MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Email")));
        }

        Vendor vendor = new Vendor();
        vendor.setId(EntityTypes.VENDOR.getIdPrefix().concat(UUID.randomUUID().toString()));
        vendor.setName(vendorPayload.getName());
        vendor.setEmail(vendorPayload.getEmail());
        vendor.setPhoneNumber(vendorPayload.getPhoneNumber());
        vendor.setAddress(vendorPayload.getAddress());
        vendor.setLocation(vendorPayload.getLocation());
        vendor.setCreatedAt(LocalDateTime.now());
        vendor.setUpdatedAt(LocalDateTime.now());
        vendor.setIsDeleted(false);

        vendor = (Vendor) vendorDao.save(vendor);
        vendorPayload.setId(vendor.getId());
        vendorPayload.setCreatedAt(vendor.getCreatedAt());
        vendorPayload.setUpdatedAt(vendor.getUpdatedAt());

        return ResponseEntity.ok(vendorPayload);
    }

    public ResponseEntity<Object> get(String id) {
        Vendor vendor = (Vendor) vendorDao.getById(id);

        if (!Objects.isNull(vendor)) {
            VendorPayload vendorPayload = new VendorPayload();
            vendorPayload.setId(vendor.getId());
            vendorPayload.setName(vendor.getName());
            vendorPayload.setEmail(vendor.getEmail());
            vendorPayload.setPhoneNumber(vendor.getPhoneNumber());
            vendorPayload.setAddress(vendor.getAddress());
            vendorPayload.setLocation(vendor.getLocation());
            vendorPayload.setCreatedAt(vendor.getCreatedAt());
            vendorPayload.setUpdatedAt(vendor.getUpdatedAt());
            return ResponseEntity.ok(vendorPayload);
        }
        return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
                MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.VENDOR.getTitle(), id)));
    }

    public List<VendorPayload> getVendors() {
        return vendorDao.getAll();
    }

    @Transactional
    public ResponseEntity<Object> update(String id, VendorPayload vendorPayload) {
        Vendor vendor = (Vendor) vendorDao.getById(id);
        if (!Objects.isNull(vendor)) {
            vendor.setName(vendorPayload.getName());
            vendor.setEmail(vendorPayload.getEmail());
            vendor.setPhoneNumber(vendorPayload.getPhoneNumber());
            vendor.setAddress(vendorPayload.getAddress());
            vendor.setLocation(vendorPayload.getLocation());
            vendor.setUpdatedAt(LocalDateTime.now());
            return ResponseEntity.ok(vendorDao.save(vendor));
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDetails(
                ApplicationResponseCode.INTERNAL_SERVER_ERROR, MessageFormat.format(Constants.RESOURCE_NOT_FOUND, id)));
    }

    @Transactional
    public ResponseEntity<Object> delete(String id) {
        Vendor vendor = (Vendor) vendorDao.getById(id);
        if (!Objects.isNull(vendor)) {
        	
            vendor.setIsDeleted(true);
            vendorDao.save(vendor);
            return ResponseEntity.ok(vendorDao.deleteById(id));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
                        MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.VENDOR.getTitle(), id)));
    }
}
