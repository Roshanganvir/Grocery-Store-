package com.grocery.management.entity;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
@Table(name = "gm_item")
public class Item {

	@Id
	private String id;
	private String name;
	private String brand;
	private String category;
	private String Description;
	private String mrp;
	private String price;
	private Long quntity;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Boolean isDeleted;

	public double getPriceAsDouble() {
		Pattern pattern = Pattern.compile("\\d+(\\.\\d+)?");
		Matcher matcher = pattern.matcher(price);
		if (matcher.find()) {
			try {
				return Double.parseDouble(matcher.group());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return 0.0;
	}
}
