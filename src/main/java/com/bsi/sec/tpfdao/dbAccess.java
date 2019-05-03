package com.bsi.sec.tpfdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class dbAccess {
	/**
	 * 
	 */
	private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(dbAccess.class);

	@Autowired
        @Qualifier("tpfDataSource")
	private BasicDataSource ds;

	static protected int ORA_SQL_OKAY = 0;
	static final protected int ORA_SQL_ROW_NOT_FOUND = 100;
	static final protected int ORA_SQL_CHILD_EXISTS = 2292;
	static final protected int ORA_SQL_DUPLICATE_ROW = 1;
	static final protected int ORA_SQL_NO_PARENT = 2291;
	static final protected int ORA_SQL_TABLE_NOT_EXIST = 942;
	static final protected int ORA_SQL_MORE_LESS_VAL = 947;
	static final protected int ORA_SQL_VAL_TOO_LONG = 1401;
	static final protected int ORA_SQL_SYNTAX_ERROR = 917;
	static final protected int DB2_SQL_OKAY = 0;
	static final protected int DB2_SQL_ROW_NOT_FOUND = 100;
	static final protected int DB2_SQL_CHILD_EXISTS = -532;
	static final protected int DB2_SQL_DUPLICATE_ROW = -803;
	static final protected int DB2_SQL_NO_PARENT = -530;
	static final protected int DB2_SQL_TABLE_NOT_EXIST = -204;
	static final protected int DB2_SQL_MORE_LESS_VAL = -117;
	static final protected int DB2_SQL_VAL_TOO_LONG = -433;
	static final protected int DB2_SQL_SYNTAX_ERROR = -10;
	static final protected int INF_SQL_OKAY = 0;
	static final protected int INF_SQL_ROW_NOT_FOUND = 100;
	static final protected int INF_SQL_CHILD_EXISTS = -692;
	static final protected int INF_SQL_DUPLICATE_ROW = -239;
	static final protected int INF_SQL_DUPLICATE_ROW_LOGON = -268;
	static final protected int INF_SQL_NO_PARENT = -691;
	static final protected int INF_SQL_TABLE_NOT_EXIST = -206;
	static final protected int INF_SQL_MORE_LESS_VAL = -236;
	static final protected int INF_SQL_VAL_TOO_LONG = 99998;
	static final protected int INF_SQL_SYNTAX_ERROR = -201;
	static final protected int SQV_SQL_OKAY = 0;
	static final protected int SQV_SQL_ROW_NOT_FOUND = 100;
	static final protected int SQV_SQL_CHILD_EXISTS = 547;
	static final protected int SQV_SQL_DUPLICATE_ROW = 2627;
	static final protected int SQV_SQL_NO_PARENT = 547;
	static final protected int SQV_SQL_TABLE_NOT_EXIST = 208;
	static final protected int SQV_SQL_MORE_LESS_VAL = 213;
	static final protected int SQV_SQL_VAL_TOO_LONG = 8512;
	static final protected int SQV_SQL_SYNTAX_ERROR = 170;
	static final protected int SYB_SQL_OKAY = 0;
	static final protected int SYB_SQL_ROW_NOT_FOUND = 100;
	static final protected int SYB_SQL_CHILD_EXISTS = 547;
	static final protected int SYB_SQL_DUPLICATE_ROW = 2601;
	static final protected int SYB_SQL_NO_PARENT = -546;
	static final protected int SYB_SQL_TABLE_NOT_EXIST = 208;
	static final protected int SYB_SQL_MORE_LESS_VAL = 213;
	static final protected int SYB_SQL_VAL_TOO_LONG = 99999;
	static final protected int SYB_SQL_SYNTAX_ERROR = 102;
	static final public int SQL_OKAY = 0;
	static final public int SQL_ROW_NOT_FOUND = 100;
	static final public int SQL_CHILD_EXISTS = 101;
	static final public int SQL_DUPLICATE_ROW = 102;
	static final public int SQL_NO_PARENT = 103;
	static final public int SQL_TABLE_NOT_EXIST = 104;
	static final public int SQL_MORE_LESS_VAL = 105;
	static final public int SQL_VAL_TOO_LONG = 106;
	static final public int SQL_SYNTAX_ERROR = 107;
	static final public int SQL_UNMAPPED_ERROR = 9999;
	static protected int MAXDB_SQL_OKAY = 0;
	static protected int MAXDB_SQL_ROW_NOT_FOUND = 100;
	static protected int MAXDB_SQL_NO_PARENT = 350;
	static protected int MAXDB_SQL_CHILD_EXISTS = 350;
	static protected int MAXDB_SQL_FIELD_IN_USE = 350;
	static protected int MAXDB_SQL_MORE_THAN_ONE_ROW = -7015;
	static protected int MAXDB_SQL_DUPLICATE_ROW = 200;
	private Connection dbConnection = null;
	private Statement dbStatement = null;
	private String dbmsName = null;
	private String dbmsVersion = null;
	private String lastError = null;

	private void dbConnect() {
		try {
			dbConnection = ds.getConnection();
		} catch (Exception x) {
			setLastError(String.format("Unable to load Database driver: %s", x.getMessage()));
			System.out.println(getLastError());
		}
	}

	public dbAccess() {
		dbConnect();
	}

	@Override
	public void finalize() {
		close();
	}

	/**
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedStatement(String sql) throws SQLException {
		if (dbConnection == null || dbConnection.isClosed()) {
			dbConnect();
		}

		if (dbConnection != null) {
			return dbConnection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		}

		return null;
	}

	/**
	 * @param sql
	 * @param resultSetType
	 * @param resultSetConcurrency
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedStatement(String sql, int resultSetType, int resultSetConcurrency)
			throws SQLException {
		if (dbConnection == null || dbConnection.isClosed()) {
			dbConnect();
		}

		if (dbConnection != null) {
			return dbConnection.prepareStatement(sql, resultSetType, resultSetConcurrency);
		}

		return null;
	}

	public String GetdbmsName() {
		return dbmsName;
	}

	public String GetdbmsVersion() {
		return dbmsVersion;
	}

	public void executeSQLNR(String SQLString) throws SQLException {
		if (dbConnection != null && !dbConnection.isClosed()) {
			dbStatement = dbConnection.createStatement();
			int n = dbStatement.executeUpdate(SQLString);
			log.debug("No. of rows updated : " + n);
		}
	}

	public ResultSet executeSQL(String SQLString) throws SQLException {
		ResultSet dbResult = null;
		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbStatement = dbConnection.createStatement();
				dbResult = dbStatement.executeQuery(SQLString);
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw ex;
		}

		return dbResult;
	}

	public int getIntValue(String SQLString) {
		int result = 0;
		ResultSet dbResult = null;
		try {
			dbResult = executeSQL(SQLString);
			if (dbResult != null && dbResult.next()) {
				result = dbResult.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbResult != null) {
				try {
					dbResult.close();
					close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	public String getStringValue(String SQLString) {
		String result = null;
		ResultSet dbResult = null;
		try {
			dbResult = executeSQL(SQLString);
			if (dbResult != null && dbResult.next()) {
				result = dbResult.getString(1).trim();
			}

			dbResult.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dbResult != null) {
				try {
					dbResult.close();
					close();
				} catch (SQLException e) {
				}
			}
		}
		return result;
	}

	// dbAccess class is not designed well, work around is to prevent JVM and
	// other threads calling the close() function simultaneously.
	public synchronized void close() {
		try {
			if (dbStatement != null) {
				dbStatement.close();
				dbStatement = null;
			}

			/*
			 * if (dbConnection != null) { dbConnection.close(); dbConnection = null; }
			 */

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public synchronized void close(boolean explicit) {
		try {
			if (dbStatement != null) {
				dbStatement.close();
				dbStatement = null;
			}

			if (dbConnection != null && explicit) {
				dbConnection.commit();
				dbConnection.close();
				dbConnection = null;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static public int GetMappedErrorCode(int VendorCode) {
		if (VendorCode == ORA_SQL_ROW_NOT_FOUND || VendorCode == DB2_SQL_ROW_NOT_FOUND
				|| VendorCode == INF_SQL_ROW_NOT_FOUND || VendorCode == SQV_SQL_ROW_NOT_FOUND
				|| VendorCode == SYB_SQL_ROW_NOT_FOUND || VendorCode == MAXDB_SQL_ROW_NOT_FOUND) {
			return SQL_ROW_NOT_FOUND;
		} else if (VendorCode == ORA_SQL_CHILD_EXISTS || VendorCode == DB2_SQL_CHILD_EXISTS
				|| VendorCode == INF_SQL_CHILD_EXISTS || VendorCode == SQV_SQL_CHILD_EXISTS
				|| VendorCode == SYB_SQL_CHILD_EXISTS || VendorCode == MAXDB_SQL_CHILD_EXISTS) {
			return SQL_CHILD_EXISTS;
		} else if (VendorCode == ORA_SQL_DUPLICATE_ROW || VendorCode == DB2_SQL_DUPLICATE_ROW
				|| VendorCode == INF_SQL_DUPLICATE_ROW || VendorCode == INF_SQL_DUPLICATE_ROW_LOGON
				|| VendorCode == SQV_SQL_DUPLICATE_ROW || VendorCode == SYB_SQL_DUPLICATE_ROW
				|| VendorCode == MAXDB_SQL_DUPLICATE_ROW) {
			return SQL_DUPLICATE_ROW;
		} else if (VendorCode == ORA_SQL_NO_PARENT || VendorCode == DB2_SQL_NO_PARENT || VendorCode == INF_SQL_NO_PARENT
				|| VendorCode == SQV_SQL_NO_PARENT || VendorCode == SYB_SQL_NO_PARENT
				|| VendorCode == MAXDB_SQL_NO_PARENT) {
			return SQL_NO_PARENT;
		} else if (VendorCode == ORA_SQL_TABLE_NOT_EXIST || VendorCode == DB2_SQL_TABLE_NOT_EXIST
				|| VendorCode == INF_SQL_TABLE_NOT_EXIST || VendorCode == SQV_SQL_TABLE_NOT_EXIST
				|| VendorCode == SYB_SQL_TABLE_NOT_EXIST) {
			return SQL_TABLE_NOT_EXIST;
		} else if (VendorCode == ORA_SQL_VAL_TOO_LONG || VendorCode == DB2_SQL_VAL_TOO_LONG
				|| VendorCode == INF_SQL_VAL_TOO_LONG || VendorCode == SQV_SQL_VAL_TOO_LONG
				|| VendorCode == SYB_SQL_VAL_TOO_LONG) {
			return SQL_VAL_TOO_LONG;
		} else if (VendorCode == ORA_SQL_SYNTAX_ERROR || VendorCode == DB2_SQL_SYNTAX_ERROR
				|| VendorCode == INF_SQL_SYNTAX_ERROR || VendorCode == SQV_SQL_SYNTAX_ERROR
				|| VendorCode == SYB_SQL_SYNTAX_ERROR) {
			return SQL_SYNTAX_ERROR;
		}

		return SQL_UNMAPPED_ERROR;
	}

	public String getLastError() {
		return lastError;
	}

	public void setLastError(String lastError) {
		this.lastError = lastError;
	}

	public Connection getDbConnection() {
		return dbConnection;
	}

	public void setDbConnection(Connection dbConnection) {
		this.dbConnection = dbConnection;
	}

	public void close(ResultSet rs) {
		try {
			if (rs != null && !rs.isClosed())
				rs.close();
		} catch (SQLException e) {
			log.warn("Unable to close resultSet!", e);
		}
	}

	public void close(PreparedStatement ps) {
		try {
			if (ps != null && !ps.isClosed())
				ps.close();
		} catch (SQLException e) {
			log.warn("Unable to close PreparedStatement!", e);
		}
	}

	public void close(ResultSet rs, PreparedStatement ps) {
		close(rs);
		close(ps);
	}

	public void close(ResultSet rs, PreparedStatement ps, boolean closeConn) {
		close(rs);
		close(ps);
		close(true);
	}

	public void close(PreparedStatement ps, boolean closeConn) {
		close(ps);
		close(closeConn);
	}
}
