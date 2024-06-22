package com.grocery.management.playload;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryPayload {
	
	private String id;
	private String name;
	private String description;
	private List<String>items;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
}