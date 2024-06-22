package com.grocery.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.management.playload.AdminPayload;
import com.grocery.management.playload.PasswordChangeRequest;
import com.grocery.management.service.AdminServices;

@RestController
@RequestMapping("/api")
public class AdminController {

	@Autowired
	private AdminServices adminServices;

	@PostMapping("/admin")
	public ResponseEntity<Object> create(@RequestBody AdminPayload adminPayload) throws Exception {

		return adminServices.create(adminPayload);

	}

	@GetMapping("/admin/{id}")
	public ResponseEntity<Object> admin(@PathVariable String id) {

		return adminServices.admin(id);

	}

	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<Object> delete(@PathVariable String id) {
		return adminServices.delete(id);
	}

	@PutMapping("/admin/update/{id}")
	public ResponseEntity<Object> update(@PathVariable String id, @RequestBody AdminPayload adminpayloadwithId) {
		return adminServices.update(id, adminpayloadwithId);
	}

	@GetMapping("/admin")
	public ResponseEntity<List<AdminPayload>> admins() {

		List<AdminPayload> getList = adminServices.admins();

		return ResponseEntity.ok(getList);

	}

	@PostMapping("/admin/change-password")
	public ResponseEntity<Object> changePassword(@RequestHeader("id") String id,
			@RequestBody PasswordChangeRequest passwordChangeRequest) {
		return adminServices.changePassword(id, passwordChangeRequest);
	}

}
