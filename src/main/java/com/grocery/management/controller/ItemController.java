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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.grocery.management.playload.ItemPayload;
import com.grocery.management.service.ItemService;

@RestController
@RequestMapping(value = "/api")
public class ItemController {

	@Autowired
	private ItemService itemService;

	@PostMapping(value = "/uploads")
	public ResponseEntity<String> upload(@RequestHeader("id") String id, @RequestParam("category") String category,
			@RequestParam("file") MultipartFile file) {

		return itemService.fileUpload(file, category, id);

	}

	@GetMapping(value = "/generate-pdf")
	public ResponseEntity<String> generatePdf() {

		return itemService.genrateBill();
	}
	@PostMapping(value = "/item")
	public ResponseEntity<Object> create(@RequestBody ItemPayload itemPayload) {
		return itemService.create(itemPayload);
	}

	@GetMapping(value = "/item/{id}")
	public ResponseEntity<Object> item(@PathVariable String id) {
		return itemService.item(id);
	}

	@GetMapping(value = "/items")
	public ResponseEntity<List<ItemPayload>> items() {
		List<ItemPayload> itemPayloads = itemService.items();
		return new ResponseEntity<List<ItemPayload>>(itemPayloads, HttpStatus.OK);
	}

	@PutMapping(value = "/item/update/{id}")
	public ResponseEntity<Object> update(@RequestBody ItemPayload itemPayload, @PathVariable String id) {
		return itemService.update(itemPayload, id);
	}

	@DeleteMapping(value = "/item/delete/{id}")
	public ResponseEntity<Object> delete(@PathVariable String id) {
		return itemService.delete(id);
	}
	
	  @GetMapping(value = "/item/search")
	    public ResponseEntity<List<ItemPayload>> searchItems(
	            @RequestParam(required = false) String name,
	            @RequestParam(required = false) String description,
	            @RequestParam(required = false) String price,
	            @RequestParam(required = false) Long quantity) {

	        List<ItemPayload> itemPayloads = itemService.searchItems(name, description, price, quantity);
	        return ResponseEntity.ok(itemPayloads);
	    }
	
	
	

}
