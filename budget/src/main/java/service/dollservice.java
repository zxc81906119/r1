package service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import dao.dolldao;
import dao.dolljdbcdao;
import model.detaildollcost;
import model.doll;
import model.roughdollcost;

public class dollservice {
	dolldao ddao = new dolljdbcdao();

	public Map<Integer, List<roughdollcost>> getroughdollcostdata(String year, String month, String day)
			throws SQLException {
		return ddao.getroughdollcostdata(year, month, day);
	}

	public List<detaildollcost> getdetaildollcostdata(String thisdate) {
		return ddao.getdetaildollcostdata(thisdate);
	}

	public void updateonedetaildata(String thisdate, String dollname, String todollmachinecount,
			String outdollmachinecount) throws SQLException {
		ddao.updateonedetaildata(thisdate, dollname, todollmachinecount, outdollmachinecount);
	}

	public void multiupdate(List<detaildollcost> dlist, String thisdate) throws SQLException {
		ddao.multiupdate(dlist, thisdate);
	}

	public void updateroughwithdraw(String thisdate, String withdrawmoney) {
		ddao.updateroughwithdraw(thisdate, withdrawmoney);
	}

	public void insertroughdata() {
		ddao.insertroughdata();
	}

	public List<doll> getalldolldata() {
		return ddao.getalldolldata();
	}
	
	public void insertnewdoll(String dollname, String dollpictureurl, String buyonedollprice) {
		ddao.insertnewdoll(dollname, dollpictureurl, buyonedollprice);
	}
	
	public String[] getalldollnames(){
		return ddao.getalldollnames();
	}
}
