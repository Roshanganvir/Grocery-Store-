package com.grocery.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.grocery.management.playload.CategoryPayload;
import com.grocery.management.service.CategoryService;

@RestController
@RequestMapping(value = "/api")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@PostMapping(value = "/category")
	public ResponseEntity<Object>create(@RequestBody CategoryPayload categoryPayload){
		return categoryService.create(categoryPayload);
	}
	
	@GetMapping(value = "/category/{id}")
	public ResponseEntity<Object>get(@PathVariable String id){
		return categoryService.get(id);
	}
	
	@GetMapping(value = "/category")
	public ResponseEntity<List<CategoryPayload>>categories(){
		List<CategoryPayload> getList = categoryService.getCategories();
		return new ResponseEntity<List<CategoryPayload>>(getList,HttpStatus.OK);
	}
	
	@PutMapping(value = "/category/update/{id}")
	public ResponseEntity<Object>update(@PathVariable String id,@RequestBody CategoryPayload categoryPayloadWithId){
		return categoryService.update(id,categoryPayloadWithId);
	}
	
	@DeleteMapping(value = "/category/delete/{id}")
	public ResponseEntity<Object>delete(@PathVariable String id){
		return categoryService.delete(id);
	}
	
}
