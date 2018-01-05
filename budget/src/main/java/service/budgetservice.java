package service;

import java.util.ArrayList;


import java.util.List;
import java.util.Map;

import dao.budgetdao;
import dao.budgetjdbcdao;
import model.detailexpenditure;
import model.roughexpenditure;
import org.json.*;

public class budgetservice {
	budgetdao bd = new budgetjdbcdao();

	public Map<Integer, List<roughexpenditure>> getroughbudget(String id, String type, String year, String month) {
		return bd.getroughbudget(id, type, year, month);
	}

	public List<detailexpenditure> getdetailbudget(String detailid) {
		return bd.getdetailbudget(detailid);
	}

	public void savedetaildataandsumroughdata(List<detailexpenditure> dlist) {
		bd.savedetaildataandsumroughdata(dlist);
	}

	public void insertthisweekdata(String id) {
		bd.insertthisweekdata(id);
	}

	public void updatetype(String year, String month, String date, String oldtype, String newtype, String id) {
		bd.updatetype(year, month, date, oldtype, newtype, id);
	}

	public void insertroughdata(String year, String month, String date, String type, String id) {
		bd.insertroughdata(year, month, date, type, id);
	}

	public void deleteroughdata(String year, String month, String date, String type, String id) {
		bd.deleteroughdata(year, month, date, type, id);
	}

	public boolean isthereroughdata(String year, String month, String date, String type, String id) {
		return bd.isthereroughdata(year, month, date, type, id);
	}

	public Map<Integer, List<roughexpenditure>> mainroughdata(String id) {
		return bd.mainroughdata(id);
	}

	public List<String[]> jsontolist(String json) {
		List<String[]> roughbudgetdata = new ArrayList<String[]>();
		try {
			JSONArray jsonarray = new JSONArray(json);
			for (int i = 0; i < jsonarray.length(); i++) {
				JSONObject onerow = jsonarray.getJSONObject(i);
				String[] arow = new String[17];
				arow[0] = onerow.getString("id");
				arow[1] = onerow.getString("date");
				arow[2] = onerow.getString("type");
				arow[3] = onerow.getInt("mondayincome") + "";
				arow[4] = onerow.getInt("tuesdayincome") + "";
				arow[5] = onerow.getInt("wednesdayincome") + "";
				arow[6] = onerow.getInt("thursdayincome") + "";
				arow[7] = onerow.getInt("fridayincome") + "";
				arow[8] = onerow.getInt("saturdayincome") + "";
				arow[9] = onerow.getInt("sundayincome") + "";
				arow[10] = onerow.getInt("mondayexpenditure") + "";
				arow[11] = onerow.getInt("tuesdayexpenditure") + "";
				arow[12] = onerow.getInt("wednesdayexpenditure") + "";
				arow[13] = onerow.getInt("thursdayexpenditure") + "";
				arow[14] = onerow.getInt("fridayexpenditure") + "";
				arow[15] = onerow.getInt("saturdayexpenditure") + "";
				arow[16] = onerow.getInt("sundayexpenditure") + "";
				roughbudgetdata.add(arow);
			}
			return roughbudgetdata;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
