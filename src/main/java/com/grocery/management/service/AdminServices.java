package com.grocery.management.service;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.grocery.management.dao.AdminDao;
import com.grocery.management.entity.Admin;
import com.grocery.management.model.ResponseDetails;
import com.grocery.management.playload.AdminPayload;
import com.grocery.management.playload.PasswordChangeRequest;
import com.grocery.management.security.AdminUtils;

import jakarta.transaction.Transactional;

@Service
public class AdminServices {

	@Autowired
	private AdminDao adminDao;

	@Autowired
	private AdminUtils adminUtils;

	@Transactional
	public ResponseEntity<Object> create(AdminPayload adminPayload) throws Exception {

		String encryptedPassword = adminUtils.encryptedPassword(adminPayload.getPassword());
		String decryptedPassword = adminUtils.decryptedPassword(encryptedPassword);

		if (adminDao.isAlreadyExits(adminPayload.getEmail())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ResponseDetails(ApplicationResponseCode.RESOURCE_CONFLICT, MessageFormat
							.format(Constants.ALREADY_EXITS, EntityTypes.ADMIN.getTitle(), adminPayload.getEmail())));
		}

		if (Objects.isNull(adminPayload.getEmail()) || adminPayload.getEmail().equalsIgnoreCase("null")
				|| adminPayload.getEmail().isEmpty() || adminPayload.getEmail().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, Constants.EMAIL)));
		}

		if (!adminPayload.getEmail().matches(
				"^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$")) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CORRECTION_EMAIL, adminPayload.getEmail())));
		}

		if (Objects.isNull(adminPayload.getName()) || adminPayload.getName().equalsIgnoreCase("null")
				|| adminPayload.getName().isEmpty() || adminPayload.getName().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Name")));
		}

		if (adminPayload.getName().length() >= 20) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.NAME_SIZE_SHOULD_BE, adminPayload.getName())));
		}

		if (!adminPayload.getName().matches("^[a-zA-Z0-9 ]*$")) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CORRECTION_NAME, adminPayload.getName())));
		}

		if (Objects.isNull(adminPayload.getPassword()) || adminPayload.getPassword().equalsIgnoreCase("null")
				|| adminPayload.getPassword().isEmpty() || adminPayload.getPassword().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Password")));
		}

		if (adminPayload.getPassword().length() <= 8) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.PASSWORD_SIZE_SHOULD_BE, adminPayload.getPassword())));
		}

		if (!adminPayload.getPassword().matches("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$")) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CORRECTION_PASSWORD, adminPayload.getPassword())));
		}

		Admin admin = new Admin();
		admin.setId(EntityTypes.ADMIN.getIdPrefix().concat(UUID.randomUUID().toString()));
		admin.setName(adminPayload.getName());
		admin.setEmail(adminPayload.getEmail());
		admin.setPassword(adminUtils.encryptedPassword(adminPayload.getPassword()));
		admin.setCreatedAt(LocalDateTime.now());
		admin.setUpdateAt(LocalDateTime.now());
		admin.setIsDeleted(false);

		admin = (Admin) adminDao.save(admin);
		adminPayload.setPassword("********");
		adminPayload.setId(admin.getId());
		adminPayload.setEmail(admin.getEmail());
		adminPayload.setCreatedAt(LocalDateTime.now());
		adminPayload.setUpdateAt(LocalDateTime.now());
		return ResponseEntity.ok(adminPayload);
	}

	public ResponseEntity<Object> admin(String id) {

		Admin admin = (Admin) adminDao.getbyId(id);

		if (!Objects.isNull(admin)) {
			AdminPayload adminPayload = new AdminPayload();
			adminPayload.setId(admin.getId());
			adminPayload.setEmail(admin.getEmail());
			adminPayload.setName(admin.getName());
			adminPayload.setPassword("********");
			adminPayload.setCreatedAt(LocalDateTime.now());
			adminPayload.setUpdateAt(LocalDateTime.now());
			return ResponseEntity.ok(adminPayload);
		}

		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ADMIN.getTitle(), id)));

	}

	@Transactional
	public ResponseEntity<Object> delete(String id) {
		Admin admin = (Admin) adminDao.getbyId(id);
		if (!Objects.isNull(admin)) {
			admin.setIsDeleted(true);
			adminDao.save(admin);
			return ResponseEntity.ok(adminDao.deleteById(id));
		}
		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ADMIN.getTitle(), id)));
	}

	@Transactional
	public ResponseEntity<Object> update(String id, AdminPayload adminpayload) {

		Admin admin = (Admin) adminDao.getbyId(id);
		Admin newAdmin = new Admin();
		if (!Objects.isNull(admin)) {
			newAdmin.setId(id);
			newAdmin.setName(adminpayload.getName());
			newAdmin.setEmail(adminpayload.getEmail());
			newAdmin.setPassword(admin.getPassword());
			newAdmin.setUpdateAt(LocalDateTime.now());
			newAdmin.setCreatedAt(admin.getCreatedAt());
			newAdmin.setIsDeleted(admin.getIsDeleted());
			return ResponseEntity.ok(adminDao.save(newAdmin));
		}

		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.INTERNAL_SERVER_ERROR,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, id)));
	}

	public List<AdminPayload> admins() {

		List<Admin> admin = adminDao.getAll();
		List<AdminPayload> adminPayloads = new ArrayList<>();
		for(Admin admin2 : admin) {
		AdminPayload adminPayload = new AdminPayload();	
		adminPayload.setId(admin2.getId());
		adminPayload.setEmail(admin2.getEmail());
		adminPayload.setName(admin2.getName());
		adminPayload.setPassword("********");
		adminPayload.setCreatedAt(LocalDateTime.now());
		adminPayload.setUpdateAt(LocalDateTime.now());
		adminPayloads.add(adminPayload);
		}
		return adminPayloads;
	}
	
	@Transactional
    public ResponseEntity<Object> changePassword(String id, PasswordChangeRequest passwordChangeRequest) {
        Admin admin = (Admin) adminDao.getbyId(id);
        if (admin == null) {
            return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
                    MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.ADMIN.getTitle(), id)));
        }

        String decryptedCurrentPassword = adminUtils.decryptedPassword(admin.getPassword());
        if (!decryptedCurrentPassword.equals(passwordChangeRequest.getCurrentPassword())) {
            return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
                    "Current password did not match"));
        }

        String encryptedNewPassword = adminUtils.encryptedPassword(passwordChangeRequest.getNewPassword());
        admin.setPassword(encryptedNewPassword);
        admin.setUpdateAt(LocalDateTime.now());
        adminDao.save(admin);

        return ResponseEntity.ok(new ResponseDetails("Success", "Password updated successfully"));
    }

}
