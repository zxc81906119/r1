package controller;

import java.io.File;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.detailexpenditure;
import model.roughexpenditure;
import service.budgetservice;
import service.excelservice;
public class budget extends HttpServlet {
	private static final long serialVersionUID = 1L;
	budgetservice bs = new budgetservice();
	excelservice es=new excelservice();
	public budget() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String page = null;
		switch (action) {
		case "mainroughdata":
			page = querymainroughdata(request);
			break;
		case "seeroughbudget":
			page = queryroughbudget(request);
			break;
		case "showdetailbudget":
			page = querydetailbudget(request);
			break;
		case "savedata":
			page = savedataandsumdata(request);
			break;
		case "updatetype":
			page = updatetype(request);
			break;
		case "insertroughexpendituredata":
			page = insertroughdata(request);
			break;
		case "toexcel":
			try {
				toexcel(request,response);
			} catch (AddressException e) {
	
				e.printStackTrace();
			} catch (MessagingException e) {
				
				e.printStackTrace();
			}
		}
		if(page!=null) {
			request.getRequestDispatcher(page).forward(request, response);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	private void toexcel(HttpServletRequest request,HttpServletResponse response) throws IOException, AddressException, MessagingException {
		List<String[]> slist=new ArrayList<String[]>();
		String id=request.getParameter("id");
		String[] type=request.getParameterValues("type");
		String[] date=request.getParameterValues("date");
		String[] mondayincome=request.getParameterValues("mondayincome");
		String[]tuesdayincome=request.getParameterValues("tuesdayincome");
		String[] wednesdayincome=request.getParameterValues("wednesdayincome");
		String[] thursdayincome=request.getParameterValues("thursdayincome");
		String[] fridayincome=request.getParameterValues("fridayincome");
		String[] saturdayincome=request.getParameterValues("saturdayincome");
		String[] sundayincome=request.getParameterValues("sundayincome");
		String[] mondayexpenditure=request.getParameterValues("mondayexpenditure");
		String[] tuesdayexpenditure=request.getParameterValues("tuesdayexpenditure");
		String[] wednesdayexpenditure=request.getParameterValues("wednesdayexpenditure");
		String[] thursdayexpenditure=request.getParameterValues("thursdayexpenditure");
		String[] fridayexpenditure=request.getParameterValues("fridayexpenditure");
		String[] saturdayexpenditure=request.getParameterValues("saturdayexpenditure");
		String[] sundayexpenditure=request.getParameterValues("sundayexpenditure");
		int length=type.length;
		for(int i=0;i<length;i++) {
			String[] arow=new String[17];
			arow[0]=id;
			arow[1]=type[i];
			arow[2]=date[i];
			arow[3]=mondayincome[i];
			arow[4]=tuesdayincome[i];
			arow[5]=wednesdayincome[i];
			arow[6]=thursdayincome[i];
			arow[7]=fridayincome[i];
			arow[8]=saturdayincome[i];
			arow[9]=sundayincome[i];
			arow[10]=mondayexpenditure[i];
			arow[11]=tuesdayexpenditure[i];
			arow[12]=wednesdayexpenditure[i];
			arow[13]=thursdayexpenditure[i];
			arow[14]=fridayexpenditure[i];
			arow[15]=saturdayexpenditure[i];
			arow[16]=sundayexpenditure[i];
			slist.add(arow);
		}
		response.setContentType("application/ms-excel");
		String fileName="收支報表";
		try {
			fileName = java.net.URLEncoder.encode(fileName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			System.err.println(e1.getMessage());
		}
		es.sendexcel(slist, request,"bboypop19@gmail.com", fileName);
		response.setHeader("Content-Disposition","attachment;filename="+fileName+".xls");
		if(slist!=null) {
			es.getexcel(slist,request, response.getOutputStream());
		}
		response.flushBuffer();
	}
	
	private String queryroughbudget(HttpServletRequest request) {
		String type = request.getParameter("type");
		String id = request.getParameter("id");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		Map<Integer, List<roughexpenditure>> pagemap = bs.getroughbudget(id, type, year, month);

		if (pagemap != null) {
			int mapsize = pagemap.size();
			request.setAttribute("mapsize", mapsize);
			request.setAttribute("pagemap", pagemap);
		}
		return "update.jsp";
	}

	private String querymainroughdata(HttpServletRequest request) {
		String id = request.getParameter("id");
		Map<Integer, List<roughexpenditure>> roughmap = bs.mainroughdata(id);
		if (roughmap != null) {
			int mapsize = roughmap.size();
			request.setAttribute("mapsize", mapsize);
			request.setAttribute("pagemap", roughmap);
			request.setAttribute("alldata", true);
		}
		return "update.jsp";
	}

	private String querydetailbudget(HttpServletRequest request) {
		String id = request.getParameter("id");
		String weekfirstday = request.getParameter("weekfirstday");
		String type = request.getParameter("type");
		String detailid =id+weekfirstday.replaceAll("-","")+type;
		List<detailexpenditure> dlist = bs.getdetailbudget(detailid);
		request.setAttribute("id", id);
		request.setAttribute("weekfirstday", weekfirstday);
		request.setAttribute("type", type);
		if (dlist != null) {
			request.setAttribute("dlist", dlist);
		}
		return "detailexpenditure.jsp";
	}

	private String savedataandsumdata(HttpServletRequest request) {
		List<detailexpenditure> dlist = new ArrayList<detailexpenditure>();
		String id = request.getParameter("id");
		String weekfirstday = request.getParameter("weekfirstday");
		String type = request.getParameter("type");
		String[] yearmonthday = weekfirstday.split("-");
		Date dt = new Date();
		Date dtaftersix = new Date();
		dt.setYear(Integer.parseInt(yearmonthday[0]) - 1900);
		dt.setMonth(Integer.parseInt(yearmonthday[1]) - 1);
		dt.setDate(Integer.parseInt(yearmonthday[2]));
		dtaftersix.setTime(dt.getTime() + 6 * 86400000);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String allweekfirstday = weekfirstday + "~" + sdf.format(dtaftersix);

		request.setAttribute("allweekfirstday", allweekfirstday);
		request.setAttribute("type", type);
		request.setAttribute("mondayincometotal", Integer.parseInt(request.getParameter("mondayincometotal")));
		request.setAttribute("tuesdayincometotal", Integer.parseInt(request.getParameter("tuesdayincometotal")));
		request.setAttribute("wednesdayincometotal", Integer.parseInt(request.getParameter("wednesdayincometotal")));
		request.setAttribute("thursdayincometotal", Integer.parseInt(request.getParameter("thursdayincometotal")));
		request.setAttribute("fridayincometotal", Integer.parseInt(request.getParameter("fridayincometotal")));
		request.setAttribute("saturdayincometotal", Integer.parseInt(request.getParameter("saturdayincometotal")));
		request.setAttribute("sundayincometotal", Integer.parseInt(request.getParameter("sundayincometotal")));
		request.setAttribute("mondayexpendituretotal",
				Integer.parseInt(request.getParameter("mondayexpendituretotal")));
		request.setAttribute("tuesdayexpendituretotal",
				Integer.parseInt(request.getParameter("tuesdayexpendituretotal")));
		request.setAttribute("wednesdayexpendituretotal",
				Integer.parseInt(request.getParameter("wednesdayexpendituretotal")));
		request.setAttribute("thursdayexpendituretotal",
				Integer.parseInt(request.getParameter("thursdayexpendituretotal")));
		request.setAttribute("fridayexpendituretotal",
				Integer.parseInt(request.getParameter("fridayexpendituretotal")));
		request.setAttribute("saturdayexpendituretotal",
				Integer.parseInt(request.getParameter("saturdayexpendituretotal")));
		request.setAttribute("sundayexpendituretotal",
				Integer.parseInt(request.getParameter("sundayexpendituretotal")));

		String[] contents = request.getParameterValues("content");
		String[] mondayincomes = request.getParameterValues("mondayincome");
		String[] tuesdayincomes = request.getParameterValues("tuesdayincome");
		String[] wednesdayincomes = request.getParameterValues("wednesdayincome");
		String[] thursdayincomes = request.getParameterValues("thursdayincome");
		String[] fridayincomes = request.getParameterValues("fridayincome");
		String[] saturdayincomes = request.getParameterValues("saturdayincome");
		String[] sundayincomes = request.getParameterValues("sundayincome");
		String[] mondayexpenditures = request.getParameterValues("mondayexpenditure");
		String[] tuesdayexpenditures = request.getParameterValues("tuesdayexpenditure");
		String[] wednesdayexpenditures = request.getParameterValues("wednesdayexpenditure");
		String[] thursdayexpenditures = request.getParameterValues("thursdayexpenditure");
		String[] fridayexpenditures = request.getParameterValues("fridayexpenditure");
		String[] saturdayexpenditures = request.getParameterValues("saturdayexpenditure");
		String[] sundayexpenditures = request.getParameterValues("sundayexpenditure");
		int datalength = contents.length;
		String detailid = id + weekfirstday.replaceAll("-", "") + type;
		for (int i = 0; i < datalength; i++) {
			detailexpenditure de = new detailexpenditure();
			de.setDetailid(detailid);
			de.setContent(contents[i]);
			de.setMondayincome(Integer.parseInt(mondayincomes[i]));
			de.setTuesdayincome(Integer.parseInt(tuesdayincomes[i]));
			de.setWednesdayincome(Integer.parseInt(wednesdayincomes[i]));
			de.setThursdayincome(Integer.parseInt(thursdayincomes[i]));
			de.setFridayincome(Integer.parseInt(fridayincomes[i]));
			de.setSaturdayincome(Integer.parseInt(saturdayincomes[i]));
			de.setSundayincome(Integer.parseInt(sundayincomes[i]));
			de.setMondayexpenditure(Integer.parseInt(mondayexpenditures[i]));
			de.setTuesdayexpenditure(Integer.parseInt(tuesdayexpenditures[i]));
			de.setWednesdayexpenditure(Integer.parseInt(wednesdayexpenditures[i]));
			de.setThursdayexpenditure(Integer.parseInt(thursdayexpenditures[i]));
			de.setFridayexpenditure(Integer.parseInt(fridayexpenditures[i]));
			de.setSaturdayexpenditure(Integer.parseInt(saturdayexpenditures[i]));
			de.setSundayexpenditure(Integer.parseInt(sundayexpenditures[i]));
			dlist.add(de);
		}
		bs.savedetaildataandsumroughdata(dlist);
		request.setAttribute("savesuccess", true);
		return "detailexpenditure.jsp";
	}

	private String updatetype(HttpServletRequest request) {
		String id = request.getParameter("id");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String date = request.getParameter("date");
		String oldtype = request.getParameter("oldtype");
		String newtype = request.getParameter("newtype");
		if (bs.isthereroughdata(year, month, date, oldtype, id)) {
			if (bs.isthereroughdata(year, month, date, newtype, id)) {
				request.setAttribute("hasrepeat", true);
			} else {
				bs.updatetype(year, month, date, oldtype, newtype, id);
				request.setAttribute("updatesuccess", true);
			}
		} else {
			request.setAttribute("notexistroughdata", true);
		}
		return "updateroughexpendituretype.jsp";
	}

	private String insertroughdata(HttpServletRequest request) {
		String id = request.getParameter("id");
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String date = request.getParameter("date");
		String type = request.getParameter("type");
		if (bs.isthereroughdata(year, month, date, type, id)) {
			request.setAttribute("existroughdata", true);
		} else {
			bs.insertroughdata(year, month, date, type, id);
			request.setAttribute("insertsuccess", true);
		}
		return "insertroughexpendituredata.jsp";
	}
}
