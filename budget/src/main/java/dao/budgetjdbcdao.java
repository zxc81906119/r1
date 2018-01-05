package dao;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Page;
import model.detailexpenditure;
import model.roughexpenditure;

public class budgetjdbcdao implements budgetdao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public Map<Integer, List<roughexpenditure>> getroughbudget(String id, String type, String year, String month) {
		conn = connectionhelper.getConnection();
		Map<Integer, List<roughexpenditure>> pagemap = new HashMap<Integer, List<roughexpenditure>>();
		Page page = new Page();
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from roughexpenditure ");
		Date dt = new Date();
		dt.setYear(Integer.parseInt(year) - 1900);
		dt.setMonth(Integer.parseInt(month) - 1);
		java.sql.Date firstday;
		java.sql.Date lastday;
		dt.setDate(1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		firstday = new java.sql.Date(cal.getTime().getTime());
		dt.setMonth(Integer.parseInt(month));
		dt.setDate(0);
		cal.setTime(dt);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
			cal.add(Calendar.DAY_OF_MONTH, 7);
		} else {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}
		lastday = new java.sql.Date(cal.getTime().getTime());
		if (type == "" || type == null) {
			sb.append("where id=? and weekfirstday between ? and ? ");
		} else {
			sb.append("where id=? and type=? and weekfirstday between ? and ? ");
		}
		sb.append("order by weekfirstday desc ");
		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, id);
			if (type != "" && type != null) {
				pstmt.setString(2, type);
				pstmt.setDate(3, firstday);
				pstmt.setDate(4, lastday);
			} else {
				pstmt.setDate(2, firstday);
				pstmt.setDate(3, lastday);
			}
			rs = pstmt.executeQuery();

			if (rs != null) {
				int totaldatacount = 0;
				rs.last();
				totaldatacount = rs.getRow();
				page.setTotaldatacount(totaldatacount);
				rs.beforeFirst();
				// 設定總頁數
				page.setTotalpagecount();
				for (int k = 1; k <= page.getTotalpagecount(); k++) {
					List<roughexpenditure> rlist = new ArrayList<roughexpenditure>();
					for (int j = ((k - 1) * page.getPerpagedatacount() + 1); j <= ((k - 1) * page.getPerpagedatacount()
							+ page.getThispagedatacount(k)); j++) {

						if (rs.next()) {
							roughexpenditure re = new roughexpenditure();
							re.setType(rs.getString("TYPE"));
							re.setWeekfirstday(rs.getDate("WEEKFIRSTDAY"));
							re.setMondayincome(rs.getInt("MONDAYincome"));
							re.setTuesdayincome(rs.getInt("TUESDAYincome"));
							re.setWednesdayincome(rs.getInt("WEDNESDAYincome"));
							re.setThursdayincome(rs.getInt("THURSDAYincome"));
							re.setFridayincome(rs.getInt("FRIDAYincome"));
							re.setSaturdayincome(rs.getInt("SATURDAYincome"));
							re.setSundayincome(rs.getInt("SUNDAYincome"));
							re.setMondayexpenditure(rs.getInt("MONDAYexpenditure"));
							re.setTuesdayexpenditure(rs.getInt("Tuesdayexpenditure"));
							re.setWednesdayexpenditure(rs.getInt("Wednesdayexpenditure"));
							re.setThursdayexpenditure(rs.getInt("Thursdayexpenditure"));
							re.setFridayexpenditure(rs.getInt("Fridayexpenditure"));
							re.setSaturdayexpenditure(rs.getInt("Saturdayexpenditure"));
							re.setSundayexpenditure(rs.getInt("Sundayexpenditure"));
							re.setDetailid(rs.getString("DETAILID"));
							rlist.add(re);
						}
					}
					pagemap.put(k, rlist);
				}
				return pagemap;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	public List<detailexpenditure> getdetailbudget(String detailid) {
		conn = connectionhelper.getConnection();
		List<detailexpenditure> dlist = new ArrayList<detailexpenditure>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from detailexpenditure ");
		sb.append("where detailid=? ");
		sb.append("order by detailid ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, detailid);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					detailexpenditure de = new detailexpenditure();
					de.setDetailid(rs.getString("DETAILID"));
					de.setContent(rs.getString("CONTENT"));
					de.setMondayincome(rs.getInt("MONDAYincome"));
					de.setTuesdayincome(rs.getInt("TUESDAYincome"));
					de.setWednesdayincome(rs.getInt("WEDNESDAYincome"));
					de.setThursdayincome(rs.getInt("THURSDAYincome"));
					de.setFridayincome(rs.getInt("FRIDAYincome"));
					de.setSaturdayincome(rs.getInt("SATURDAYincome"));
					de.setSundayincome(rs.getInt("SUNDAYincome"));
					de.setMondayexpenditure(rs.getInt("MONDAYexpenditure"));
					de.setTuesdayexpenditure(rs.getInt("Tuesdayexpenditure"));
					de.setWednesdayexpenditure(rs.getInt("Wednesdayexpenditure"));
					de.setThursdayexpenditure(rs.getInt("Thursdayexpenditure"));
					de.setFridayexpenditure(rs.getInt("Fridayexpenditure"));
					de.setSaturdayexpenditure(rs.getInt("Saturdayexpenditure"));
					de.setSundayexpenditure(rs.getInt("Sundayexpenditure"));
					dlist.add(de);
				}
				return dlist;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	// ******************************************************重要
	public void savedetaildataandsumroughdata(List<detailexpenditure> dlist) {
		conn = connectionhelper.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("delete from detailexpenditure ");
		sb.append("where detailid=? ");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("insert into detailexpenditure ");
		sb1.append("values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ");

		StringBuilder sb2 = new StringBuilder();
		sb2.append("update roughexpenditure ");
		sb2.append(
				"set mondayincome=?,tuesdayincome=?,wednesdayincome=?,thursdayincome=?,fridayincome=?,saturdayincome=?,sundayincome=?,mondayexpenditure=?,tuesdayexpenditure=?,wednesdayexpenditure=?,thursdayexpenditure=?,fridayexpenditure=?,saturdayexpenditure=?,sundayexpenditure=? ");
		sb2.append("where detailid=? ");

		try {

			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, dlist.get(0).getDetailid());
			pstmt.executeUpdate();

			roughexpenditure re = new roughexpenditure();
			for (int i = 0; i < dlist.size(); i++) {
				detailexpenditure de = dlist.get(i);
				pstmt = conn.prepareStatement(sb1.toString());
				pstmt.setString(1, de.getDetailid());
				pstmt.setString(2, de.getContent());
				pstmt.setInt(3, de.getMondayincome());
				pstmt.setInt(4, de.getTuesdayincome());
				pstmt.setInt(5, de.getWednesdayincome());
				pstmt.setInt(6, de.getThursdayincome());
				pstmt.setInt(7, de.getFridayincome());
				pstmt.setInt(8, de.getSaturdayincome());
				pstmt.setInt(9, de.getSundayincome());
				pstmt.setInt(10, de.getMondayexpenditure());
				pstmt.setInt(11, de.getTuesdayexpenditure());
				pstmt.setInt(12, de.getWednesdayexpenditure());
				pstmt.setInt(13, de.getThursdayexpenditure());
				pstmt.setInt(14, de.getFridayexpenditure());
				pstmt.setInt(15, de.getSaturdayexpenditure());
				pstmt.setInt(16, de.getSundayexpenditure());
				pstmt.executeUpdate();

				re.setMondayexpenditure(re.getMondayexpenditure() + de.getMondayexpenditure());
				re.setTuesdayexpenditure(re.getTuesdayexpenditure() + de.getTuesdayexpenditure());
				re.setWednesdayexpenditure(re.getWednesdayexpenditure() + de.getWednesdayexpenditure());
				re.setThursdayexpenditure(re.getThursdayexpenditure() + de.getThursdayexpenditure());
				re.setFridayexpenditure(re.getFridayexpenditure() + de.getFridayexpenditure());
				re.setSaturdayexpenditure(re.getSaturdayexpenditure() + de.getSaturdayexpenditure());
				re.setSundayexpenditure(re.getSundayexpenditure() + de.getSundayexpenditure());
				re.setMondayincome(re.getMondayincome() + de.getMondayincome());
				re.setTuesdayincome(re.getTuesdayincome() + de.getTuesdayincome());
				re.setWednesdayincome(re.getWednesdayincome() + de.getWednesdayincome());
				re.setThursdayincome(re.getThursdayincome() + de.getThursdayincome());
				re.setFridayincome(re.getFridayincome() + de.getFridayincome());
				re.setSaturdayincome(re.getSaturdayincome() + de.getSaturdayincome());
				re.setSundayincome(re.getSundayincome() + de.getSundayincome());
			}

			pstmt = conn.prepareStatement(sb2.toString());
			pstmt.setInt(1, re.getMondayincome());
			pstmt.setInt(2, re.getTuesdayincome());
			pstmt.setInt(3, re.getWednesdayincome());
			pstmt.setInt(4, re.getThursdayincome());
			pstmt.setInt(5, re.getFridayincome());
			pstmt.setInt(6, re.getSaturdayincome());
			pstmt.setInt(7, re.getSundayincome());
			pstmt.setInt(8, re.getMondayexpenditure());
			pstmt.setInt(9, re.getTuesdayexpenditure());
			pstmt.setInt(10, re.getWednesdayexpenditure());
			pstmt.setInt(11, re.getThursdayexpenditure());
			pstmt.setInt(12, re.getFridayexpenditure());
			pstmt.setInt(13, re.getSaturdayexpenditure());
			pstmt.setInt(14, re.getSundayexpenditure());
			pstmt.setString(15, dlist.get(0).getDetailid());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void insertthisweekdata(String id) {
		conn = connectionhelper.getConnection();

		StringBuilder sb1 = new StringBuilder();
		sb1.append("insert into roughexpenditure ");
		sb1.append("values(?,?,'通用類',0,0,0,0,0,0,0,0,0,0,0,0,0,0,?) ");

		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) ");
		sb.append("from roughexpenditure ");
		sb.append("where weekfirstday=? and id=? ");

		Date dt = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dt);
		if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		} else {
			cal.add(Calendar.DAY_OF_MONTH, -1);
			cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		}
		dt = cal.getTime();
		java.sql.Date sd = new java.sql.Date(dt.getTime());
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setDate(1, sd);
			pstmt.setString(2, id);
			rs = pstmt.executeQuery();
			if (rs != null) {
				if (rs.next()) {
					if (rs.getInt("COUNT(*)") == 0) {
						pstmt = conn.prepareStatement(sb1.toString());
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						String stringdt = sdf.format(dt);
						pstmt.setString(1, id);
						pstmt.setDate(2, sd);
						pstmt.setString(3, id + stringdt + "通用類");
						pstmt.executeUpdate();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatetype(String year, String month, String date, String oldtype, String newtype, String id) {
		conn = connectionhelper.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("update roughexpenditure ");
		sb.append("set type=? ");
		sb.append("where weekfirstday=? and id=? and type=? ");

		StringBuilder sb2 = new StringBuilder();
		sb2.append("update detailexpenditure ");
		sb2.append("set detailid=? ");
		sb2.append("where detailid=? ");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("update roughexpenditure ");
		sb1.append("set detailid=? ");
		sb1.append("where weekfirstday=? and id=? and type=? ");

		try {
			pstmt = conn.prepareStatement(sb.toString());
			Date dt = new Date();
			dt.setYear(Integer.parseInt(year) - 1900);
			dt.setMonth(Integer.parseInt(month) - 1);
			dt.setDate(Integer.parseInt(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			} else {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			}
			java.sql.Date sdt = new java.sql.Date(cal.getTime().getTime());
			pstmt.setString(1, newtype);
			pstmt.setDate(2, sdt);
			pstmt.setString(3, id);
			pstmt.setString(4, oldtype);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sb1.toString());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String yearmonthdate = sdf.format(cal.getTime());
			pstmt.setString(1, id + yearmonthdate + newtype);
			pstmt.setDate(2, sdt);
			pstmt.setString(3, id);
			pstmt.setString(4, newtype);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sb2.toString());
			pstmt.setString(1, id + yearmonthdate + newtype);
			pstmt.setString(2, id + yearmonthdate + oldtype);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void deleteroughdata(String year, String month, String date, String type, String id) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("delete from roughexpenditure ");
		sb.append("where id=? and type=? and weekfirstday=? ");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("delete from detailexpenditure ");
		sb1.append("where detailid=? ");

		try {
			Date dt = new Date();
			dt.setYear(Integer.parseInt(year) - 1900);
			dt.setMonth(Integer.parseInt(month) - 1);
			dt.setDate(Integer.parseInt(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			} else {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			}
			java.sql.Date sdt = new java.sql.Date(cal.getTime().getTime());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String stringdt = sdf.format(cal.getTime());
			String detailid = id + stringdt + type;
			System.out.println(detailid);
			pstmt = conn.prepareStatement(sb1.toString());
			pstmt.setString(1, detailid);
			pstmt.executeUpdate();
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, type);
			pstmt.setDate(3, sdt);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public Map<Integer, List<roughexpenditure>> mainroughdata(String id) {
		conn = connectionhelper.getConnection();
		Map<Integer, List<roughexpenditure>> roughmap = new HashMap<Integer, List<roughexpenditure>>();

		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from roughexpenditure ");
		sb.append("where id=? ");
		sb.append("order by weekfirstday desc ");

		try {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.last();
				int totalrowdata = rs.getRow();
				Page page = new Page();
				page.setTotaldatacount(totalrowdata);
				int totalpage;
				if (totalrowdata <= page.getPerpagedatacount()) {
					totalpage = 1;
				} else {
					if (totalrowdata % page.getPerpagedatacount() == 0) {
						totalpage = totalrowdata / page.getPerpagedatacount();
					} else {
						totalpage = totalrowdata / page.getPerpagedatacount() + 1;
					}
				}
				rs.beforeFirst();
				for (int g = 1; g <= totalpage; g++) {
					List<roughexpenditure> rlist = new ArrayList<roughexpenditure>();
					for (int l = 1 + (g - 1) * page.getPerpagedatacount(); l <= (g - 1) * page.getPerpagedatacount()
							+ page.getThispagedatacount(g); l++) {
						if (rs.next()) {
							System.out.println("gg");
							roughexpenditure re = new roughexpenditure();
							re.setType(rs.getString("TYPE"));
							re.setWeekfirstday(rs.getDate("WEEKFIRSTDAY"));
							re.setMondayincome(rs.getInt("MONDAYincome"));
							re.setTuesdayincome(rs.getInt("TUESDAYincome"));
							re.setWednesdayincome(rs.getInt("WEDNESDAYincome"));
							re.setThursdayincome(rs.getInt("THURSDAYincome"));
							re.setFridayincome(rs.getInt("FRIDAYincome"));
							re.setSaturdayincome(rs.getInt("SATURDAYincome"));
							re.setSundayincome(rs.getInt("SUNDAYincome"));
							re.setMondayexpenditure(rs.getInt("MONDAYexpenditure"));
							re.setTuesdayexpenditure(rs.getInt("Tuesdayexpenditure"));
							re.setWednesdayexpenditure(rs.getInt("Wednesdayexpenditure"));
							re.setThursdayexpenditure(rs.getInt("Thursdayexpenditure"));
							re.setFridayexpenditure(rs.getInt("Fridayexpenditure"));
							re.setSaturdayexpenditure(rs.getInt("Saturdayexpenditure"));
							re.setSundayexpenditure(rs.getInt("Sundayexpenditure"));
							re.setDetailid(rs.getString("DETAILID"));
							rlist.add(re);
						}
					}
					roughmap.put(g, rlist);
				}
				return roughmap;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	public void insertroughdata(String year, String month, String date, String type, String id) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("insert into roughexpenditure ");
		sb.append("values(?,?,?,0,0,0,0,0,0,0,0,0,0,0,0,0,0,?) ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			Date dt = new Date();
			dt.setYear(Integer.parseInt(year) - 1900);
			dt.setMonth(Integer.parseInt(month) - 1);
			dt.setDate(Integer.parseInt(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			} else {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			}
			java.sql.Date sdt = new java.sql.Date(cal.getTime().getTime());
			pstmt.setDate(2, sdt);
			pstmt.setString(3, type);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String yearmonthdate = sdf.format(cal.getTime());
			pstmt.setString(4, id + yearmonthdate + type);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}

	}

	public boolean isthereroughdata(String year, String month, String date, String type, String id) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) ");
		sb.append("from roughexpenditure ");
		sb.append("where id=? and type=? and weekfirstday=? ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, type);
			Date dt = new Date();
			dt.setYear(Integer.parseInt(year) - 1900);
			dt.setMonth(Integer.parseInt(month) - 1);
			dt.setDate(Integer.parseInt(date));
			Calendar cal = Calendar.getInstance();
			cal.setTime(dt);
			if (cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			} else {
				cal.add(Calendar.DAY_OF_MONTH, -1);
				cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			}
			java.sql.Date sdt = new java.sql.Date(cal.getTime().getTime());
			pstmt.setDate(3, sdt);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if (rs.getInt("COUNT(*)") == 1) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	private void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}