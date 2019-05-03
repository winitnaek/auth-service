package com.bsi.sec.tpfdao;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;



/**
 * @author SitaP
 *
 */
@Component
public class BtoDsetDao implements Serializable {
  
  private static final Logger log = LogManager.getLogger(BtoDsetDao.class);
  private static final long serialVersionUID = 1L;
  private int dsId;
  private int custId;
  private String name;
  private String descr;
  private boolean enabled = false;
  private Timestamp createdAt;
  private Timestamp updatedAt;
  
  //added for TPF4.0.b
  private String onBoardingDone;
  
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }


  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  public Timestamp getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Timestamp createdAt) {
    this.createdAt = createdAt;
  }

  public Timestamp getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Timestamp updatedAt) {
    this.updatedAt = updatedAt;
  }


  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getDsId() {
    return dsId;
  }

  public void setDsId(int dsId) {
    this.dsId = dsId;
  }

  /**
 * @return the custId
 */
public int getCustId() {
	return custId;
}

/**
 * @param custId the custId to set
 */
public void setCustId(int custId) {
	this.custId = custId;
}

private static final String GET_DSET = "SELECT DATASETID,NAME,DESCR,ENABLED,CREATEDAT,UPDATEDAT from OBX.BTODSET where datasetid = ?" ;
  private static final String GET_DSETID_BYNAME = "SELECT DATASETID,NAME,DESCR,ENABLED,CREATEDAT,UPDATEDAT from OBX.BTODSET where name = ?" ;
   //"SELECT D.DATASETID,D.NAME,D.ENABLED,C.CUSTNAME FROM OBX.BTODSET D,OBX.BTOCUST C  where  D.CUSTID in( SELECT CUSTID FROM OBX.BTODSET D WHERE D.DATASETID=?) AND D.CUSTID=C.CUSTID ORDER BY D.ENABLED DESC" ;
  
  /*private static final String GET_DSETS_LOGINID ="SELECT D.DATASETID,D.NAME,D.ENABLED,C.CUSTNAME FROM OBX.BTODSET D,OBX.BTOCUST C,"
  		+ " OBX.BTOLOGIN L  where  D.CUSTID in( SELECT CUSTID FROM OBX.BTODSET D WHERE D.DATASETID=?) AND D.CUSTID=C.CUSTID "
  		+ " and L.DATASETID=D.DATASETID and L.LOGINID=? ORDER BY D.ENABLED DESC";
  */
  /*private static final String GET_DSETS_SAMLID ="SELECT D.DATASETID,D.NAME,D.ENABLED,C.CUSTNAME FROM OBX.BTODSET D,OBX.BTOCUST C,"
	+ " OBX.BTOLOGIN L  where  D.CUSTID in( SELECT CUSTID FROM OBX.BTODSET D WHERE D.DATASETID=?) AND D.CUSTID=C.CUSTID "
	+ " and L.DATASETID=D.DATASETID and L.SAMLUID=? ORDER BY D.ENABLED DESC";*/

  private static final String GET_DSETS_LOGINID =  "SELECT D.DATASETID,D.NAME,D.ENABLED,C.CUSTNAME,CON.VALUE1 FROM OBX.BTODSET D,OBX.BTOCUST C, OBX.BTOCONFIG CON, OBX.BTOLOGIN L WITH (NOLOCK)  where  D.CUSTID in( "
		  		+ " SELECT CUSTID FROM OBX.BTODSET D WHERE D.DATASETID=? ) AND CON.DATASETID=L.DATASETID AND"
		  		+ " D.CUSTID=C.CUSTID and L.DATASETID=D.DATASETID and CON.ITEMNAME=? AND L.LOGINID=? ORDER BY D.ENABLED DESC";
	
  private static final String GET_DSETS_SAMLID ="SELECT D.DATASETID,D.NAME,D.ENABLED,C.CUSTNAME,CON.VALUE1 FROM OBX.BTODSET D,"
  		+ "OBX.BTOCUST C, OBX.BTOCONFIG CON, OBX.BTOLOGIN L WITH (NOLOCK)  where  D.CUSTID in(SELECT CUSTID FROM OBX.BTODSET D WHERE D.DATASETID=? )"
  		+ " AND CON.DATASETID=L.DATASETID AND D.CUSTID=C.CUSTID and L.DATASETID=D.DATASETID AND CON.ITEMNAME=? and L.SAMLUID=? ORDER BY D.ENABLED DESC";
  
  private static final String IS_DSETNAME_EXISTS = "SELECT DATASETID,NAME from OBX.BTODSET where name = ?" ;
  private static final String IS_DSETNAME_EXISTS_FOR_OTHER_CUSTOMERS = "SELECT NAME from OBX.BTODSET where name = ? and datasetid <> ? " ;
  private static final String UPDATE_DSETNAME_SQL = "UPDATE OBX.BTODSET SET NAME =?, UPDATEDAT =? WHERE DATASETID = ? ";
  private static final String UPDATE_DSETDESCR_SQL = "UPDATE OBX.BTODSET SET DESCR =?, UPDATEDAT =? WHERE DATASETID = ? ";
  private static final String UPDATE_DSET_ENABLE_SQL = "UPDATE OBX.BTODSET SET ENABLED = ?, UPDATEDAT=? WHERE  DATASETID = ?";
  private static final String INTERNAL_DEFAULT_INFO_UPDATEDT = "UPDATE OBX.BTODSET SET UPDATEDAT =? WHERE DATASETID = ? ";
  private static final String SELECT_CUSTID_SQL = "SELECT CUSTID FROM OBX.BTODSET WHERE DATASETID = ?";
  private static final String SELECT_DATASETID_BYCUST = "SELECT DATASETID FROM OBX.BTODSET WHERE CUSTID=?";
  private static final String SELECT_NAME_BYCUST = "SELECT NAME FROM OBX.BTODSET WHERE CUSTID=?";
  private static final String SELECT_CUSTID_BY_DSETID="SELECT CUSTID FROM OBX.BTODSET WHERE DATASETID=?";
  private static final String SELECT_CUST_NAME_BY_DSETID="SELECT C.CUSTNAME FROM OBX.BTODSET D, OBX.BTOCUST C WHERE D.CUSTID=C.CUSTID AND D.DATASETID=? ";
  
  
  private static final String TBL_BTODSET="OBX.BTODSET";
  
	
  private static final String INSERT_SQL = "INSERT INTO OBX.BTODSET (NAME,DESCR,ENABLED,CREATEDAT,CUSTID)"
											+ "VALUES (?, ?, ?, ?,?)";
  
  private static final String UPDATE_DSETDESC_SQL = "UPDATE OBX.BTODSET SET DESCR =?, UPDATEDAT =? WHERE DATASETID = ? ";
 
  public static BtoDsetDao getDset(int dsId)
      throws Exception {
    BtoDsetDao dset = null;
    PreparedStatement pstmt = null; 
    ResultSet rs= null;
    dbAccess dba=null;
  
    try {
      dba = new dbAccess ();
          pstmt = dba.getPreparedStatement(GET_DSET);
          pstmt.setInt(1,dsId);
          rs = pstmt.executeQuery(); 
      if (rs != null && rs.next()) {
        dset = new BtoDsetDao();
        dset.dsId = rs.getInt("DATASETID");
        dset.name = rs.getString("NAME");
        dset.descr = rs.getString("DESCR");
        dset.enabled=rs.getBoolean("ENABLED");
        
      }
    } catch (SQLException e) {
      log.error("Unble to execute query!", e);
    } finally {
      if (dba != null)
        dba.close(rs, pstmt);
    }
    return dset;
  }
  public static BtoDsetDao getDsetByDSName(String dsName)
	      throws Exception {
	   BtoDsetDao dset = null;
	    PreparedStatement pstmt = null; 
	    ResultSet rs= null;
	    dbAccess dba=null;
	  
	    try {
	      dba = new dbAccess ();
	          pstmt = dba.getPreparedStatement(GET_DSETID_BYNAME);
	          pstmt.setString(1,dsName);
	          rs = pstmt.executeQuery(); 
	          if (rs != null && rs.next()) {
	              dset = new BtoDsetDao();
	              dset.dsId = rs.getInt("DATASETID");
	              dset.name = rs.getString("NAME");
	              dset.descr = rs.getString("DESCR");
	              dset.enabled=rs.getBoolean("ENABLED");
	              
	            }
	    } catch (SQLException e) {
	      log.error("Unble to execute query!", e);
	    } finally {
	      if (dba != null)
	        dba.close(rs, pstmt);
	    }
	    return dset;
	  }

  /**
     * TODO: Note that method must not be static as DAO will be autowired
     * as Spring component!
   * 
   * @param dsId
   * @param loginId
   * @param loginType
   * @return
   * @throws Exception 
   */
  public Map<String, List<BtoDsetDao> > getDatasetsByCust(int dsId,String loginId,String loginType)
			throws Exception {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		dbAccess dba = null;
		List<BtoDsetDao> dsList = new ArrayList<BtoDsetDao>();
		Map<String, List<BtoDsetDao> > custMap = new HashMap<String, List<BtoDsetDao> >();
		String custName = null;
		BtoDsetDao dset = null;
		try {
			dba = new dbAccess();
			
			if(loginType.equalsIgnoreCase("SAML"))
				pstmt = dba.getPreparedStatement(GET_DSETS_SAMLID);
			else
				pstmt = dba.getPreparedStatement(GET_DSETS_LOGINID);
			pstmt.setInt(1,dsId);
			pstmt.setString(2,"ONBOARDING_DONE");
			pstmt.setString(3,loginId);
			rs = pstmt.executeQuery();
			while (rs != null && rs.next()) {
				dset = new BtoDsetDao();
		        dset.dsId = rs.getInt("DATASETID");
		        dset.name = rs.getString("NAME");
		        dset.enabled=rs.getBoolean("ENABLED");
		        dset.onBoardingDone = rs.getString("VALUE1");
		        custName= rs.getString("CUSTNAME");
		        dsList.add(dset);
			}
			custMap.put(custName, dsList);
		} catch (SQLException e) {
			log.error("Unble to execute query!", e);
		} finally {
			if (dba != null)
				dba.close(rs, pstmt);
		}
		return custMap;
	}
  public static boolean isDsetNameExists(String dsetName)
	      throws Exception {
	    BtoDsetDao dset = null;
	    PreparedStatement pstmt = null; 
	    ResultSet rs= null;
	    dbAccess dba=null;
	  
	    try {
	      dba = new dbAccess ();
	          pstmt = dba.getPreparedStatement(IS_DSETNAME_EXISTS);
	          pstmt.setString(1,dsetName);
	          rs = pstmt.executeQuery(); 
	      if (rs != null && rs.next()) {	       
	    	   return true;	        
	      }
	    } catch (SQLException e) {
	      log.error("Unble to execute query!", e);
	    } finally {
	      if (dba != null)
	        dba.close(rs, pstmt);
	    }
	    return false;
	  }
  
  
  public static boolean isDsetNameExistsForOtherCust(String dsetName, int dsId)
	      throws Exception {
	    BtoDsetDao dset = null;
	    PreparedStatement pstmt = null; 
	    ResultSet rs= null;
	    dbAccess dba=null;
	  
	    try {
	      dba = new dbAccess ();
	          pstmt = dba.getPreparedStatement(IS_DSETNAME_EXISTS_FOR_OTHER_CUSTOMERS);
	          pstmt.setString(1,dsetName);
	          pstmt.setInt(2,dsId);
	          rs = pstmt.executeQuery(); 
	      if (rs != null && rs.next()) {	       
	    	   return true;	        
	      }
	    } catch (SQLException e) {
	      log.error("Unble to execute query!", e);
	    } finally {
	      if (dba != null)
	        dba.close(rs, pstmt);
	    }
	    return false;
	  }
  
  public int insert() {
		dbAccess dba = null;		
		PreparedStatement ps = null;
		int dsId = 0;
		  try{
	    	dba = new dbAccess();
	    	ps = dba.getPreparedStatement(INSERT_SQL);	  			
			ps.setString(1,name );
			ps.setString(2, descr);
			ps.setBoolean(3, enabled);			
			java.util.Date createdDate = new Date();
			ps.setTimestamp(4, new java.sql.Timestamp(createdDate.getTime()));
			ps.setInt(5, custId);
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();  
			 rs.next();  
			 dsId = rs.getInt(1);
		} catch (SQLException e) {
			log.error("Failed while inserting BTODSET record : " + e);
			return dsId;
		} finally {
			if (dba != null) {
				dba.close(ps);
			}
		}
		return dsId;
	}
  

	public static int updateCMCDsetName(BtoDsetDao btoDsetDao)
			throws SQLException {
		int result = 0;

		dbAccess dba = null;
		PreparedStatement ps = null;

		StringBuffer sql = new StringBuffer();

		try {
			dba = new dbAccess();
			ps= dba.getPreparedStatement(UPDATE_DSETNAME_SQL);
			ps.setInt(1, btoDsetDao.getDsId());
			ps.setTimestamp(2, btoDsetDao.getUpdatedAt());		
			
			
			log.debug("Executing update statement: " + sql.toString());
			log.debug("With parameters: ");
			log.debug(" [DATASETID = " + btoDsetDao.getDsId() + "]");		
			result = ps.executeUpdate();
			log.debug(" [" + result + " row(s) have been updated. ]");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.fillInStackTrace());
		} finally {
			if (ps != null) 
				ps.close();
			dba.close();
		}
		return result;
	}
  
	public int updateCMCDsetName()
			throws SQLException {
		
		int result = 0;
		dbAccess dba = null;
		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();

		try {
			dba = new dbAccess();
			ps= dba.getPreparedStatement(UPDATE_DSETNAME_SQL);
			ps.setString(1, name);
			ps.setTimestamp(2, updatedAt);
			ps.setInt(3,dsId);
			
			
			log.debug("Executing update statement: " + sql.toString());
			log.debug("With parameters: ");
			log.debug(" [DATASETID = " + dsId + "]");		
			result = ps.executeUpdate();
			log.debug(" [" + result + " row(s) have been updated. ]");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.fillInStackTrace());
		} finally {
			if (ps != null) 
				ps.close();
			dba.close();
		}
		return result;
	}
	
	public int updateDsetDescr()
			throws SQLException {
		
		int result = 0;
		dbAccess dba = null;
		PreparedStatement ps = null;
		StringBuffer sql = new StringBuffer();

		try {
			dba = new dbAccess();
			ps= dba.getPreparedStatement(UPDATE_DSETDESCR_SQL);
			ps.setString(1, descr);
			ps.setTimestamp(2, updatedAt);
			ps.setInt(3,dsId);
			log.debug("Executing update statement: " + sql.toString());
			log.debug("With parameters: ");
			log.debug(" [DATASETID = " + dsId + "]");		
			result = ps.executeUpdate();
			log.debug(" [" + result + " row(s) have been updated. ]");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new SQLException(e.fillInStackTrace());
		} finally {
			if (ps != null) 
				ps.close();
			dba.close();
		}
		return result;
	}
  
  
  
	
	 public static int updateDsetDescr(int dsId, String descr) throws SQLException {
			int result = 0;
			dbAccess dba = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer();

			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(UPDATE_DSETDESC_SQL);
				ps.setString(1, descr);
				java.util.Date updatedDate = new Date();
				ps.setTimestamp(2, new java.sql.Timestamp(updatedDate.getTime()));	
				ps.setInt(3,dsId);
				
				
				log.debug("Executing update statement: " + sql.toString());
				log.debug("With parameters: ");
				log.debug(" [DATASETID = " + dsId + "]");		
				result = ps.executeUpdate();
				log.debug(" [" + result + " row(s) have been updated. ]");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.fillInStackTrace());
			} finally {
				if (ps != null) 
					ps.close();
				dba.close();
			}
			return result;
		}
	 
	 
	 public static int updateDsetUpdateDate(int dsId, Timestamp ts) throws SQLException {
			int result = 0;
			dbAccess dba = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer();

			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(INTERNAL_DEFAULT_INFO_UPDATEDT);
				ps.setTimestamp(1, ts);
				ps.setInt(2,dsId);
				
				
				log.debug("Executing update statement: " + sql.toString());
				log.debug("With parameters: ");
				log.debug(" [DATASETID = " + dsId + "]");		
				result = ps.executeUpdate();
				log.debug(" [" + result + " row(s) have been updated. ]");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.fillInStackTrace());
			} finally {
				if (ps != null) 
					ps.close();
				dba.close();
			}
			return result;
		}
	 
	 
	 public static int updateDsetEnabledStatus(int dsId, int enabled, java.util.Date updateDate) throws SQLException {
			int result = 0;
			dbAccess dba = null;
			PreparedStatement ps = null;
			StringBuffer sql = new StringBuffer();
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(UPDATE_DSET_ENABLE_SQL);
				ps.setInt(1, enabled);
				ps.setTimestamp(2, new java.sql.Timestamp(updateDate.getTime()));
				ps.setInt(3, dsId);				
				log.debug("Executing update statement: " + sql.toString());
				log.debug("With parameters: ");
				log.debug(" [DATASETID = " + dsId + "]");		
				result = ps.executeUpdate();
				log.debug(" [" + result + " row(s) have been updated. ]");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new SQLException(e.fillInStackTrace());
			} finally {
				if (ps != null) 
					ps.close();
				dba.close();
			}
			return result;
		}
	 
	 public static String getDatasetName(int dsId){
		 
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			String sql = "Select NAME from obx.BTODSET where Datasetid = ?";
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(sql);
				ps.setInt(1, dsId);
				log.debug("Executing update statement: " + sql);
				result = ps.executeQuery();
				if(result!=null && result.next())
					return result.getString("NAME");
			} catch (SQLException e) {
				log.info(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				dba.close();
			}		 
		 return null;
	 }
	 
	 public static int getCustId(int dsId){
		 
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(SELECT_CUSTID_SQL);
				ps.setInt(1, dsId);
				log.debug("Executing update statement: " + SELECT_CUSTID_SQL);
				result = ps.executeQuery();
				if(result!=null && result.next())
					return result.getInt("CUSTID");
			} catch (SQLException e) {
				log.info("Error occured while getting custid: " + e.getMessage());
				log.debug(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				dba.close();
			}		 
		 return 0;
	 }
	 
	 public static List<Integer> getDsIdByCustId(int custId){
		 
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			List<Integer> datasetList = new ArrayList<Integer>();
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(SELECT_DATASETID_BYCUST);
				ps.setInt(1, custId);
				log.debug("Executing update statement: " + SELECT_DATASETID_BYCUST);
				result = ps.executeQuery();
				while(result!=null && result.next())
					 datasetList.add(result.getInt("DATASETID"));
			} catch (SQLException e) {
				log.info("Error occured while getting datasetid: " + e.getMessage());
				log.debug(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.info(e.getMessage());
						log.debug(e.fillInStackTrace());
					}
				dba.close();
			}		 
		 return datasetList;
	 }
	 
	 public static List<String> getDsNameByCustId(int custId){
		 
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			List<String> datasetList = new ArrayList<String>();
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(SELECT_NAME_BYCUST);
				ps.setInt(1, custId);
				log.debug("Executing update statement: " + SELECT_NAME_BYCUST);
				result = ps.executeQuery();
				while(result!=null && result.next())
					 datasetList.add(result.getString("NAME"));
			} catch (SQLException e) {
				log.info("Error occured while getting name: " + e.getMessage());
				log.debug(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.info(e.getMessage());
						log.debug(e.fillInStackTrace());
					}
				dba.close();
			}		 
		 return datasetList;
	 }

	public String getOnBoardingDone() {
		return onBoardingDone;
	}

	public void setOnBoardingDone(String onBoardingDone) {
		this.onBoardingDone = onBoardingDone;
	}
	 
	 public static int getCustIdByDsetId(int dsId){
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(SELECT_CUSTID_BY_DSETID);
				ps.setInt(1, dsId);
				log.debug("Executing update statement: " + SELECT_CUSTID_BY_DSETID);
				result = ps.executeQuery();
				if(result!=null && result.next())
					 return result.getInt("CUSTID");
			} catch (SQLException e) {
				log.info("Error occured while getting custid: " + e.getMessage());
				log.debug(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.info(e.getMessage());
						log.debug(e.fillInStackTrace());
					}
				dba.close();
			}		 
		 return 0;
	 }
	 
	 public static String getCustNameByDsetId(int dsId){
		    ResultSet result = null;
			dbAccess dba = null;
			PreparedStatement ps = null;
			try {
				dba = new dbAccess();
				ps= dba.getPreparedStatement(SELECT_CUST_NAME_BY_DSETID);
				ps.setInt(1, dsId);
				log.debug("Executing select statement: " + SELECT_CUST_NAME_BY_DSETID);
				result = ps.executeQuery();
				if(result!=null && result.next())
					 return result.getString("CUSTNAME");
			} catch (SQLException e) {
				log.info("Error occured while getting custid: " + e.getMessage());
				log.debug(e);
			} finally {
				if (ps != null)
					try {
						ps.close();
					} catch (SQLException e) {
						log.info(e.getMessage());
						log.debug(e.fillInStackTrace());
					}
				dba.close();
			}		 
		 return null;
	 }
	 
}
