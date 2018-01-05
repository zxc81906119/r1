package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import model.detaildollcost;
import model.doll;
import model.roughdollcost;

public interface dolldao {
	public Map<Integer, List<roughdollcost>> getroughdollcostdata(String year, String month, String day)
			throws SQLException;

	public List<detaildollcost> getdetaildollcostdata(String thisdate);

	public void updateonedetaildata(String thisdate, String dollname, String todollmachinecount,
			String outdollmachinecount) throws SQLException;

	public void multiupdate(List<detaildollcost> dlist, String thisdate) throws SQLException;

	public void updateroughwithdraw(String thisdate, String withdrawmoney);

	public void insertroughdata();

	public List<doll> getalldolldata();
	
	public void insertnewdoll(String dollname, String dollpictureurl, String buyonedollprice);
	
	public String[] getalldollnames();
}
