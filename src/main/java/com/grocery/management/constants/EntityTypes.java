package com.grocery.management.constants;

public enum EntityTypes {

	ADMIN("Admin", "admin-"), CATEGORY("Category","catg-"), ITEM("Item","item-"), VENDOR("VENDOR", "Vendor-");

	private final String title;
	private final String idPrefix;

	private EntityTypes(String title, String idPrefix) {
		this.title = title;
		this.idPrefix = idPrefix;
	}

	public String getTitle() {
		return this.title;
	}

	public String getIdPrefix() {
		return this.idPrefix;
	}

}