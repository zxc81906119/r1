package controller;

import java.io.BufferedReader;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Part;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Out;

import model.detaildollcost;
import model.roughdollcost;
import service.dollservice;

public class doll extends HttpServlet {
	dollservice ds = new dollservice();

	private static final long serialVersionUID = 1L;

	public doll() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String action = request.getParameter("action");
		String page = null;
		switch (action) {
		case "dailycostandinoutgoods":
			try {
				page = getallroughdollcostdata(request);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "detailcostdata":
			page = getalldetaildollcostdata(request);
			break;
		case "updatedetailonedata":
			try {
				updatedetailonedata(request, response);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "multiupdate":
			try {
				page = multiupdate(request);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "updatewithdrawmoney":
			updatewithdrawmoney(request, response);
			break;
		case "getalldolldata":
			page = getalldolldata(request);
			break;
		case "insertnewdolldata":
			page=insertnewdolldata(request);
			break;
		case "getalldollnames":
			getalldollnames(request,response);
			break;
		}
		if (page != null) {
			request.getRequestDispatcher(page).forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	private void getalldollnames(HttpServletRequest request,HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=UTF-8");
		String[] dollnames=ds.getalldollnames();
		String dollnamesstring="";
		if(dollnames!=null){
			for(int i=0;i<dollnames.length;i++){
				if(i!=dollnames.length-1){
					dollnamesstring+=dollnames[i]+",";
				}else{
					dollnamesstring+=dollnames[i];
				}
			}
			response.getWriter().print(dollnamesstring);
		}
	}
	private String getallroughdollcostdata(HttpServletRequest request) throws SQLException {
		String year = request.getParameter("year");
		String month = request.getParameter("month");
		String day = request.getParameter("day");
		Map<Integer, List<roughdollcost>> pagemap = ds.getroughdollcostdata(year, month, day);
		if (pagemap != null) {
			request.setAttribute("pagemap", pagemap);
		}
		return "costanalize.jsp";
	}

	private String getalldetaildollcostdata(HttpServletRequest request) {
		String thisdate = request.getParameter("thisdate");
		List<detaildollcost> dlist = ds.getdetaildollcostdata(thisdate);
		if (dlist != null) {
			request.setAttribute("dlist", dlist);
			request.setAttribute("thisdate", thisdate);
		}
		return "detaildollcost.jsp";
	}

	private void updatedetailonedata(HttpServletRequest request, HttpServletResponse response)
			throws SQLException, IOException {
		response.setContentType("text/plain;charset=UTF-8");
		String thisdate = request.getParameter("thisdate");
		String dollname = request.getParameter("dollname");
		
		String todollmachinecount = request.getParameter("todollmachinecount");
		String outdollmachinecount = request.getParameter("outdollmachinecount");
		ds.updateonedetaildata(thisdate, dollname, todollmachinecount, outdollmachinecount);
		response.getWriter().print("��摰��");
	}

	private String multiupdate(HttpServletRequest request) throws SQLException {
		String thisdate = request.getParameter("thisdate");
		String[] dollname = request.getParameterValues("dollname");
		String[] todollmachinecount = request.getParameterValues("todollmachinecount");
		String[] outdollmachinecount = request.getParameterValues("outdollmachinecount");
		List<detaildollcost> dlist = new ArrayList<detaildollcost>();
		for (int i = 0; i < dollname.length; i++) {
			detaildollcost ddc = new detaildollcost();
			ddc.setDollname(dollname[i]);
			ddc.setTodollmachinecount(Integer.parseInt(todollmachinecount[i]));
			ddc.setOutdollmachinecount(Integer.parseInt(outdollmachinecount[i]));
			dlist.add(ddc);
		}
		ds.multiupdate(dlist, thisdate);
		List<detaildollcost> dlist1=ds.getdetaildollcostdata(thisdate);
		if(dlist1!=null){
			request.setAttribute("dlist", dlist1);
			request.setAttribute("thisdate", thisdate);
		}
		return "detaildollcost.jsp";
	}

	private void updatewithdrawmoney(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=UTF-8");
		String thisdate = request.getParameter("thisdate");
		String withdrawmoney = request.getParameter("withdrawmoney");
		ds.updateroughwithdraw(thisdate, withdrawmoney);
		PrintWriter out = response.getWriter();
		out.print("更新成功");
	}

	private String getalldolldata(HttpServletRequest request) {
		List<model.doll> dlist = ds.getalldolldata();
		if (dlist != null) {
			request.setAttribute("dlist", dlist);
		}
		return "insertnewdoll.jsp";

	}

	private String insertnewdolldata(HttpServletRequest request) throws IOException {
		String dollname=request.getParameter("dollname");
		String buyonedollprice=request.getParameter("buyonedollprice");
		String dollpictureurl="images/"+dollname+".jpg";
		ds.insertnewdoll(dollname, dollpictureurl, buyonedollprice);
		List<model.doll> dlist=ds.getalldolldata();
		if(dlist!=null) {
			request.setAttribute("dlist",dlist);
		}
		return "insertnewdoll.jsp";
	}
}
