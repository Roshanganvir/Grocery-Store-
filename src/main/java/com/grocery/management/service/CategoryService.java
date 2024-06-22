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
import com.grocery.management.dao.CategoryDao;
import com.grocery.management.entity.Category;
import com.grocery.management.model.ResponseDetails;
import com.grocery.management.playload.CategoryPayload;

import jakarta.transaction.Transactional;

@Service
public class CategoryService {

	@Autowired
	private CategoryDao categoryDao;

	@Transactional
	public ResponseEntity<Object> create(CategoryPayload categoryPayload) {

		if (categoryDao.isAreadyExits(categoryPayload.getName())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ResponseDetails(ApplicationResponseCode.RESOURCE_CONFLICT, MessageFormat.format(
							Constants.ALREADY_EXITS_NAME, EntityTypes.CATEGORY.getTitle(), categoryPayload.getName())));
		}

		if (Objects.isNull(categoryPayload.getName()) || categoryPayload.getName().equalsIgnoreCase("null")
				|| categoryPayload.getName().isEmpty() || categoryPayload.getName().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Name")));
		}

		if (categoryPayload.getName().length() >= 20) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.NAME_SIZE_SHOULD_BE, categoryPayload.getName())));
		}

		if (Objects.isNull(categoryPayload.getDescription())
				|| categoryPayload.getDescription().equalsIgnoreCase("null")
				|| categoryPayload.getDescription().isEmpty()
				|| categoryPayload.getDescription().trim().length() == 0) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.RESOURCE_CANNOT_BE_EMPTY, "Description")));
		}

		if (categoryPayload.getDescription().length() >= 50) {
			return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.CONSTRAINT_VOILATION,
					MessageFormat.format(Constants.DESCRIPTION_SIZE_SHOULD_BE, categoryPayload.getDescription())));
		}

		Category category = new Category();
		category.setId(EntityTypes.CATEGORY.getIdPrefix().concat(UUID.randomUUID().toString()));
		category.setName(categoryPayload.getName());
		category.setDescription(categoryPayload.getDescription());
		category.setItems(categoryPayload.getItems());
		category.setCreateAt(LocalDateTime.now());
		category.setUpdateAt(LocalDateTime.now());
		category.setIsDeleted(false);

		category = (Category) categoryDao.save(category);
		categoryPayload.setId(category.getId());
		categoryPayload.setDescription(category.getDescription());
		categoryPayload.setName(category.getName());
		categoryPayload.setItems(category.getItems());
		categoryPayload.setCreateAt(LocalDateTime.now());
		categoryPayload.setUpdateAt(LocalDateTime.now());

		return ResponseEntity.ok(categoryPayload);
	}

	public ResponseEntity<Object> get(String id) {
		Category category = (Category) categoryDao.getById(id);

		if (!Objects.isNull(category)) {
			CategoryPayload categoryPayload = new CategoryPayload();
			categoryPayload.setId(category.getId());
			categoryPayload.setDescription(category.getDescription());
			categoryPayload.setName(category.getName());
			categoryPayload.setItems(category.getItems());
			categoryPayload.setCreateAt(LocalDateTime.now());
			categoryPayload.setUpdateAt(LocalDateTime.now());
			return ResponseEntity.ok(categoryPayload);
		}
		return ResponseEntity.badRequest().body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
				MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.CATEGORY.getTitle(), id)));
	}

	public List<CategoryPayload> getCategories() {

		return categoryDao.getAll();
	}

	@Transactional
	public ResponseEntity<Object> update(String id, CategoryPayload categoryPayload) {
		Category category = (Category) categoryDao.getById(id);
		Category newCategory = new Category();
		if (!Objects.isNull(category)) {
			newCategory.setId(id);
			newCategory.setName(categoryPayload.getName());
			newCategory.setDescription(categoryPayload.getDescription());
			newCategory.setItems(categoryPayload.getItems());
			newCategory.setCreateAt(category.getCreateAt());
			newCategory.setUpdateAt(LocalDateTime.now());
			newCategory.setIsDeleted(category.getIsDeleted());
			return ResponseEntity.ok(categoryDao.save(newCategory));
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDetails(
				ApplicationResponseCode.INTERNAL_SERVER_ERROR, MessageFormat.format(Constants.RESOURCE_NOT_FOUND, id)));
	}

	@Transactional
	public ResponseEntity<Object> delete(String id) {
		Category category = (Category) categoryDao.getById(id);
		if (!Objects.isNull(category)) {
			category.setIsDeleted(true);
			categoryDao.save(category);
			return ResponseEntity.ok(categoryDao.deleteById(id));
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new ResponseDetails(ApplicationResponseCode.RESOURCE_NOT_FOUND,
						MessageFormat.format(Constants.RESOURCE_NOT_FOUND, EntityTypes.CATEGORY.getTitle(), id)));
	}

}
