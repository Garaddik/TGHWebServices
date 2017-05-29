package com.mk.controller;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mk.constants.AppConstants;
import com.mk.constants.Utility;
import com.mk.exception.TGHDBException;
import com.mk.exception.TGHException;
import com.mk.service.EventService;
import com.mk.service.EventServiceImpl;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

@Path("/events")
public class EventControllerImpl {

	EventService service = new EventServiceImpl();

	private final Logger logger = LoggerFactory.getLogger(EventControllerImpl.class);

	@GET
	@Path("/")
	@Produces(MediaType.APPLICATION_JSON)
	public String getEvents() {

		logger.info(AppConstants.STARTMETHOD + "getEvents");
		String response = null;

		try {
			response = service.getevents();

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEvents" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEvents" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "getEvents");
		return response;
	}
	@GET
	@Path("/{id}/category/{catid}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSubCategories(@PathParam("id")int eventId,@PathParam("catid") int catId) {

		logger.info(AppConstants.STARTMETHOD + "getSubCategories");
		logger.info("EventId : " + eventId + "  catId : " + catId);
		String response = null;

		try {
			response = service.getSubCategories(eventId, catId);

		} catch (TGHException e) {
			logger.info(
					AppConstants.ERRORMETHOD + "getEventSubCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(
					AppConstants.ERRORMETHOD + "getEventSubCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "getSubCategories");

		return response;
	}
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getCategories(@PathParam("id")int id) {

		logger.info(AppConstants.STARTMETHOD + "getCategories");
		logger.info(id + "");
		String response = null;

		try {

			response = service.getCategories(id);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEventCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEventCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "getCategories");
		return response;
	}

	@POST
	@Path("/save")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String saveRequestedItems(String request) {

		logger.info(AppConstants.STARTMETHOD + "saveRequestedItems");
		logger.info(request);
		String response = null;
		try {

			response = service.saveRequestedItems(request);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "saveRequesteditems" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "getEventCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "saveRequestedItems");
		return response;
	}

	@POST
	@Path("/event")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addEvent(String request) {

		logger.info(AppConstants.STARTMETHOD + "addEvent");
		logger.info(request);

		String response = null;
		try {

			response = service.addEvent(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\taddvent\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\taddvent\t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}

		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "addEvent");

		return response;
	}

	@POST
	@Path("/event/category")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addCategory(String request) {

		logger.info(AppConstants.STARTMETHOD + "addCategory");
		logger.info(request);

		String response = null;
		try {

			response = service.addEventCategory(request);

		} catch (TGHException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t addCategory \t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.error(AppConstants.ERRORMETHOD + "\t addCategory \t" + e.getStatus() + "\t" + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "addCategory");
		return response;
	}

	@POST
	@Path("/event/category/subcategory")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String addSubCategory(String request) {
		logger.info(AppConstants.STARTMETHOD);
		logger.info(request);
		String response = null;
		try {

			response = service.addEventSubCategory(request);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "addSubCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "addSubCategories" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "addSubCategories");
		return response;
	}


	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteEvent(@PathParam("id")int id) {

		logger.info(AppConstants.STARTMETHOD + "deleteEvent");
		logger.info("Delete Event Id :" + id);
		String response = null;

		try {
			response = service.deleteEvent(id);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "deleteEvent" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "DeleteEvent" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "deleteEvent");
		return response;
	}

	@DELETE
	@Path("/category/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteCategory(@PathParam("id")int id) {

		logger.info(AppConstants.STARTMETHOD + "deleteCategory");
		logger.info("Delete Category Id :" + id);
		String response = null;

		try {
			response = service.deleteCategory(id);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "deleteCategory" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "DeleteCategory" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "deleteCategory");
		return response;
	}

	@DELETE
	@Path("/subcategory/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String deleteSubCategory(@PathParam("id")int id) {
		logger.info(AppConstants.STARTMETHOD + "deleteSubCategory");
		logger.info("Delete SubCategory Id :" + id);
		String response = null;

		try {
			response = service.deleteSubCategory(id);

		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "deleteSubCategory" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "DeleteSubCategory" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "deleteSubCategory");
		return response;
	}

	@GET
	@Path("/gallery/{offset}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getGalleryPhotos(@PathParam("offset")int offset) {
		logger.info(AppConstants.STARTMETHOD + "getGalleryPhotos");
		logger.info("SubCategory Id :" + offset);
		String response = null;
		try {
			response = service.getGalleryPhotos(offset);
		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "getGalleryPhotos" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "getGalleryPhotos" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info(response);
		logger.info(AppConstants.ENDMETHOD + "getGalleryPhotos");
		return response;
	}

	@POST
	@Path("/file/{subcatid}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public String subcategoryFileUpload(@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @PathParam("subcatid") String subCatId){
		logger.info("zipFile start");
		logger.info("File name" + fileDetail.getFileName() + "Sub Category ID " + subCatId);
		String response = null;
		try {
			response = service.subcategoryFileUpload(inputStream,fileDetail, Integer.parseInt(subCatId));
		} catch (TGHException e) {
			logger.info(AppConstants.ERRORMETHOD + "subcategoryFileUpload" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		} catch (TGHDBException e) {
			logger.info(AppConstants.ERRORMETHOD + "subcategoryFileUpload" + "\t" + e.getStatus() + e.getErrorMessage());
			response = Utility.ErrorResponse(e.getStatus(), e.getErrorMessage());
		}
		logger.info("zipFile end");
		return response;
	}
}
