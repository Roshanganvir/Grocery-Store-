

package com.grocery.management.entity;

import java.time.LocalDateTime;

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
@Table(name = "gm_admin")
public class Admin {
                              
	@Id
	private String id;
	private String name;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	private LocalDateTime updateAt;
	private Boolean isDeleted;

}
