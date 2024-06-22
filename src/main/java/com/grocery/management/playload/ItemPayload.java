package com.grocery.management.playload;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemPayload {
	
	private String id;
	private String name;
	private String brand;
	private String category;
	private String description;
	private String mrp;
	private String price;
	private Long quntity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

}
