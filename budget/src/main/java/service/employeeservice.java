package service;

import dao.employeedao;
import dao.employeejdbcdao;

public class employeeservice {
	employeedao edao = new employeejdbcdao();

	public boolean hasid(String id) {
		return edao.hasid(id);
	}

	public boolean rightpassword(String id, String password) {
		return edao.rightpassword(id, password);
	}
}
