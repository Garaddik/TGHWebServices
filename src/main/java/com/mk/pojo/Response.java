package com.mk.pojo;

import java.util.List;

public class Response {

	private int status;

	private String message;

	private boolean next;

	private int offset;

	private User user;

	private List<Event> eventList;

	private List<Category> catList;

	private List<SubCategory> subCatList;

	private List<Item> itemList;

	private Integer istemp;

	private List<Photo> photoList;

	public Boolean getNext() {
		return next;
	}

	public void setNext(Boolean next) {
		this.next = next;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public List<Photo> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<Photo> photoList) {
		this.photoList = photoList;
	}

	public Integer getIstemp() {
		return istemp;
	}

	public void setIstemp(Integer istemp) {
		this.istemp = istemp;
	}

	private Boolean type;

	public Boolean getType() {
		return type;
	}

	public void setType(Boolean type) {
		this.type = type;
	}

	public List<Item> getItemList() {
		return itemList;
	}

	public void setItemList(List<Item> itemList) {
		this.itemList = itemList;
	}

	public Response(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public Response(int status, String message, User user) {
		super();
		this.status = status;
		this.message = message;
		this.user = user;
	}

	public Response(int status, String message, List<Event> eventList) {
		super();
		this.status = status;
		this.message = message;
		this.eventList = eventList;
	}

	public Response(List<Category> catList, int status, String message) {
		super();
		this.catList = catList;
		this.status = status;
		this.message = message;
	}

	public Response(int status, List<SubCategory> subCatList, String message) {
		super();
		this.status = status;
		this.subCatList = subCatList;
		this.message = message;
	}

	public Response(int status, String message, List<Item> itemList, boolean type) {
		super();
		this.status = status;
		this.message = message;
		this.itemList = itemList;
		this.type = type;
	}

	public Response(int status, String message, List<Photo> photoList, int offset,boolean next) {
		super();
		this.status = status;
		this.message = message;
		this.photoList = photoList;
		this.offset = offset;
		this.next = next;
	}
	
	public Response(int status, String message,int offset, boolean next) {
		super();
		this.status = status;
		this.message = message;
		this.offset = offset;
		this.next = next;	
	}

	
	public void setNext(boolean next) {
		this.next = next;
	}


	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Event> getEventList() {
		return eventList;
	}

	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}

	public List<Category> getCatList() {
		return catList;
	}

	public void setCatList(List<Category> catList) {
		this.catList = catList;
	}

	public List<SubCategory> getSubCatList() {
		return subCatList;
	}

	public void setSubCatList(List<SubCategory> subCatList) {
		this.subCatList = subCatList;
	}

}
