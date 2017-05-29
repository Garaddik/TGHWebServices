package com.mk.dao;

import java.util.List;
import java.util.Map;

import com.mk.exception.TGHDBException;
import com.mk.pojo.Category;
import com.mk.pojo.Event;
import com.mk.pojo.Item;
import com.mk.pojo.Order;
import com.mk.pojo.Photo;
import com.mk.pojo.SubCategory;
import com.mk.pojo.User;

public interface EventDAO {

	public List<Event> getEvents() throws TGHDBException;

	public List<Category> getCategories(int id) throws TGHDBException;
	
	public List<SubCategory> getSubCategories(int eventId,int catId) throws TGHDBException;

	public int saveRequestedItems(List<Item> items,User user) throws TGHDBException;

	public int addEvent(Event event) throws TGHDBException;

	public int addEventCategory(Event event) throws TGHDBException;

	public int addEventSubCategory(Event event) throws TGHDBException;

	public Order getOrderedItems(int orderId,int userId) throws TGHDBException;

	public int deleteEvent(int id) throws TGHDBException;
	
	public int deleteCategory(int id) throws TGHDBException;
	
	public int deleteSubCategory(int id) throws TGHDBException;

	public List<Photo> getGalleryPhotos(int offset) throws TGHDBException;

	public Map<String, String> getConfiguration() throws TGHDBException;

	public String subCategoryImageStore(Integer subCatId,String fileName) throws TGHDBException;

}
