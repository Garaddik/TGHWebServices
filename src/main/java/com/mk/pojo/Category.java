package com.mk.pojo;

import java.util.List;

public class Category {

	private int id;
	private String name;
	private String type;
	private String image;
	private List<SubCategory> subCatList;
	private SubCategory subCat;
	private int eventId;

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public List<SubCategory> getSubCatList() {
		return subCatList;
	}

	public void setSubCatList(List<SubCategory> subCatList) {
		this.subCatList = subCatList;
	}

	public SubCategory getSubCat() {
		return subCat;
	}

	public void setSubCat(SubCategory subCat) {
		this.subCat = subCat;
	}

}
