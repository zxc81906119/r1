package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.budgetservice;
import service.dollservice;
import service.employeeservice;

public class login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	budgetservice bs=new budgetservice();
	dollservice ds=new dollservice();
	public login() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");
		String password = request.getParameter("password");
		employeeservice es = new employeeservice();
		if (es.hasid(id)) {
			if (es.rightpassword(id, password)) {
				request.getSession().setAttribute("id", id);
				bs.insertthisweekdata(id);
				ds.insertroughdata();
				request.getRequestDispatcher("main.jsp").forward(request, response);
			} else {
				request.setAttribute("passworderror", "密碼錯誤");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		} else {
			request.setAttribute("idisnotexist", "帳號不存在");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
