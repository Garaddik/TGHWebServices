package com.mk.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.mk.constants.AppConstants;
import com.mk.exception.TGHException;
import com.mk.pojo.Category;
import com.mk.pojo.Event;
import com.mk.pojo.Item;
import com.mk.pojo.Order;
import com.mk.pojo.Request;
import com.mk.pojo.SubCategory;

public class EventServiceHelper {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceHelper.class);

	public static List<Item> getItems(Request requestObj) throws TGHException {
		logger.info(AppConstants.STARTMETHOD + " : getItems");
		List<Item> itemList = null;
		try {
			itemList = new ArrayList<Item>();
			Item item = null;
			if (null != requestObj && null != requestObj.getUser() && null != requestObj.getEventList()) {

				for (Event event : requestObj.getEventList()) {

					if (null != event && null != event.getCatList()) {

						for (Category category : event.getCatList()) {

							if (null != category && null != category.getSubCatList()) {

								for (SubCategory subCat : category.getSubCatList()) {
									if (null != subCat) {
										item = new Item();
										item.setUserId(requestObj.getUser().getId());
										item.setEventId(event.getId());
										item.setCatId(category.getId());
										item.setSubcatId(subCat.getId());
										itemList.add(item);
									}
								}
							}
						}
					}
				}
			}
			if (itemList.isEmpty()) {
				itemList = null;
			}
		} catch (NullPointerException npe) {
			logger.info(AppConstants.ERRORMETHOD + "getItems : error message " + npe.getMessage());
			throw new TGHException(500, npe.getMessage());
		}
		logger.info(AppConstants.ENDMETHOD + " getItems");
		return itemList;
	}

	public static void main(String[] args) {
		String json = "{\"id\":147,\"eventList\":[{\"id\":10,\"catList\" : [{\"id\":2,\"subCatList\" : [{\"id\":3}	]}]}]}";
		Gson gson = new Gson();
		Request request = gson.fromJson(json, Request.class);
		try {
			getItems(request);
		} catch (TGHException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String OrderMail(Order order) {
		
		logger.info(AppConstants.STARTMETHOD + "OrderMail");
			
		StringBuffer buffer = new StringBuffer();
		
		boolean isFirstSub = true;
		buffer.append("<html>");
		buffer.append("<body>");
		buffer.append("<p>Hi Support team,</p>");
		buffer.append("<p style=\"margin-left:20px;\">Please find the Below Order Items</p>");
		buffer.append("<table style=\"border:1px solid black;\">");
		buffer.append("<tr>");
		buffer.append("<th>Event Name</th>");
		buffer.append("<th>Categories</th>");
		buffer.append("<th>Items</th>");
		buffer.append("</tr>");
		
		for(Event event : order.getEventList()){
			buffer.append("<tr>");
			for(Category category : order.getCategoryList()){
				if( event.getId() == category.getEventId()){
					isFirstSub = true;
					String subCats = "";
					buffer.append("<td>");
					buffer.append(event.getName());
					buffer.append("</td>");
					buffer.append("<td>");
					buffer.append(category.getName());
					buffer.append("</td>");
					for( SubCategory subCat : order.getSubCategoryList() ){
						if( subCat.getCatId() == category.getId()){
							if(isFirstSub){
								subCats = subCat.getName();
								isFirstSub = false;
							}else{
								subCats = subCats +", " +subCat.getName();
							}
						}
					}
					buffer.append("<td>");
					buffer.append(subCats);
					buffer.append("</td>");
					buffer.append("</tr>");
				}
			}
		}
		
		buffer.append("</table>");
		
		buffer.append("<p></p>");
		buffer.append("<p>User Name : " + order.getUser().getUserName()+"</p>");
		buffer.append("<p>EmailId : " + order.getUser().getEmailId()+"</p>");
		buffer.append("<p>Phone Number : " +order.getUser().getPhoneNumber()+"</p>");
		buffer.append("Address : " + order.getUser().getAddress());
		buffer.append("");
		buffer.append("<p>Thanks</p>");
		buffer.append("<p>"+order.getUser().getUserName()+"</p>");
		buffer.append("</body>");
		buffer.append("</html>");
		
		logger.info(AppConstants.ENDMETHOD + "OrderMail");
	
		return buffer.toString();
	}
	
public static String formatedBody(Order order){
		
		logger.info(AppConstants.STARTMETHOD + " formated Body");
		
		StringBuffer buffer = new StringBuffer();
		boolean isFirstSub = true;
		
		buffer.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
		buffer.append("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		buffer.append("<head>");
		buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
		buffer.append("<title>EVENT</title>");
		buffer.append("<style type=\"text/css\">");
		buffer.append("body {");
		buffer.append("table {border-collapse: collapse;width: 100%;}");
		buffer.append("th, td {text-align: left;padding: 15px;}");
		buffer.append(".headcontent_less{padding-left:4%;padding-right:10%;}");
		buffer.append("tr:nth-child(even){background-color: #f2f2f2;}");
		buffer.append("padding-top: 0 !important;padding-bottom: 0 !important;padding-top: 0 !important;padding-bottom: 0 !important;margin:0 !important;width: 100% !important;-webkit-text-size-adjust: 100% !important;-ms-text-size-adjust: 100% !important;-webkit-font-smoothing: antialiased !important;}");
		buffer.append(".tableContent img {border: 0 !important;display: inline-block !important;outline: none !important;}");
		buffer.append("a{color:#382F2E;}");
		buffer.append("p, h1,h2,ul,ol,li,div{margin:0;padding:0;}");
		buffer.append("h1,h2{font-weight: normal;background:transparent !important;border:none !important;}");
		buffer.append(".contentEditable h2.big{font-size: 30px !important;}");
		buffer.append(".contentEditable h2.bigger{font-size: 37px !important;}");
		buffer.append("td,table{vertical-align: top;}");
		buffer.append("td.middle{vertical-align: middle;}");
		buffer.append("a.link1{font-size:13px;color:#B791BF;text-decoration:none;}");
		buffer.append(".link2{font-size:13px;color:#ffffff;text-decoration:none;line-height:19px;font-family: Helvetica;}");
		buffer.append(".link3{color:#FBEFFE;text-decoration: none;}");
		buffer.append(".contentEditable li{margin-top:10px;margin-bottom:10px;list-style: none;color:#ffffff;text-align:center;font-size:13px;line-height:19px;}");
		buffer.append(".appart p{font-size:13px;line-height:19px;color:#aaaaaa !important;}");
		buffer.append(".bgBody{background:url(https://i.screenshot.net/73dlktg) no-repeat left top;}");
		buffer.append(".bgItem{background:#fd0002;}");
		buffer.append("</style>");
		buffer.append("<script type=\"colorScheme\" class=\"swatch active\">{\"name\":\"Default\",\"bgBody\":\"CFB4D5\",\"link\":\"B791BF\",\"color\":\"ffffff\",\"bgItem\":\"CFB4D5\",\"title\":\"ffffff\"}</script>");
		buffer.append("</head>");
		
		
		buffer.append("<body paddingwidth=\"0\" paddingheight=\"0\" class='bgBody' style=\"padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;\" offset=\"0\" toppadding=\"0\" leftpadding=\"0\"> <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tableContent bgBody\" align=\"center\" style='font-family:Georgia, serif;'>");
		
		buffer.append("<tr> <td width='660' align='center' > <table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"bgItem\" style=\"border:2px double #fff;;\">");
		
		buffer.append("<tr> <td align='center' width='660' class='movableContentContainer'>");
		buffer.append("<div class='movableContent'> <table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" > <tr> <td align='center'> <div class=\"contentEditableContainer contentImageEditable\"> <div class=\"contentEditable\" > <img src=\"http://information.edmition.com/wp-content/uploads/2016/02/bng-events.jpg\" alt='' data-default=\"placeholder\" data-max-width=\"660\" width='660' height='250' > </div> </div> </td> </tr> </table> </div>");
		
		buffer.append("<div class='movableContent'> <table width=\"667\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> <tr><td height='30' colspan='3'></td></tr> <tr> <td class='middle' width='100'><div style='border-top:1px solid #EBCDF1'></div></td> <td> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#ffffff;text-align:center;font-family:Baskerville;'> <h2 class='bigger'>Event Management</h2> </div> </div> </td> <td class='middle' width='100'><div style='border-top:1px solid #EBCDF1'></div></td> </tr> <tr><td height='30' colspan='3'></td></tr> <tr> <td width='80'></td> <td align='left'> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#ffffff;font-size:13px;line-height:19px;'> <div> Hi Team,<br /> <br />");
		
		buffer.append("<h2 style=\"font-size:24px; color:#A5CA78;\">Requested User Information</h2><hr><br />");
		buffer.append("<strong style=\"color:#A5CA78;\">User Name</strong> :");
		buffer.append(order.getUser().getUserName());
		
		buffer.append("<br />");
		
		buffer.append("<strong style=\"color:#A5CA78;\">EmailId</strong> :");
		buffer.append(order.getUser().getEmailId()+"<br />");
		buffer.append("<strong style=\"color:#A5CA78;\">Phone Number</strong> : ");
		buffer.append( order.getUser().getPhoneNumber()+ "<br /><br />");
		buffer.append("<strong style=\"color:#A5CA78;\">Address</strong> :<br />");
		buffer.append(order.getUser().getAddress());
		
		buffer.append("<p > Please find the Below Order Items</p><br />");
		
		buffer.append("<tr> <th style=\"padding:5px 30px;\">Event Name</th> <th style=\"padding:5px 30px;\">Categories</th> <th style=\"padding:5px 30px;\">Items</th> </tr>");
		
		buffer.append("<table border=\"2px solid #fff;\">");
		
		for(Event event : order.getEventList()){
			buffer.append("<tr>");
			for(Category category : order.getCategoryList()){
				if( event.getId() == category.getEventId()){
					isFirstSub = true;
					String subCats = "";
					buffer.append("<td style=\"padding:5px 30px;\">");
					buffer.append(event.getName());
					buffer.append("</td>");
					buffer.append("<td style=\"padding:5px 30px;\">");
					buffer.append(category.getName());
					buffer.append("</td>");
					for( SubCategory subCat : order.getSubCategoryList() ){
						if( subCat.getCatId() == category.getId()){
							if(isFirstSub){
								subCats = subCat.getName();
								isFirstSub = false;
							}else{
								subCats = subCats +", " +subCat.getName();
							}
						}
					}
					buffer.append("<td style=\"padding:5px 30px;\">");
					buffer.append(subCats);
					buffer.append("</td>");
					buffer.append("</tr>");
				}
			}
		}
		
		buffer.append("</table><br />");
		buffer.append("<br />");
		buffer.append("<div> <p>Thanks,</p> <p>Mail Support team</p> </div> </div> </div> </td><br /><br /> <a target='_blank' href=\"#\" class='link2' style='color:#ffffff;background:#FD0002;padding:10px 18px;border-radius:40px;-moz-border-radius:40px;-webkit-border-radius:40px; border:1px #fff solid;'>Get In Touch</a> <td width='80'></td> </tr> </table> </div> <div class='movableContent'> <table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> <tr><td height='75'></td></tr> <tr><td><div style='border-top:1px solid #EBCDF2;'></div></td></tr> <tr><td height='25'></td></tr> <tr> <td> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#FBEFFE;text-align:center;font-size:13px;line-height:19px;'> <p>[Event Management] <br> [jp nagar, 1st phase, bengaluru - 560078] <br> [+91 1234567890] <br> </p><br /> <a href=\"#\"><img src=\"http://maximolaura.yx0se0rk8.netdna-cdn.com/wp-content/uploads/2016/05/facebook-icon.png\" width=\"34\" height=\"33\" alt=\"\" style=\"padding-right:5px;\"/></a> <a href=\"#\"><img src=\"http://findicons.com/files/icons/2788/circle/128/youtube.png\" width=\"34\" height=\"34\" alt=\"\" style=\"padding-right:5px;\"/></a> <a href=\"#\"><img src=\"http://www.unitapastoralesancataldo.it/images/twitter_icono.png\" width=\"34\" height=\"33\" alt=\"\"/></a> </div> </div> </td> </tr> </table> </div> </td> </tr> <tr><td height='20'></td></tr> </table> </td> </tr> </table> </body> </html>");
		
		return buffer.toString();
	}
	
	public static  String orderBody(Order order){
		StringBuffer buff = new StringBuffer();
		buff.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"> <html xmlns=\"http://www.w3.org/1999/xhtml\"> <head> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> <title>EVENT</title> <style type=\"text/css\"> body { table { border-collapse: collapse; width: 100%; } th, td { text-align: left; padding: 15px; } .headcontent_less{ padding-left:4%; padding-right:10%; } tr:nth-child(even){ background-color: #f2f2f2; } padding-top: 0 !important; padding-bottom: 0 !important; padding-top: 0 !important; padding-bottom: 0 !important; margin:0 !important; width: 100% !important; -webkit-text-size-adjust: 100% !important; -ms-text-size-adjust: 100% !important; -webkit-font-smoothing: antialiased !important; } .tableContent img { border: 0 !important; display: inline-block !important; outline: none !important; } a{ color:#382F2E; } p, h1,h2,ul,ol,li,div{ margin:0; padding:0; } h1,h2{ font-weight: normal; background:transparent !important; border:none !important; } .contentEditable h2.big{ font-size: 30px !important; } .contentEditable h2.bigger{ font-size: 37px !important; } td,table{ vertical-align: top; } td.middle{ vertical-align: middle; } a.link1{ font-size:13px; color:#B791BF; text-decoration:none; } .link2{ font-size:13px; color:#ffffff; text-decoration:none; line-height:19px; font-family: Helvetica; } .link3{ color:#FBEFFE; text-decoration: none; } .contentEditable li{ margin-top:10px; margin-bottom:10px; list-style: none; color:#ffffff; text-align:center; font-size:13px; line-height:19px; } .appart p{ font-size:13px; line-height:19px; color:#aaaaaa !important; } .bgBody{background:url(http://prescottevents.net/wp-content/uploads/2016/05/full-slide1.jpg) no-repeat left top;} .bgItem{background:#fd0002;} </style> <script type=\"colorScheme\" class=\"swatch active\"> { \"name\":\"Default\", \"bgBody\":\"CFB4D5\", \"link\":\"B791BF\", \"color\":\"ffffff\", \"bgItem\":\"CFB4D5\", \"title\":\"ffffff\" } </script> </head> <body paddingwidth=\"0\" paddingheight=\"0\" class='bgBody' style=\"padding-top: 0; padding-bottom: 0; padding-top: 0; padding-bottom: 0; background-repeat: repeat; width: 100% !important; -webkit-text-size-adjust: 100%; -ms-text-size-adjust: 100%; -webkit-font-smoothing: antialiased;\" offset=\"0\" toppadding=\"0\" leftpadding=\"0\"> <table width=\"100%\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" class=\"tableContent bgBody\" align=\"center\" style='font-family:Georgia, serif;'> <!-- =============== START HEADER =============== --> <tr> <td width='660' align='center' > <table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" class=\"bgItem\" style=\"border:2px double #fff;;\"> <tr> <!-- =============== END HEADER =============== --> <!-- =============== START BODY =============== --> <td align='center' width='660' class='movableContentContainer'> <div class='movableContent'> <table width=\"660\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" > <tr> <td align='center'> <div class=\"contentEditableContainer contentImageEditable\"> <div class=\"contentEditable\" > <img src=\"http://information.edmition.com/wp-content/uploads/2016/02/bng-events.jpg\" alt='' data-default=\"placeholder\" data-max-width=\"660\" width='660' height='250' > </div> </div> </td> </tr> </table> </div> <div class='movableContent'> <table width=\"667\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> <tr><td height='30' colspan='3'></td></tr> <tr> <td class='middle' width='100'><div style='border-top:1px solid #EBCDF1'></div></td> <td> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#ffffff;text-align:center;font-family:Baskerville;'> <h2 class='bigger'>Event Management</h2> </div> </div> </td> <td class='middle' width='100'><div style='border-top:1px solid #EBCDF1'></div></td> </tr> <tr><td height='30' colspan='3'></td></tr> <tr> <td width='80'></td> <td align='left'> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#ffffff;font-size:13px;line-height:19px;'> <div> Hi Team,<br /> <br /> <h2 style=\"font-size:24px; color:#A5CA78;\">Requested User Information</h2> <hr><br /> <strong style=\"color:#A5CA78;\">User Name</strong> : Kirankumar Garaddi<br /> <strong style=\"color:#A5CA78;\">EmailId</strong> : kiran@gmail.com<br /> <strong style=\"color:#A5CA78;\">Phone Number</strong> : 8553887424<br /><br /> <strong style=\"color:#A5CA78;\">Address</strong> :<br /> 24th Main cross <br /> J P Nagar Phase 1st<br /> Bangalore - 560071<br /><br /> <p > Please find the Below Order Items</p><br /> <table border=\"2px solid #fff;\"> <tr> <th style=\"padding:5px 30px;\">Event Name</th> <th style=\"padding:5px 30px;\">Categories</th> <th style=\"padding:5px 30px;\">Items</th> </tr> <tr > <td style=\"padding:5px 30px;\">Corporate events</td> <td style=\"padding:5px 30px;\">Team Outing</td> <td style=\"padding:5px 30px;\">Accommodation, Activities Planning, Trainer, Karaoke, Location booking, Properties/accessories, Travel management</td> </tr> <tr > <td style=\"padding:5px 30px;\">Corporate events</td> <td style=\"padding:5px 30px;\">Fun @ work events</td> <td style=\"padding:5px 30px;\">subcat1, subcat2, subcat3, subcat4, subcat5, subcat6, subcat6</td> </tr> </table><br /> <br /> <div> <p>Thanks,</p> <p>Mail Support team</p> </div> </div> </div> </td><br /><br /> <a target='_blank' href=\"#\" class='link2' style='color:#ffffff;background:#FD0002;padding:10px 18px;border-radius:40px;-moz-border-radius:40px;-webkit-border-radius:40px; border:1px #fff solid;'>Get In Touch</a> <td width='80'></td> </tr> </table> </div> <div class='movableContent'> <table width=\"600\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\"> <tr><td height='75'></td></tr> <tr><td><div style='border-top:1px solid #EBCDF2;'></div></td></tr> <tr><td height='25'></td></tr> <tr> <td> <div class=\"contentEditableContainer contentTextEditable\"> <div class=\"contentEditable\" style='color:#FBEFFE;text-align:center;font-size:13px;line-height:19px;'> <p>[Event Management] <br> [jp nagar, 1st phase, bengaluru - 560078] <br> [+91 1234567890] <br> </p><br /> <a href=\"#\"><img src=\"http://maximolaura.yx0se0rk8.netdna-cdn.com/wp-content/uploads/2016/05/facebook-icon.png\" width=\"34\" height=\"33\" alt=\"\" style=\"padding-right:5px;\"/></a> <a href=\"#\"><img src=\"http://findicons.com/files/icons/2788/circle/128/youtube.png\" width=\"34\" height=\"34\" alt=\"\" style=\"padding-right:5px;\"/></a> <a href=\"#\"><img src=\"http://www.unitapastoralesancataldo.it/images/twitter_icono.png\" width=\"34\" height=\"33\" alt=\"\"/></a> </div> </div> </td> </tr> </table> </div> </td> </tr> <tr><td height='20'></td></tr> <!-- =============== END FOOTER =============== --> </table> </td> </tr> </table> </body> </html>");
		return buff.toString();
	}
}
