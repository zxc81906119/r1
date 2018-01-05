package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Page;
import model.detaildollcost;
import model.doll;
import model.roughdollcost;

public class dolljdbcdao implements dolldao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public Map<Integer, List<roughdollcost>> getroughdollcostdata(String year, String month, String day)
			throws SQLException {
		conn = connectionhelper.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from roughdollcost ");
		sb.append("order by thisdate desc ");

		if (year != "" && year != null) {
			if ((month == null || month == "") && (day == null || day == "")) {
				sb.append("where to_char(thisdate,'yyyy')=? ");
				pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, year);
			}
			if ((month == null || month == "") && day != null && day != "") {
				day = day.substring(0, 1).equals("0") ? day : (Integer.parseInt(day) >= 10 ? day : "0" + day);
				sb.append("where to_char(thisdate,'yyyy')=? and to_char(thisdate,'dd')=? ");
				pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, year);
				pstmt.setString(2, day);
			}
			if (month != null && month != "" && (day == null || day == "")) {
				month = month.substring(0, 1).equals("0") ? month
						: (Integer.parseInt(month) >= 10 ? month : "0" + month);
				sb.append("where to_char(thisdate,'yyyy')=? and to_char(thisdate,'MM')=? ");
				pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, year);
				pstmt.setString(2, month);
			}
			if (month != null && month != "" && day != null && day != "") {
				String perfectdate = year
						+ (month.substring(0, 1).equals("0") ? month
								: (Integer.parseInt(month) >= 10 ? month : "0" + month))
						+ (day.substring(0, 1).equals("0") ? day : (Integer.parseInt(day) >= 10 ? day : "0" + day));
				sb.append("where to_char(thisdate,'yyyyMMdd')=? ");
				pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE,
						ResultSet.CONCUR_READ_ONLY);
				pstmt.setString(1, perfectdate);
			}
		} else {
			pstmt = conn.prepareStatement(sb.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		}
		rs = pstmt.executeQuery();
		if (rs != null) {
			Page page = new Page();
			rs.last();
			int datalength=rs.getRow();
			page.setTotaldatacount(datalength);
			rs.beforeFirst();
			page.setTotalpagecount();
			Map<Integer, List<roughdollcost>> map = new HashMap<Integer, List<roughdollcost>>();
			for (int pg = 1; pg <= page.getTotalpagecount(); pg++) {
				List<roughdollcost> rlist = new ArrayList<roughdollcost>();
				for (int row = 1 + (pg - 1) * page.getPerpagedatacount(); row <= (pg - 1) * page.getPerpagedatacount()
						+ page.getThispagedatacount(pg); row++) {
					if (rs.next()) {
						roughdollcost rdc = new roughdollcost();
						rdc.setThisdate(rs.getDate("thisdate"));
						rdc.setWithdrawmoney(rs.getInt("withdrawmoney"));
						rdc.setTotalcost(rs.getInt("totalcost"));
						rdc.setAlldollindollmachinecount(rs.getInt("alldollindollmachinecount"));
						rdc.setAlldolloutdollmachinecount(rs.getInt("alldolloutdollmachinecount"));
						rlist.add(rdc);
					}
				}
				map.put(pg, rlist);
			}
			close();
			return map;
		}
		return null;
	}

	public List<detaildollcost> getdetaildollcostdata(String thisdate) {
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from detaildollcost a,doll b ");
		sb.append("where to_char(a.thisdate,'yyyy-MM-dd')=? and a.dollname=b.dollname ");
		List<detaildollcost> dlist = new ArrayList<detaildollcost>();
		conn = connectionhelper.getConnection();
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, thisdate);
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					detaildollcost ddc = new detaildollcost();
					ddc.setThisdate(rs.getDate("thisdate"));
					ddc.setDollname(rs.getString("dollname"));
					ddc.setOutdollmachinecount(rs.getInt("outdollmachinecount"));
					ddc.setTodollmachinecount(rs.getInt("todollmachinecount"));
					doll dl = new doll();
					dl.setBuyonedollprice(rs.getInt("buyonedollprice"));
					dl.setDollpictureurl(rs.getString("dollpictureurl"));
					ddc.setThisdoll(dl);
					dlist.add(ddc);
				}
				System.out.println(dlist.size());
				return dlist;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return null;
	}

	public void updateonedetaildata(String thisdate, String dollname, String todollmachinecount,String outdollmachinecount) throws SQLException {
		Connection localconn = connectionhelper.getConnection();
		PreparedStatement localpstmt = null;
		ResultSet localrs = null;
		
		int nulltodollmachinecount = 0;
		int nulloutdollmachinecount = 0;

		StringBuilder sb = new StringBuilder();
		sb.append("update detaildollcost ");
		sb.append("set todollmachinecount=? ");
		sb.append("where to_char(thisdate,'yyyy-MM-dd')=? and dollname=? ");

		StringBuilder sb3 = new StringBuilder();
		sb3.append("update detaildollcost ");
		sb3.append("set outdollmachinecount=? ");
		sb3.append("where to_char(thisdate,'yyyy-MM-dd')=? and dollname=? ");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("select todollmachinecount,outdollmachinecount ");
		sb1.append("from detaildollcost ");
		sb1.append("where to_char(thisdate,'yyyy-MM-dd')=? and dollname=? ");

		StringBuilder sb2 = new StringBuilder();
		sb2.append("update dollmachinestorage ");
		sb2.append("set dollcount=(select dollcount from dollmachinestorage where dollname=?)+? ");
		sb2.append("where dollname=? ");

		StringBuilder sb4 = new StringBuilder();
		sb4.append("update dollstorage ");
		sb4.append("set dollcount=(select dollcount from dollstorage where dollname=?)-? ");
		sb4.append("where dollname=? ");

		StringBuilder sb5 = new StringBuilder();
		sb5.append("update roughdollcost ");
		sb5.append("set alldollindollmachinecount=(select alldollindollmachinecount from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+? ");
		sb5.append("where to_char(thisdate,'yyyy-MM-dd')=? ");

		StringBuilder sb6 = new StringBuilder();
		sb6.append("update roughdollcost ");
		sb6.append("set totalcost=(select totalcost from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+?*(select buyonedollprice from doll where dollname=?),alldolloutdollmachinecount=(select alldolloutdollmachinecount from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+? ");
		sb6.append("where to_char(thisdate,'yyyy-MM-dd')=? ");

		localpstmt = localconn.prepareStatement(sb1.toString());
		localpstmt.setString(1, thisdate);
		localpstmt.setString(2, dollname);
		localrs = localpstmt.executeQuery();
		if (localrs.next()) {
			nulltodollmachinecount = localrs.getInt("todollmachinecount");
			nulloutdollmachinecount = localrs.getInt("outdollmachinecount");
		}
		
		int deference1 = Integer.parseInt(todollmachinecount) - nulltodollmachinecount;
		int deference2 = Integer.parseInt(outdollmachinecount) - nulloutdollmachinecount;
		if (deference1 != 0) {
			localpstmt = localconn.prepareStatement(sb.toString());
			localpstmt.setInt(1, Integer.parseInt(todollmachinecount));
			localpstmt.setString(2, thisdate);
			localpstmt.setString(3, dollname);
			localpstmt.executeUpdate();

			localpstmt = localconn.prepareStatement(sb2.toString());
			localpstmt.setString(1, dollname);
			localpstmt.setInt(2, deference1);
			localpstmt.setString(3, dollname);
			localpstmt.executeUpdate();

			localpstmt = localconn.prepareStatement(sb4.toString());
			localpstmt.setString(1, dollname);
			localpstmt.setInt(2, deference1);
			localpstmt.setString(3, dollname);
			localpstmt.executeUpdate();

			localpstmt = localconn.prepareStatement(sb5.toString());
			localpstmt.setString(1, thisdate);
			localpstmt.setInt(2, deference1);
			localpstmt.setString(3, thisdate);
			localpstmt.executeUpdate();
		}
		if (deference2 != 0) {
			localpstmt = localconn.prepareStatement(sb3.toString());
			localpstmt.setInt(1, Integer.parseInt(outdollmachinecount));
			localpstmt.setString(2, thisdate);
			localpstmt.setString(3, dollname);
			localpstmt.executeUpdate();

			localpstmt = localconn.prepareStatement(sb2.toString());
			localpstmt.setString(1, dollname);
			localpstmt.setInt(2, -deference2);
			localpstmt.setString(3, dollname);
			localpstmt.executeUpdate();

			localpstmt = localconn.prepareStatement(sb6.toString());
			localpstmt.setString(1, thisdate);
			localpstmt.setInt(2, deference2);
			localpstmt.setString(3, dollname);
			localpstmt.setString(4, thisdate);
			localpstmt.setInt(5, deference2);
			localpstmt.setString(6, thisdate);
			localpstmt.executeUpdate();
		}
		if (localrs != null) {
			localrs.close();
		}
		if (localpstmt != null) {
			localpstmt.close();
		}
		if (localconn != null) {
			localconn.close();
		}
	}

	public void multiupdate(List<detaildollcost> dlist, String thisdate) throws SQLException {
		conn = connectionhelper.getConnection();

		StringBuilder sb = new StringBuilder();
		sb.append("delete from detaildollcost ");
		sb.append("where to_char(thisdate,'yyyy-MM-dd')=? and dollname=? ");

		StringBuilder sb1 = new StringBuilder();
		sb1.append("insert into detaildollcost ");
		sb1.append("values(to_date(?,'yyyy-MM-dd'),?,?,?) ");

		StringBuilder sb2 = new StringBuilder();
		sb2.append("select dollname ");
		sb2.append("from detaildollcost ");
		sb2.append("where to_char(thisdate,'yyyy-MM-dd')=? ");

		StringBuilder sb3 = new StringBuilder();
		sb3.append("update dollmachinestorage ");
		sb3.append("set dollcount=(select dollcount from dollmachinestorage where dollname=?)+? ");
		sb3.append("where dollname=? ");

		StringBuilder sb4 = new StringBuilder();
		sb4.append("update dollstorage ");
		sb4.append("set dollcount=(select dollcount from dollstorage where dollname=?)-? ");
		sb4.append("where dollname=? ");

		StringBuilder sb5 = new StringBuilder();
		sb5.append("update roughdollcost ");
		sb5.append("set totalcost=((select totalcost from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+?*(select buyonedollprice from doll where dollname=?)),alldollindollmachinecount=((select alldollindollmachinecount from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+?),alldolloutdollmachinecount=((select alldolloutdollmachinecount from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)+?) ");
		sb5.append("where to_char(thisdate,'yyyy-MM-dd')=? ");
		
		StringBuilder sb6=new StringBuilder();
		sb6.append("update roughdollcost ");
		sb6.append("set totalcost=(select totalcost from roughdollcost where to_char(thisdate,'yyyy-mm-dd')=?)-(select OUTDOLLMACHINECOUNT from detaildollcost where to_char(thisdate,'yyyy-mm-dd')=? and dollname=?)*(select buyonedollprice from doll where dollname=?),alldollindollmachinecount=(select alldollindollmachinecount from roughdollcost where to_char(thisdate,'yyyy-mm-dd')=? )-(select toDOLLMACHINECOUNT from detaildollcost where dollname=? and to_char(thisdate,'yyyy-mm-dd')=?),alldolloutdollmachinecount=(select alldolloutdollmachinecount from roughdollcost where to_char(thisdate,'yyyy-MM-dd')=?)-(select outDOLLMACHINECOUNT from detaildollcost where dollname=? and to_char(thisdate,'yyyy-mm-dd')=?) ");
		sb6.append("where to_char(thisdate,'yyyy-mm-dd')=? ");
		
		
		pstmt = conn.prepareStatement(sb2.toString(), ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		pstmt.setString(1, thisdate);
		rs = pstmt.executeQuery();

		String[] nulldollnames = null;
		if (rs != null) {
			rs.last();
			int datalength=rs.getRow();
			nulldollnames = new String[datalength];
			rs.beforeFirst();
			int i = 0;
			while (rs.next()) {
				nulldollnames[i] = rs.getString(1);
				i++;
			}
		}
		for (detaildollcost ddc : dlist) {
			int a = 0;
			for (int k = 0; k < nulldollnames.length; k++) {
				if (ddc.getDollname().equals(nulldollnames[k])) {
					nulldollnames[k] = null;
					a = 1;
					break;
				}
			}
			if (a == 0) {
				pstmt = conn.prepareStatement(sb1.toString());
				pstmt.setString(1, thisdate);
				pstmt.setString(2, ddc.getDollname());
				pstmt.setInt(3, ddc.getTodollmachinecount());
				pstmt.setInt(4, ddc.getOutdollmachinecount());
				pstmt.executeUpdate();

				pstmt = conn.prepareStatement(sb3.toString());
				pstmt.setString(1, ddc.getDollname());
				pstmt.setInt(2, ddc.getTodollmachinecount());
				pstmt.setString(3, ddc.getDollname());
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sb3.toString());
				pstmt.setString(1, ddc.getDollname());
				pstmt.setInt(2, -ddc.getOutdollmachinecount());
				pstmt.setString(3, ddc.getDollname());
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sb4.toString());
				pstmt.setString(1, ddc.getDollname());
				pstmt.setInt(2, ddc.getTodollmachinecount());
				pstmt.setString(3, ddc.getDollname());
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sb5.toString());
				pstmt.setString(1, thisdate);
				pstmt.setInt(2, ddc.getOutdollmachinecount());
				pstmt.setString(3, ddc.getDollname());
				pstmt.setString(4, thisdate);
				pstmt.setInt(5, ddc.getTodollmachinecount());
				pstmt.setString(6, thisdate);
				pstmt.setInt(7, ddc.getOutdollmachinecount());
				pstmt.setString(8,thisdate);
				pstmt.executeUpdate();
				
			}
			if (a == 1) {
				updateonedetaildata(thisdate, ddc.getDollname() + "", ddc.getTodollmachinecount() + "",
						ddc.getOutdollmachinecount() + "");
			}
		}
		for (int p = 0; p < nulldollnames.length; p++) {
			if (nulldollnames[p] != null) {
				pstmt = conn.prepareStatement(sb6.toString());
				pstmt.setString(1, thisdate);
				pstmt.setString(2,thisdate);
				pstmt.setString(3, nulldollnames[p]);
				pstmt.setString(4, nulldollnames[p]);
				pstmt.setString(5, thisdate);
				pstmt.setString(6, nulldollnames[p]);
				pstmt.setString(7, thisdate);
				pstmt.setString(8, thisdate);
				pstmt.setString(9, nulldollnames[p]);
				pstmt.setString(10, thisdate);
				pstmt.setString(11, thisdate);
				pstmt.executeUpdate();
				
				pstmt = conn.prepareStatement(sb.toString());
				pstmt.setString(1, thisdate);
				pstmt.setString(2, nulldollnames[p]);
				pstmt.executeUpdate();
			}else{
				continue;
			}
		}
		close();
	}
	
	public List<doll> getalldolldata() {
		conn = connectionhelper.getConnection();
		List<doll> dlist = new ArrayList<doll>();
		StringBuilder sb = new StringBuilder();
		sb.append("select * ");
		sb.append("from doll ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			rs = pstmt.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					doll d = new doll();
					d.setDollname(rs.getString("dollname"));
					d.setBuyonedollprice(rs.getInt("buyonedollprice"));
					d.setDollpictureurl(rs.getString("dollpictureurl"));
					dlist.add(d);
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

	// ���憓�
	public void insertnewdoll(String dollname, String dollpictureurl, String buyonedollprice) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("insert into doll ");
		sb.append("values(?,?,?) ");
		StringBuilder sb2=new StringBuilder();
		sb2.append("insert into dollmachinestorage ");
		sb2.append("values(?,0)");
		StringBuilder sb3=new StringBuilder();
		sb3.append("insert into dollstorage ");
		sb3.append("values(?,0)");
		StringBuilder sb1 = new StringBuilder();
		sb1.append("select count(*) ");
		sb1.append("from doll ");
		sb1.append("where dollname=? ");

		try {
			pstmt = conn.prepareStatement(sb1.toString());
			pstmt.setString(1, dollname);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if (rs.getInt(1) == 0) {
					pstmt = conn.prepareStatement(sb.toString());
					pstmt.setString(1, dollname);
					pstmt.setInt(2, Integer.parseInt(buyonedollprice));
					pstmt.setString(3, dollpictureurl);
					pstmt.executeUpdate();
					pstmt=conn.prepareStatement(sb3.toString());
					pstmt.setString(1,dollname);
					pstmt.executeUpdate();
					pstmt=conn.prepareStatement(sb2.toString());
					pstmt.setString(1,dollname);
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void updateroughwithdraw(String thisdate, String withdrawmoney) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("update roughdollcost ");
		sb.append("set withdrawmoney=? ");
		sb.append("where to_char(thisdate,'yyyy-MM-dd')=? ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setInt(1, Integer.parseInt(withdrawmoney));
			pstmt.setString(2, thisdate);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	public void insertroughdata() {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("insert into roughdollcost ");
		sb.append("values(sysdate,0,0,0,0) ");
		StringBuilder sb1 = new StringBuilder();
		sb1.append("select count(*) ");
		sb1.append("from roughdollcost ");
		sb1.append("where to_char(thisdate,'yyyy-MM-dd')=? ");
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String nowstring = sdf.format(now);
		try {
			pstmt = conn.prepareStatement(sb1.toString());
			pstmt.setString(1, nowstring);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if (rs.getInt(1) == 0) {
					pstmt = conn.prepareStatement(sb.toString());
					pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}
	public String[] getalldollnames(){
		conn=connectionhelper.getConnection();
		String[] dollnames=null;
		StringBuilder sb=new StringBuilder();
		sb.append("select dollname ");
		sb.append("from doll ");
		try {
			pstmt=conn.prepareStatement(sb.toString(),ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs=pstmt.executeQuery();
			if(rs!=null){
				rs.last();
				int rows=rs.getRow();
				dollnames=new String[rows];
				rs.beforeFirst();
				int i=0;
				while(rs.next()){
					dollnames[i]=rs.getString(1);
					i++;
				}
				return dollnames;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			close();
		}
		return null;
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
