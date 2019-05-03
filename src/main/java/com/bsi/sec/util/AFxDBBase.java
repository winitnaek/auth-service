package com.bsi.sec.util;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AFxDBBase {
    private static final Logger log = LogManager.getLogger(AFxDBBase.class);
	
    static private String  dbmsName    = null;
    static private String  dbmsVersion = null;

    static public int ORACLE    = 0;
    static public int DB2       = 1;
    static public int INFORMIX  = 2;
    static public int SYBASE    = 3;
    static public int SQLSERVER = 4;
    static public int MAXDB     = 5;
    static public int UNKNOWN   = -1;

    
    public static String SUBSTRING = "SUBSTR";
    public static String CONCAT = "+";
    public static String ISNULL = "ISNULL";
    public static String ISBLANK = " ";
    public static String TO_NUM = " ";
    public static String TO_CHAR = " ";
    public static String NULL = "NULL";
    
    static public int BSI_NOT_MATCH = -8001;
    static public int BSI_NO_FORMAT = -8002;

    static public int DBType  = UNKNOWN;

    static protected long ORA_SQL_OKAY          = 0;
    static protected long ORA_SQL_ROW_NOT_FOUND = 100;
    static protected long ORA_SQL_CHILD_EXISTS  = 2292;
    static protected long ORA_SQL_DUPLICATE_ROW = 1;
    static protected long ORA_SQL_NO_PARENT     = 2291;
    static protected long ORA_SQL_TABLE_NOT_EXIST = 942;
    static protected long ORA_SQL_MORE_LESS_VAL = 947; //913
    static protected long ORA_SQL_VAL_TOO_LONG  = 1401;
    static protected long ORA_SQL_SYNTAX_ERROR  = 917;  //missing comma

    static protected long DB2_SQL_OKAY          = 0;
    static protected long DB2_SQL_ROW_NOT_FOUND = 100;
    static protected long DB2_SQL_CHILD_EXISTS  = -532;
    static protected long DB2_SQL_DUPLICATE_ROW = -803;
    static protected long DB2_SQL_NO_PARENT     = -530;
    static protected long DB2_SQL_TABLE_NOT_EXIST = -204;
    static protected long DB2_SQL_MORE_LESS_VAL = -117;
    static protected long DB2_SQL_VAL_TOO_LONG  = -433;
    static protected long DB2_SQL_SYNTAX_ERROR  = -10;

    static protected long INF_SQL_OKAY          = 0;
    static protected long INF_SQL_ROW_NOT_FOUND = 100;
    static protected long INF_SQL_CHILD_EXISTS  = -692;
    static protected long INF_SQL_DUPLICATE_ROW = -239;  //on log -yfg
    static protected long INF_SQL_DUPLICATE_ROW_LOGON = -268; //log turned on -yfg

    static protected long INF_SQL_NO_PARENT     = -691;
    static protected long INF_SQL_TABLE_NOT_EXIST = -206;
    static protected long INF_SQL_MORE_LESS_VAL = -236;
    static protected long INF_SQL_VAL_TOO_LONG  = 99998; // auto truncate
    static protected long INF_SQL_SYNTAX_ERROR  = -201;

    static protected long SQV_SQL_OKAY          = 0;
    static protected long SQV_SQL_ROW_NOT_FOUND = 100;
    static protected long SQV_SQL_CHILD_EXISTS  = 547;
    static protected long SQV_SQL_DUPLICATE_ROW = 2627;
    static protected long SQV_SQL_NO_PARENT     = 547;
    static protected long SQV_SQL_TABLE_NOT_EXIST = 208;
    static protected long SQV_SQL_MORE_LESS_VAL = 213;
    static protected long SQV_SQL_VAL_TOO_LONG  = 8512;
    static protected long SQV_SQL_SYNTAX_ERROR  = 170;

    static protected  long SYB_SQL_OKAY              = 0;
    static protected  long SYB_SQL_ROW_NOT_FOUND     = 100;
    static protected  long SYB_SQL_CHILD_EXISTS      = 547;
    static protected  long SYB_SQL_DUPLICATE_ROW     = 2601;
    static protected  long SYB_SQL_NO_PARENT         = -546;
    static protected long SYB_SQL_TABLE_NOT_EXIST   = 208;
    static protected long SYB_SQL_MORE_LESS_VAL     = 213;
    static protected long SYB_SQL_VAL_TOO_LONG      = 99999;   //auto truncate
    static protected long SYB_SQL_SYNTAX_ERROR      = 102;

    
    static protected  long MAXDB_SQL_OKAY                = 0;
    static protected  long MAXDB_SQL_ROW_NOT_FOUND       =  100;
    static protected  long MAXDB_SQL_NO_PARENT           =  350;
    static protected  long MAXDB_SQL_CHILD_EXISTS        =  350;
    static protected  long MAXDB_SQL_FIELD_IN_USE        =  350;
    static protected  long MAXDB_SQL_MORE_THAN_ONE_ROW   = -7015;
    static protected  long MAXDB_SQL_DUPLICATE_ROW       =  200;

    static private long SQL_OKAY             = 0;
    static private long SQL_ROW_NOT_FOUND    = -1; //0;
    static public long SQL_CHILD_EXISTS     = -1; //0;
    static public long SQL_DUPLICATE_ROW    = -1; //0;
    static public long SQL_NO_PARENT        = -1; //0;
    static public long SQL_TABLE_NOT_EXIST  = -1; //0;
    static public long SQL_MORE_LESS_VAL    = -1; //0;
    static public long SQL_VAL_TOO_LONG     = -1; //0;
    static public long SQL_SYNTAX_ERROR     = -1; //0;

    
	private static boolean checked = false;

	public String getdbmsName() throws SQLException {
		return dbmsName;
	}

	public String getdbmsVersion() throws SQLException {
		return dbmsVersion;
	}


	static public void setSqlCodesForDBMS(String productName,
			String productVersion) {
		if (!checked) {
			log.info("Setting database setting");
			dbmsName = productName;
			dbmsVersion = productVersion;
			if (dbmsName.indexOf("ORACLE") != -1
					|| dbmsName.indexOf("Oracle") != -1) {
				SQL_OKAY = ORA_SQL_OKAY;
				SQL_ROW_NOT_FOUND = ORA_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = ORA_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = ORA_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = ORA_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = ORA_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = ORA_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = ORA_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = ORA_SQL_SYNTAX_ERROR;

				DBType = ORACLE;
				SUBSTRING = "SUBSTR";
				CONCAT = "||";
				ISNULL = "NVL";
				TO_CHAR = "TO_CHAR";
				TO_NUM = "TO_NUMBER";
			} else if (dbmsName.indexOf("DB2") != -1
					|| dbmsName.indexOf("db2") != -1) {
				SQL_OKAY = DB2_SQL_OKAY;
				SQL_ROW_NOT_FOUND = DB2_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = DB2_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = DB2_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = DB2_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = DB2_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = DB2_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = DB2_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = DB2_SQL_SYNTAX_ERROR;

				DBType = DB2;
				SUBSTRING = "SUBSTR";
				CONCAT = "||";
				ISNULL = "COALESCE";
			} else if (dbmsName.indexOf("INFORMIX") != -1
					|| dbmsName.indexOf("Informix") != -1) {
				SQL_OKAY = INF_SQL_OKAY;
				SQL_ROW_NOT_FOUND = INF_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = INF_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = INF_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = INF_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = INF_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = INF_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = INF_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = INF_SQL_SYNTAX_ERROR;

				DBType = INFORMIX;
				SUBSTRING = "SUBSTRING";
				CONCAT = "+";
				ISNULL = "NVL";
			} else if (dbmsName.indexOf("SYBASE") != -1
					|| dbmsName.indexOf("Sybase") != -1) {
				// Sybase section must be put before "SQL SERVER" secion since
				// it's full name is "SYBASE SQL SERVER"
				SQL_OKAY = SYB_SQL_OKAY;
				SQL_ROW_NOT_FOUND = SYB_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = SYB_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = SYB_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = SYB_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = SYB_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = SYB_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = SYB_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = SYB_SQL_SYNTAX_ERROR;

				DBType = SYBASE;
				SUBSTRING = "SUBSTRING";
				CONCAT = "+";
				ISNULL = "ISNULL";
			} else if (dbmsName.indexOf("SQL SERVER") != -1
					|| dbmsName.indexOf("SQL Server") != -1) {
				SQL_OKAY = SQV_SQL_OKAY;
				SQL_ROW_NOT_FOUND = SQV_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = SQV_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = SQV_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = SQV_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = SQV_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = SQV_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = SQV_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = SQV_SQL_SYNTAX_ERROR;

				DBType = SQLSERVER;
				SUBSTRING = "SUBSTRING";
				CONCAT = "+";
				ISNULL = "ISNULL";
				TO_CHAR = " ";
				TO_NUM = " ";
				NULL = "NULL";
			}  else if (dbmsName.indexOf("SAP DB") != -1
					|| dbmsName.indexOf("sap db") != -1) {
				SQL_OKAY = MAXDB_SQL_OKAY;
				SQL_ROW_NOT_FOUND = MAXDB_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = MAXDB_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = MAXDB_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = MAXDB_SQL_NO_PARENT;

				DBType = MAXDB;
				SUBSTRING = "SUBSTR";
				CONCAT = "||";
				ISNULL = "value";
			}else // db2 is your default RRR 11/6/1
			{
				SQL_OKAY = DB2_SQL_OKAY;
				SQL_ROW_NOT_FOUND = DB2_SQL_ROW_NOT_FOUND;
				SQL_CHILD_EXISTS = DB2_SQL_CHILD_EXISTS;
				SQL_DUPLICATE_ROW = DB2_SQL_DUPLICATE_ROW;
				SQL_NO_PARENT = DB2_SQL_NO_PARENT;
				SQL_TABLE_NOT_EXIST = DB2_SQL_TABLE_NOT_EXIST;
				SQL_MORE_LESS_VAL = DB2_SQL_MORE_LESS_VAL;
				SQL_VAL_TOO_LONG = DB2_SQL_VAL_TOO_LONG;
				SQL_SYNTAX_ERROR = DB2_SQL_SYNTAX_ERROR;

				DBType = DB2;
				SUBSTRING = "SUBSTR";
				CONCAT = "+";
				ISNULL = "ISNULL";
			}
			checked = true;
			log.info("DBType "+DBType);
			log.info("SUBSTRING "+SUBSTRING);
			log.info("CONCAT "+CONCAT);
			
		}
	}

	public static long getBSIDupRowCode(int nativeCode) {
		if (dbmsName.indexOf("INFORMIX") != -1
				|| dbmsName.indexOf("Informix") != -1) {
			if (nativeCode == INF_SQL_DUPLICATE_ROW_LOGON
					|| nativeCode == INF_SQL_DUPLICATE_ROW) {
				// System.err.println("nativeCode="+nativeCode +
				// "       new code="+SQL_DUPLICATE_ROW );
				return SQL_DUPLICATE_ROW;
			}
		}
		return nativeCode;
	}

	public synchronized static String getProp(String prop) {
		String classString = null;
		try {
			Properties props = new Properties();
			FileInputStream in;
			String InitPath = System.getProperty("user.dir");
			String Sep = System.getProperty("file.separator");
			in = new FileInputStream(InitPath + Sep + "tf80.init");
			props.load(in);
			classString = props.getProperty(prop);
			in.close();
		} catch (Exception ignore) {
		}
		return classString;
	}
}
