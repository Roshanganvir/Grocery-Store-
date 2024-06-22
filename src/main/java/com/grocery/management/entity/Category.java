package com.grocery.management.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "gm_catg")
public class Category {
	
	@Id
	private String id;
	private String name;
	private String description;
	private List<String>items;
	private LocalDateTime createAt;
	private LocalDateTime updateAt;
	private Boolean isDeleted;


}