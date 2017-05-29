package com.mk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.constants.Utility;
import com.mk.exception.TGHDBException;
import com.mk.pojo.Category;
import com.mk.pojo.Event;
import com.mk.pojo.Item;
import com.mk.pojo.Order;
import com.mk.pojo.Photo;
import com.mk.pojo.SubCategory;
import com.mk.pojo.User;

public class EventDAOImpl implements EventDAO {

	private static final Logger logger = LoggerFactory.getLogger(EventDAOImpl.class);

	public List<Event> getEvents() throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getEvents");
		List<Event> eventList = null;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		boolean isNotlistInstanceCreated = true;
		try {
			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "SELECT * FROM EVENT where active = 1";

				statement = connection.prepareStatement(sqlQuery);

				ResultSet rs = statement.executeQuery();

				while (rs.next()) {

					if (isNotlistInstanceCreated) {
						eventList = new ArrayList<Event>();
						isNotlistInstanceCreated = false;
					}
					Event event = new Event();
					event.setId(rs.getInt(1));
					event.setName(rs.getString(2));
					event.setImage(rs.getString(3));
					event.setType(rs.getString(4));

					eventList.add(event);
				}
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEvents");
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "getEvents");
		return eventList;
	}

	@Override
	public List<Category> getCategories(int id) throws TGHDBException {
		List<Category> catList = null;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		boolean isNotlistInstanceCreated = true;
		try {
			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "SELECT * FROM CATEGORY where eventid = ? and active = 1";

				statement = connection.prepareStatement(sqlQuery);
				statement.setInt(1, id);

				ResultSet rs = statement.executeQuery();

				while (rs.next()) {

					if (isNotlistInstanceCreated) {
						catList = new ArrayList<Category>();
						isNotlistInstanceCreated = false;
					}
					Category category = new Category();
					category.setId(rs.getInt(1));
					category.setName(rs.getString(2));
					category.setImage(rs.getString(3));
					category.setType(rs.getString(4));

					catList.add(category);
				}
			}
		} catch (SQLException e) {
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}

		return catList;
	}

	@Override
	public List<SubCategory> getSubCategories(int eventId, int catId) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "getSubCategories");
		List<SubCategory> subCatList = null;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		boolean isNotlistInstanceCreated = true;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "SELECT id,name,image,price,quantity,image FROM SUBCATEGORY where catId = ? and active = 1";
				statement = connection.prepareStatement(sqlQuery);
				statement.setInt(1, catId);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					if (isNotlistInstanceCreated) {
						subCatList = new ArrayList<SubCategory>();
						isNotlistInstanceCreated = false;
					}
					
					SubCategory subCategory = new SubCategory();
					subCategory.setId(rs.getInt(1));
					subCategory.setName(rs.getString(2));
					subCategory.setImage(rs.getString(3));
					subCategory.setPrice(rs.getString(4));
					subCategory.setQuantity(rs.getString(5));
					subCategory.setImage(rs.getString(6));
					subCatList.add(subCategory);
				}
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + " getSubCategories " + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + " getSubCategories");
		return subCatList;
	}

	@Override
	public int saveRequestedItems(List<Item> items, User user) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "saveRequestedItems");
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		int orderId = 0;

		try {
			connection = Utility.getConnection();
			date = new Date();
			if (null != connection) {

				sqlQuery = "INSERT INTO ORDER_ITEM(userId,createdDate) VALUES(?,?)";
				statement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, user.getId());
				statement.setString(2, date.toString());

				connection.setAutoCommit(false);

				statement.execute();
				ResultSet id = statement.getGeneratedKeys();

				if (id.next()) {
					orderId = id.getInt(1);
				}
				logger.info("Order Id Generated : " + orderId + "UserMailID : " + user.getId());

				sqlQuery = "INSERT INTO ITEM(orderid,eventid,catid,subcatid,createdDate,description,name,active) VALUES(?,?,?,?,?,?,?,?)";
				statement = connection.prepareStatement(sqlQuery);

				for (Item item : items) {
					statement.setInt(1, orderId);
					statement.setInt(2, item.getEventId());
					statement.setInt(3, item.getCatId());
					statement.setInt(4, item.getSubcatId());
					statement.setString(5, date.toString());
					statement.setString(6, item.getDescription());
					statement.setString(7, item.getName());
					statement.setInt(8, 1);
					statement.addBatch();
				}

				statement.executeBatch();
				connection.commit();
				connection.close();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + " saveRequestedItems" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "saveRequestedItems");
		return orderId;
	}

	@Override
	public int addEvent(Event event) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "addEvent");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "INSERT INTO EVENT(name,createdDate,active) VALUES(?,?,1)";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, event.getName());
				statement.setString(2, date.toString());
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "\t addEvent \t" + " Database Error occured : " + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "addEvent");
		return status;
	}

	@Override
	public int addEventCategory(Event event) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "addEventCategory");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "INSERT INTO CATEGORY(name,createdDate,eventid,active) VALUES(?,?,?,1)";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, event.getCategory().getName());
				statement.setString(2, date.toString());
				statement.setInt(3, event.getId());
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "\t addEventCategory \t" + " Database Error occured : "
					+ e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "addEventCategory");
		return status;
	}

	@Override
	public int addEventSubCategory(Event event) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "addEventSubCategories");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "INSERT INTO SUBCATEGORY(name,createdDate,eventid,catid,quantity,price,active) VALUES(?,?,?,?,?,?,1)";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, event.getCategory().getSubCat().getName());
				statement.setString(2, date.toString());
				statement.setInt(3, event.getId());
				statement.setInt(4, event.getCategory().getId());
				statement.setString(5, event.getCategory().getSubCat().getQuantity());
				statement.setString(6, event.getCategory().getSubCat().getPrice());
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.error(AppConstants.ERRORMETHOD + "addEventSubCategory" + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "addEventSubCategory");
		return status;
	}

	@Override
	public Order getOrderedItems(int orderId, int userId) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getOrderItems");
		logger.info("Generated Order Number : " + orderId + " and user information : ");
		Set<Integer> eventIds = null;
		Set<Integer> categoryIds = null;
		Set<Integer> subcategoryIds = null;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		ResultSet rs = null;
		Order order = null;
		List<Event> eventList = null;
		List<Category> categoryList = null;
		List<SubCategory> subCategoryList = null;
		User user = null;

		try {

			connection = Utility.getConnection();

			if (null != connection) {

				sqlQuery = "SELECT eventid,catid,subcatid FROM ITEM where orderId = ? and active = 1";
				logger.info("SQL  Query : " + sqlQuery);
				statement = connection.prepareStatement(sqlQuery);
				statement.setInt(1, orderId);
				rs = statement.executeQuery();
				eventIds = new HashSet<Integer>();
				categoryIds = new HashSet<Integer>();
				subcategoryIds = new HashSet<Integer>();

				while (rs.next()) {
					eventIds.add(rs.getInt(1));
					categoryIds.add(rs.getInt(2));
					subcategoryIds.add(rs.getInt(3));
				}

				sqlQuery = "SELECT id,Name FROM Event where id in ( "
						+ eventIds.toString().replace("[", "").replace("]", "") + ") and active = 1";
				logger.info("SQL  Query : " + sqlQuery);
				statement = connection.prepareStatement(sqlQuery);
				rs = statement.executeQuery();
				eventList = new ArrayList<Event>();

				while (rs.next()) {
					Event event = new Event();
					event.setId(rs.getInt(1));
					event.setName(rs.getString(2));
					eventList.add(event);
				}

				sqlQuery = "SELECT id,Name,eventid FROM Category where id in ( "
						+ categoryIds.toString().replace("[", "").replace("]", "") + ") and active = 1";
				logger.info("SQL  Query : " + sqlQuery);
				statement = connection.prepareStatement(sqlQuery);
				rs = statement.executeQuery();
				categoryList = new ArrayList<Category>();

				while (rs.next()) {

					Category category = new Category();
					category.setId(rs.getInt(1));
					category.setName(rs.getString(2));
					category.setEventId(rs.getInt(3));
					categoryList.add(category);

				}

				sqlQuery = "SELECT id,Name,catid FROM SubCategory where id in ("
						+ subcategoryIds.toString().replace("[", "").replace("]", "") + ") and active = 1";
				logger.info("SQL  Query : " + sqlQuery);
				statement = connection.prepareStatement(sqlQuery);
				rs = statement.executeQuery();

				subCategoryList = new ArrayList<SubCategory>();

				while (rs.next()) {
					SubCategory subCategory = new SubCategory();
					subCategory.setId(rs.getInt(1));
					subCategory.setName(rs.getString(2));
					subCategory.setCatId(rs.getInt(3));
					subCategoryList.add(subCategory);
				}
				sqlQuery = "SELECT userName,emailId,phoneNumber,address from user where id = ?";
				logger.info("User SQL  Query : " + sqlQuery);
				statement = connection.prepareStatement(sqlQuery);
				statement.setInt(1, userId);
				rs = statement.executeQuery();

				if (rs.next()) {
					user = new User();
					user.setUserName(rs.getString(1));
					user.setEmailId(rs.getString(2));
					user.setPhoneNumber(rs.getString(3));
					user.setAddress(rs.getString(4));
				}
			}
			order = new Order();
			order.setEventList(eventList);
			order.setCategoryList(categoryList);
			order.setSubCategoryList(subCategoryList);
			order.setUser(user);

		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "getOrderedItems");
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		return order;
	}

	public static void main(String[] args) {

		EventDAO dao = new EventDAOImpl();
		List<Item> items = new ArrayList<Item>();
		Item item = new Item();
		item.setCatId(1);
		item.setDescription("description");
		item.setEventId(1);
		item.setSubcatId(1);
		item.setName("welcome");

		items.add(item);
		items.add(item);
		User user = new User();
		user.setId(1);

		try {
			int status = dao.saveRequestedItems(items, user);
			System.out.println(status);
		} catch (TGHDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public int deleteEvent(int id) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deleteEvent");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "update event set active = 0,deletedDate = ? where id = ?";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, date.toString());
				statement.setInt(2, id);
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "\t deleteEvent \t" + " Database Error occured : " + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteEvent");
		return status;
	}
	
	@Override
	public int deleteCategory(int id) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deleteCategory");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "update category set active = 0,deletedDate = ? where id = ?";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, date.toString());
				statement.setInt(2, id);
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "\t deleteCategory \t" + " Database Error occured : " + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteCategory");
		return status;
	}
	
	@Override
	public int deleteSubCategory(int id) throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deletesubCategory");
		int status = 0;
		Connection connection = null;
		PreparedStatement statement = null;
		String sqlQuery = null;
		Date date = null;
		try {
			connection = Utility.getConnection();
			if (null != connection) {
				sqlQuery = "update subcategory set active = 0,deletedDate = ? where id = ?";
				statement = connection.prepareStatement(sqlQuery);
				connection.setAutoCommit(false);
				date = new Date();
				statement.setString(1, date.toString());
				statement.setInt(2, id);
				status = statement.executeUpdate();
				connection.commit();
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "\t deleteSubCategory \t" + " Database Error occured : " + e.getMessage());
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteSubCategory");
		return status;
	}

	@Override
	public List<Photo> getGalleryPhotos(int offset) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "getGalleryPhotos");
		List<Photo> photoList = null;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		boolean isNotlistInstanceCreated = true;
		try {
			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "SELECT * FROM Photo where active = 1 LIMIT "+offset +",10";
				statement = connection.prepareStatement(sqlQuery);
				ResultSet rs = statement.executeQuery();
				while (rs.next()) {
					if (isNotlistInstanceCreated) {
						photoList = new ArrayList<Photo>();
						isNotlistInstanceCreated = false;
					}
					Photo photo = new Photo();
					photo.setId(rs.getInt(1));
					photo.setName(rs.getString(2));
					photoList.add(photo);
				}
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "getGalleryPhotos");
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "getGalleryPhotos");
		return photoList;

	}

	@Override
	public Map<String, String> getConfiguration() throws TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getConfiguration");
		Map<String,String> configMap = null;;
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		
		try {
			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "SELECT config_key,config_value FROM Config where active = 1";
				statement = connection.prepareStatement(sqlQuery);
				ResultSet rs = statement.executeQuery();
				configMap = new HashMap<String,String>();
				
				while (rs.next()) {
					configMap.put(rs.getString(1), rs.getString(2));
				}
			}
		
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "getConfiguration");
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "getConfiguration");
		return configMap;
	}

	@Override
	public String subCategoryImageStore(Integer subCatId,String fileName) throws TGHDBException {
		logger.info(AppConstants.STARTMETHOD + "subCategoryImageStore");
		PreparedStatement statement = null;
		Connection connection = null;
		String sqlQuery = null;
		String response = null; 
		try {
			connection = Utility.getConnection();

			if (null != connection) {
				sqlQuery = "update subcategory set image = ? where id = ?";
				statement = connection.prepareStatement(sqlQuery);
				statement.setString(1, fileName);
				statement.setInt(2, subCatId);
				int rs = statement.executeUpdate();
				if( rs > 0){
					response = "SUCCESS";
				}
			}
		} catch (SQLException e) {
			logger.info(AppConstants.ERRORMETHOD + "subCategoryImageStore");
			throw new TGHDBException(503, "Database Error occured : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "subCategoryImageStore");
		return response;

		
	}
	
}
