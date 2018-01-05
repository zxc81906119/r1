package dao;

import java.util.List;
import java.util.Map;

import model.detailexpenditure;
import model.roughexpenditure;

public interface budgetdao {
	public Map<Integer,List<roughexpenditure>> getroughbudget(String id, String type, String year, String month);

	public List<detailexpenditure> getdetailbudget(String detailid);

	public void savedetaildataandsumroughdata(List<detailexpenditure> dlist);

	public void insertthisweekdata(String id);

	public void updatetype(String year, String month, String date, String oldtype, String newtype, String id);

	public void insertroughdata(String year, String month, String date, String type, String id);

	public boolean isthereroughdata(String year, String month, String date, String type, String id);

	public void deleteroughdata(String year, String month, String date, String type, String id);
	
	public Map<Integer,List<roughexpenditure>> mainroughdata(String id);
}
