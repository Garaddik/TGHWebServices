package com.mk.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mk.constants.AppConstants;
import com.mk.constants.Utility;
import com.mk.cryptography.MailUtility;
import com.mk.dao.EventDAO;
import com.mk.dao.EventDAOImpl;
import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;
import com.mk.pojo.Category;
import com.mk.pojo.Event;
import com.mk.pojo.Order;
import com.mk.pojo.Photo;
import com.mk.pojo.Request;
import com.mk.pojo.Response;
import com.mk.pojo.SubCategory;
import com.mk.pojo.User;
import com.sun.jersey.core.header.FormDataContentDisposition;

public class EventServiceImpl implements EventService {

	EventDAO eventDAO = new EventDAOImpl();

	private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);


	public String getevents() throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getEvents");
		String response = null;
		Gson gson = null;
		Response responseObj = null;

		try {

			List<Event> eventList = eventDAO.getEvents();
			gson = new Gson();
			if (null != eventList) {
				responseObj = new Response(200, "SUCCESS", eventList);
			} else {
				responseObj = new Response(304, "No Events Found");
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.info(AppConstants.ERRORMETHOD + " getEvents " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info(AppConstants.ERRORMETHOD + " getEvents " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "getEvents");
		return response;
	}


	public String getCategories(int id) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getCategories");
		String response = null;
		Gson gson = null;
		Response responseObj = null;

		try {
			gson = new Gson();
			if (id != 0) {
				List<Category> catList = eventDAO.getCategories(id);
				if (null != catList) {
					responseObj = new Response(catList, 200, "SUCCESS");
				} else {
					responseObj = new Response(304, "No Categories Found.");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.info(AppConstants.ERRORMETHOD + "getCategories" + " error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info(AppConstants.ERRORMETHOD + "getCategories" + " error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "getCategories");
		return response;
	}

	public String getSubCategories(int eventId, int catId) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getSubCategories");

		String response = null;
		Gson gson = null;
		Response responseObj = null;

		try {
			gson = new Gson();
			if (eventId != 0 || catId != 0) {
				List<SubCategory> subCatList = eventDAO.getSubCategories(eventId, catId);
				Map<String, String> configuration = eventDAO.getConfiguration();

				if (null != subCatList) {
					for (SubCategory category : subCatList) {
						if (null != category.getImage()) {
							category.setImage(configuration.get(AppConstants.DOMAIN_NAME)
									+ configuration.get(AppConstants.TGH_IMAGE_PATH) + category.getImage());
						}
					}
					responseObj = new Response(200, subCatList, "SUCCESS");
				} else {
					responseObj = new Response(304, "No Items Found.");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "getSubCategories");
		return response;
	}

	public String saveRequestedItems(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "saveRequestedItems");
		String response = null;
		Gson gson = null;
		int orderId = 0;
		Response responseObj = null;

		try {

			gson = new Gson();

			if (null != request) {

				Request requestObj = gson.fromJson(request, Request.class);

				/*
				 * List<Item> items = EventServiceHelper.getItems(requestObj);
				 */
				if (null != requestObj.getItemList()) {

					orderId = eventDAO.saveRequestedItems(requestObj.getItemList(), requestObj.getUser());

					if (null != requestObj.getUser()) {
						getOrderedItems(orderId, requestObj.getUser());
					}
					if (orderId > 0) {
						responseObj = new Response(200, "Request sent successfully.");
					} else {
						responseObj = new Response(304, "Unable to Process your request.");
					}

				} else {
					responseObj = new Response(422, "InSufficient Request.");
				}
			} else {
				responseObj = new Response(422, "InSufficient Request.");
			}
			response = gson.toJson(responseObj);

		} catch (NullPointerException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + " SaveRequestedItems");
		return response;
	}

	public String addEvent(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "addEvent");

		String response = null;
		Gson gson = null;
		int status = 0;

		try {

			gson = new Gson();

			Event event = gson.fromJson(request, Event.class);

			if (null != event && null != event.getName()) {

				status = eventDAO.addEvent(event);

				if (status > 0) {
					response = Utility.ValidResponse(200, "SUCCESS");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}

			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}

		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\taddvent\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\taddvent\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "addEvent");
		return response;
	}

	public String addEventCategory(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "addEventCategory");

		String response = null;
		int status = 0;
		Gson gson = null;
		Event event = null;

		try {

			gson = new Gson();
			event = gson.fromJson(request, Event.class);

			if (null != event && event.getId() > 0 && null != event.getCategory()
					&& null != event.getCategory().getName()) {

				status = eventDAO.addEventCategory(event);

				if (status > 0) {
					response = Utility.ValidResponse(200, "SUCCESS");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}

			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t addEventCategory \t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t addEventCategory \t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "addEventCategory");
		return response;
	}
	
	public String addEventSubCategory(String request) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "addEventSubCategory");
		String response = null;
		int status = 0;
		Gson gson = null;
		Event event = null;

		try {

			gson = new Gson();
			event = gson.fromJson(request, Event.class);

			if (null != event && event.getId() > 0 && null != event.getCategory() && event.getCategory().getId() > 0
					&& null != event.getCategory().getSubCat() && null != event.getCategory().getSubCat().getName()) {

				status = eventDAO.addEventSubCategory(event);

				if (status > 0) {
					response = Utility.ValidResponse(200, "SUCCESS");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}

			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}
		} catch (NullPointerException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info("error occurred in Server : " + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}

		logger.info(AppConstants.ENDMETHOD + "addEventSubCategory");
		return response;
	}

	public String getOrderedItems(int orderId, User user) throws TGHException, TGHDBException {
		String body = null;
		try {
			if (orderId > 0) {
				Order order = eventDAO.getOrderedItems(orderId, user.getId());
				body = EventServiceHelper.OrderMail(order);
				System.out.println(body);
				MailUtility.sendMail("mkanytime33@gmail.com", "9964269233", order.getUser().getEmailId(),
						AppConstants.SIGNUP, body);
			}
		} catch (TGHDBException e) {
			logger.info("Error occured while saving");
			throw new TGHException(500, e.getMessage());
		}
		return body;
	}

	public static void main(String[] args) {
		EventServiceImpl service = new EventServiceImpl();

		try {
			String response = service.getSubCategories(86, 23);
			System.out.println(response);
		} catch (Exception e) {

		}
	}

	public String deleteEvent(int id) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deleteEvent");
		String response = null;
		int status = 0;
		try {
			if (id > 0) {
				status = eventDAO.deleteEvent(id);
				if (status > 0) {
					response = Utility.ValidResponse(200, "Event Deleted Successfully.");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}
			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeletevent\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeleteEvent\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteEvent");
		return response;
	}

	public String deleteCategory(int id) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deleteCategory");
		String response = null;
		int status = 0;
		try {
			if (id > 0) {
				status = eventDAO.deleteCategory(id);
				if (status > 0) {
					response = Utility.ValidResponse(200, "Category Deleted Successfully.");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}
			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeleteSubCategory\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeleteSubCategory\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteSubCategory");
		return response;
	}

	public String deleteSubCategory(int id) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "deleteSubCategory");
		String response = null;
		int status = 0;
		try {
			if (id > 0) {
				status = eventDAO.deleteSubCategory(id);
				if (status > 0) {
					response = Utility.ValidResponse(200, "SubCategory Deleted Successfully.");
				} else {
					response = Utility.ValidResponse(304, "Unable to Process your request.");
				}
			} else {
				response = Utility.ValidResponse(422, "InSuffieceint Request");
			}
		} catch (NullPointerException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeleteSubCategory\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.error(AppConstants.ERRORMETHOD + "\tdeleteSubCategory\t" + "500" + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "deleteSubCategory");
		return response;
	}

	public String getGalleryPhotos(int offset) throws TGHException, TGHDBException {

		logger.info(AppConstants.STARTMETHOD + "getGalleryPhotos");
		String response = null;
		Gson gson = null;
		Response responseObj = null;
		try {
			List<Photo> photoList = eventDAO.getGalleryPhotos(offset);
			Map<String, String> configuration = eventDAO.getConfiguration();
			gson = new Gson();
			if (null != photoList && !photoList.isEmpty()) {
				if (null != configuration && configuration.containsKey(AppConstants.DOMAIN_NAME)
						&& configuration.containsKey(AppConstants.TGH_IMAGE_PATH)) {
					for (Photo photo : photoList) {
						photo.setPath(configuration.get(AppConstants.DOMAIN_NAME)
								+ configuration.get(AppConstants.TGH_IMAGE_PATH) + photo.getName());
						logger.debug(configuration.get(AppConstants.DOMAIN_NAME)
								+ configuration.get(AppConstants.TGH_IMAGE_PATH) + photo.getName());
					}
				}
				if (photoList.size() == 10) {
					responseObj = new Response(200, "SUCCESS", photoList, offset + photoList.size(), true);
				} else {
					responseObj = new Response(200, "SUCCESS", photoList, offset + photoList.size(), false);
				}
			} else {
				responseObj = new Response(304, "No More Images", offset, false);
			}
			response = gson.toJson(responseObj);
		} catch (NullPointerException e) {
			logger.info(AppConstants.ERRORMETHOD + " getGalleryPhotos " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (JsonSyntaxException e) {
			logger.info(AppConstants.ERRORMETHOD + " getGalleryPhotos " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + "getGalleryPhotos");
		return response;
	}

	public String subcategoryFileUpload(InputStream inputStream,FormDataContentDisposition fileDetails, Integer subCatId) throws TGHException, TGHDBException {
		OutputStream out = null;
		String response;
		try {

			Map<String, String> configuration = eventDAO.getConfiguration();

			int read = 0;
			byte[] bytes = new byte[1024];
			
			String path = configuration.get(AppConstants.ROOT_PATH)
			+ configuration.get(AppConstants.TGH_IMAGE_PATH) + fileDetails.getFileName();
			
			logger.info("Server Path:  " + path);
			out = new FileOutputStream(new File(path));
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			response = eventDAO.subCategoryImageStore(subCatId,fileDetails.getFileName());
			if(null != response){
				response = Utility.ValidResponse(200, "SUCCESS");
			}
		} catch (FileNotFoundException e) {
			logger.info(AppConstants.ERRORMETHOD + " subcategoryFileUpload " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		} catch (IOException e) {
			logger.info(AppConstants.ERRORMETHOD + " subcategoryFileUpload " + "\t" + e.getMessage());
			throw new TGHException(500, "error occurred in Server : " + e.getMessage());
		}
		return response;
	}
}
