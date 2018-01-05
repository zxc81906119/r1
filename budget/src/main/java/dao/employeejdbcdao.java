package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class employeejdbcdao implements employeedao {
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;

	public boolean hasid(String id) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(id) ");
		sb.append("from employhome ");
		sb.append("where id=? ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs != null && rs.next()) {
				if (rs.getInt("COUNT(ID)") == 1) {
					return true;
				} else {
					return false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return false;
	}

	public boolean rightpassword(String id, String password) {
		conn = connectionhelper.getConnection();
		StringBuilder sb = new StringBuilder();
		sb.append("select count(*) ");
		sb.append("from employhome ");
		sb.append("where id=? and password=? ");
		try {
			pstmt = conn.prepareStatement(sb.toString());
			pstmt.setString(1, id);
			pstmt.setString(2, password);
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
		}finally {
			close();
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
