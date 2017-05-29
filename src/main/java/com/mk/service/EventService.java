package com.mk.service;

import java.io.InputStream;

import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;
import com.mk.pojo.User;
import com.sun.jersey.core.header.FormDataContentDisposition;

public interface EventService {

	String getevents() throws TGHException,TGHDBException;
	
	String getCategories(int id) throws TGHException,TGHDBException;
	
	String getSubCategories(int eventId,int catId) throws TGHException,TGHDBException;
	
	String saveRequestedItems(String request) throws TGHException,TGHDBException;

	String addEvent(String request) throws TGHException,TGHDBException;

	String addEventCategory(String request) throws TGHException,TGHDBException;

	String addEventSubCategory(String request) throws TGHException,TGHDBException;

	String getOrderedItems(int id,User user) throws TGHException,TGHDBException;

	String deleteEvent(int id) throws TGHException,TGHDBException;
	
	String deleteCategory(int id) throws TGHException,TGHDBException;
	
	String deleteSubCategory(int id) throws TGHException,TGHDBException;

	String getGalleryPhotos(int offset) throws TGHException,TGHDBException;

	String subcategoryFileUpload(InputStream inputStream,FormDataContentDisposition fileDetails, Integer subCatId) throws TGHException,TGHDBException;
	
}
