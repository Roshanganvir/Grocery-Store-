
package com.grocery.management.playload;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AdminPayload {

	private String id;

	private String name;

	private String email;

	private String password;

	private LocalDateTime createdAt;
	private LocalDateTime updateAt;

}