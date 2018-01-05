package dao;

import java.sql.*;
import javax.sql.*;
import javax.naming.*;

public class connectionhelper {
	public static Connection getConnection() {
		Connection conn = null;
		try {
			Context initContext = new InitialContext();
			Context envContext = (Context) initContext.lookup("java:/comp/env");
			DataSource ds = (DataSource) envContext.lookup("jdbc/budget");
			conn = ds.getConnection();
		} catch (Exception e) {
			throw new RuntimeException("ㄟ不要鬧" + e.getMessage());
		}
		return conn;
	}
}
