/*
 * $Id: BtoConfigDao.java,v 1.54 2018/07/27 19:06:19 mchallana Exp $
 ******************************************************************************
 * Copyright 1996, 2004 BY Business Software, Inc., 
 * 155 Technology Parkway, Suite 100,  Norcross, GA 30092, U.S.A.
 * htt://www.bsi.com
 * All rights reserved.
 *
 * This source code is confidential, unpublished, and proprietary to Business
 * Software, Inc.  Unauthorized Use, Transfer, Disclosure, or Copying is
 * strictly prohibited, and may result in civil liability, and criminal
 * prosecution. This notice must be reproduced on all authorized copies.
 ******************************************************************************
 */
package com.bsi.sec.tpfdao;

import java.io.Serializable;
import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.bsi.sec.exception.dbException;
import com.bsi.sec.dto.CompanyConfigInfo;
import com.bsi.sec.dto.ConfigurationAudit;
import com.bsi.sec.util.AFxDBBase;
import com.bsi.sec.util.AppConsts;
import com.bsi.sec.util.CryptUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sita
 *
 */
@Component
public class BtoConfigDao implements Serializable {

    public static final String OBFSE_STR = "**********";
    public static final int FLD_LEN_ITEMNAME = 50;

    private static final long serialVersionUID = -5332399041534628645L;

    private static final Logger log = LogManager.getLogger(BtoConfigDao.class);
    public static final String SCHEDULED_IMPORT_AVAILABLE = "y";

    @Autowired
    private dbAccess dba;

    private int dsId;

    private int compId;
    private String custid;
    private String fein;
    private String itemname;
    private String value1;
    private String value2 = " ";
    private int category = 0;
    private int subcat = 0;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private static List<String> v1v2ItemList = Arrays.asList(AppConsts.V1V2_ITEMS);
    private static List<String> splValueItemList = Arrays.asList(AppConsts.SPL_VALUE_ITEMS);
    private static List<String> decryptValueItemList = Arrays.asList(AppConsts.DECRYPT_ITEMS);
    private static List<String> obfuscateItemList = Arrays.asList(AppConsts.OBFUSCATE_ITEMS);
    private static List<String> blockConversionList = Arrays.asList(AppConsts.EXP_LIST);
    private static final String TBL_BTOCONFIG = "OBX.BTOCONFIG";

    // private static final String scheduled_import_global_config_values = "SELECT
    // ITEMNAME,VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = 1 AND
    // COMPID = 1 AND CATGRY=2";
    private static final String scheduled_import_global_config_values = "SELECT ITEMNAME,VALUE1 FROM OBX.BTOCONFIG cfg , obx.btocomp cmp, OBX.BTODSET dst where dst.name = '_BSI' "
            + " and dst.datasetid = cfg.datasetid and dst.datasetid=cmp.datasetid and cmp.compid=cfg.compid and cfg.CATGRY=2";

    private static final String cust_config_values = "SELECT ITEMNAME,VALUE1 FROM OBX.BTOCONFIG "
            + "WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME IN ('APPROVE_EMPLOYEE_TAXES','BYPASS_EXPORT_CONFIG','BYPASS_TAX_APPROVAL_ON_INITIAL_IMPORT','CONVERSION_DONE','SEC_MASTER','RETURN_FEDERAL_FOR_SAME_LIVEWORK_TERRITORY') ";

    private static final String INSERT_SQL = "INSERT INTO OBX.BTOCONFIG (DATASETID, COMPID, ITEMNAME, VALUE1, VALUE2, "
            + "CATGRY, SUBCAT, CREATEDAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE_CMC_INFO_SQL = "UPDATE OBX.BTOCONFIG SET VALUE1 =?, UPDATEDAT =? WHERE ITEMNAME =? AND DATASETID =? AND COMPID = ? ";

    private static final String INSERT_NEW_COMP_FROM_DEF = "INSERT INTO OBX.BTOCONFIG (DATASETID, COMPID, ITEMNAME, VALUE1, VALUE2, "
            + "CATGRY, SUBCAT, CREATEDAT,UPDATEDAT) SELECT DATASETID, ? ,ITEMNAME, VALUE1, VALUE2,CATGRY, SUBCAT, ?,? FROM OBX.BTOCONFIG WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME <> 'CONVERSION_DONE' AND SUBCAT NOT IN (9)";

    private static final String cust_cmcpg_config_items = "SELECT ITEMNAME,VALUE1 FROM OBX.BTOCONFIG "
            + "WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME IN ('ERPSYSTEM_DEST','ERPSYSTEM','DATA_TRANSFER_TYPE','EMP_IMPORT_LOG','ONBOARDING_DONE','BACKUP_RESTORE_CONFIG','COMPANY_TYPE') ";

    private static final String General_config_items = "SELECT ITEMNAME,VALUE1 FROM OBX.BTOCONFIG WHERE DATASETID = ? AND COMPID = ?  ";
    private static final String General_config_itemsValue2 = "SELECT ITEMNAME,VALUE2 FROM OBX.BTOCONFIG WHERE DATASETID = ? AND COMPID = ?  ";
    private static final String config_audit_comp_cnt = "SELECT COUNT(DISTINCT T2.COMPID) FROM  OBX.BTOCONFIG T1, OBX.BTOCONFIG T2 "
            + " WHERE T1.DATASETID = ? AND T1.DATASETID = T2.DATASETID "
            + "AND T1.COMPID = (SELECT C1.COMPID FROM OBX.BTOCOMP C1 WHERE C1.DATASETID = T1.DATASETID AND C1.FEIN = '_DEFAULT')"
            + "AND T2.COMPID != (SELECT C1.COMPID FROM OBX.BTOCOMP C1 WHERE C1.DATASETID = T1.DATASETID AND C1.FEIN  = '_DEFAULT')"
            + "AND T1.ITEMNAME = T2.ITEMNAME AND T1.SUBCAT <> 9 AND T1.SUBCAT <> 8 AND ((T1.VALUE1 != T2.VALUE1) OR (T1.VALUE2 != T2.VALUE2))";

    private static final String GET_CONFIG_AUDIT = "SELECT T1.DATASETID,T2.COMPID,	T1.UPDATEDAT,C.FEIN,C.LEGALNAME,T1.ITEMNAME,T1.VALUE1 as DSVALUE1,T2.VALUE1 as COMPVALUE1,"
            + "T1.VALUE2 as DSVALUE2,T2.VALUE2 as COMPVALUE2 FROM OBX.BTOCONFIG T1,OBX.BTOCONFIG T2,OBX.BTOCOMP C "
            + " WHERE T1.DATASETID = T2.DATASETID AND C.DATASETID = T2.DATASETID AND C.COMPID = T2.COMPID "
            + " AND T1.COMPID = (SELECT C1.COMPID FROM OBX.BTOCOMP C1 WHERE C1.DATASETID = T1.DATASETID AND C1.FEIN = '_DEFAULT') "
            + " AND T2.COMPID != (SELECT C1.COMPID 		FROM OBX.BTOCOMP C1 WHERE C1.DATASETID = T1.DATASETID AND C1.FEIN  = '_DEFAULT') "
            + " AND T1.ITEMNAME = T2.ITEMNAME AND T1.SUBCAT <> 9 AND T1.SUBCAT <> 8 AND ((T1.VALUE1 != T2.VALUE1) OR (T1.VALUE2 != T2.VALUE2) ) ";

    private static final String get_comp_config_items = "SELECT CONFIG.DATASETID, DSET.NAME, DSET.DESCR, CONFIG.COMPID, COMP.SAMLCID, LOGIN.SAMLUID, CONFIG.ITEMNAME, CONFIG.VALUE1 "
            + "FROM OBX.BTOCONFIG CONFIG, OBX.BTOLOGIN LOGIN, OBX.BTOCOMP COMP, OBX.BTODSET DSET "
            + "WHERE CONFIG.DATASETID = LOGIN.DATASETID AND CONFIG.COMPID = LOGIN.COMPID AND COMP.DATASETID = LOGIN.DATASETID AND COMP.COMPID = LOGIN.COMPID AND COMP.DATASETID = DSET.DATASETID AND "
            + "CONFIG.DATASETID = ? AND CONFIG.COMPID = ? AND ITEMNAME IN ('ERPSYSTEM', 'ERPSYSTEM_DEST','DATA_TRANSFER_TYPE','USERNAME','PASSWORD') ";

    private static final String INSERT_SQL_WZ = "INSERT INTO OBX.BTOCONFIG (DATASETID, COMPID, ITEMNAME, VALUE1, VALUE2, "
            + "CATGRY, SUBCAT, CREATEDAT, UPDATEDAT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_SCHEDULED_WEEKDAYS = "SELECT ITEMNAME, VALUE1 FROM OBX.BTOCONFIG WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME IN ('SCHED_FREQ_MONDAY','SCHED_FREQ_TUESDAY','SCHED_FREQ_WEDNESDAY','SCHED_FREQ_THURSDAY','SCHED_FREQ_FRIDAY','SCHED_FREQ_SATURDAY','SCHED_FREQ_SUNDAY')";

    private static final String UPDATE_SCHEDULED_WEEKDAYS_TO_ZERO = "UPDATE OBX.BTOCONFIG SET VALUE1 = 0 WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME IN ('SCHED_FREQ_MONDAY','SCHED_FREQ_TUESDAY','SCHED_FREQ_WEDNESDAY','SCHED_FREQ_THURSDAY','SCHED_FREQ_FRIDAY','SCHED_FREQ_SATURDAY','SCHED_FREQ_SUNDAY')";

    // private static final String TJC_GLOBAL_CONFIG_VALUES = "SELECT
    // ITEMNAME,VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = 1 AND
    // COMPID = 1 AND CATGRY=3";
    private static final String TJC_GLOBAL_CONFIG_VALUES = "SELECT ITEMNAME, VALUE1 FROM OBX.BTOCONFIG cfg , obx.BTOCOMP cmp, OBX.BTODSET dst where dst.name = '_BSI' and dst.datasetid = cfg.datasetid and dst.datasetid=cmp.datasetid "
            + "and cmp.compid=cfg.compid and cfg.CATGRY=3";

    private static final String DB_PRODUCT_VER = "SELECT VALUE1 FROM OBX.BTOCONFIG CFG join OBX.BTOCOMP C ON C.FEIN = '_DEFAULT' "
            + " JOIN OBX.BTODSET D ON  D.NAME = '_BSI'"
            + " WHERE CFG.ITEMNAME='DB_PRODUCT_VERSION' and CFG.DATASETID=C.DATASETID AND CFG.COMPID=C.COMPID AND C.DATASETID=D.DATASETID";

    private static final String SAML_OPTION = " SELECT VALUE1 FROM OBX.BTOCONFIG WHERE ITEMNAME = 'SAML_OPTION' AND DATASETID = (SELECT DATASETID FROM OBX.BTOCOMP WHERE SAMLCID=? AND FEIN ='_DEFAULT') ";

    private static final String CUSTOMER_PII_CONFIG = "select COMP.DATASETID, COMP.COMPID, CONFIG.ITEMNAME, CONFIG.VALUE1 from obx.btocomp COMP, obx.btodset dset, OBX.btoconfig config"
            + " where custid = (select custid from obx.btodset where datasetid=?)and comp.datasetid=dset.datasetid and comp.datasetid=config.datasetid"
            + " and comp.compid = config.compid and config.itemname='" + AppConsts.ITEM_SECURE_EMPLOYEE_PII + "'";

    /**
     * Default no-arg constructor
     */
    public BtoConfigDao() {
    }

    /**
     * @param custid
     * @param fein
     * @return
     * @throws SQLException
     */
    public int getCount(int dsId, int compId) throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(*) CNTR FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND COMPID = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());

            if (pstmt != null) {
                pstmt.setInt(1, dsId);
                pstmt.setInt(2, compId);

                log.debug("Executing query: " + sql.toString());
                log.debug("With parameters: ");
                log.debug(" [DATASETID = " + dsId + "]");
                log.debug(" [COMPID = " + compId + "]");

                resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    result = resultSet.getInt("CNTR");
                    log.debug(" [Query result is " + result + "]");
                }
            }
        } catch (SQLException e) {
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }

        return result;
    }

    public static final List<Boolean> getCustomerPii(int dsId) {
        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        List<Boolean> piiFlags = new ArrayList<Boolean>();
        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(CUSTOMER_PII_CONFIG);
            pstmt.setInt(1, dsId);
            resultSet = pstmt.executeQuery();
            String pii;
            while (resultSet != null && resultSet.next()) {
                pii = resultSet.getString("VALUE1");
                if (StringUtils.isNotBlank(pii)) {
                    if ("y".equalsIgnoreCase(pii.trim())) {
                        piiFlags.add(true);
                    } else {
                        piiFlags.add(false);
                    }
                }
            }
        } catch (SQLException ex) {
            log.error(ex.getMessage());
            log.debug("{}", ex.fillInStackTrace());
        }
        return piiFlags;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @return
     * @throws SQLException
     */
    public int getCount(int dsId, int compId, String itemname) throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT COUNT(*) CNTR FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND COMPID = ? ");
        sql.append(" 	AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: ");
            log.debug(" [DATASETID = " + dsId + "]");
            log.debug(" [COMPID = " + compId + "]");
            log.debug(" [ITEMNAME = " + itemname + "]");

            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getInt("CNTR");
                log.debug(" [Query result is " + result + "]");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }

        return result;
    }

    /**
     * 
     * TODO: Note that method must not be static as DAO will be autowired
     * as Spring component!
     * 
     * @param custid
     * @param fein
     * @param itemname
     * @return
     * @throws SQLException
     */
    public String getValue(int dsId, int compId, String itemname) throws SQLException {
        String result = null;

        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: [DATASETID = " + dsId + "] [COMPID = " + compId + "] [ITEMNAME = " + itemname
                    + "]");

            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString("VALUE1").trim();
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemname + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }
        return result;
    }

    public static String getValue2(int dsId, int compId, String itemname) throws SQLException {
        String result = null;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT VALUE2 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: [DATASETID = " + dsId + "] [COMPID = " + compId + "] [ITEMNAME = " + itemname
                    + "]");

            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString("VALUE2");
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemname + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param itemname
     * @return
     * @throws SQLException
     */
    public static String getValue(int datasetId, String itemname) throws SQLException {
        String result = null;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        String sql = "SELECT VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND ITEMNAME = ?";

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql);
            pstmt.setInt(1, datasetId);
            pstmt.setString(2, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: [CUSTID = " + datasetId + "] [ITEMNAME = " + itemname + "]");

            resultSet = pstmt.executeQuery();

            if (resultSet != null && resultSet.next()) {
                result = resultSet.getString("VALUE1");
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemname + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }

            // TODO dba.close();
            dba.close(pstmt, true);
        }
        return result;
    }

    /**
     * @param itemname
     * @return
     * @throws SQLException
     */
    public static String getValue(String itemname) throws SQLException {
        String result = null;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        String sql = "SELECT VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE ITEMNAME = ?";

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql);
            pstmt.setString(1, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: [ITEMNAME = " + itemname + "]");

            resultSet = pstmt.executeQuery();

            if (resultSet != null && resultSet.next()) {
                result = resultSet.getString("VALUE1");
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemname + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @return
     * @throws SQLException
     */
    public static String getDecryptedValue(int dsId, int compId, String itemname) throws SQLException {
        BtoConfigDao dao = new BtoConfigDao();
        String result = dao.getValue(dsId, compId, itemname);
        if (StringUtils.isNotBlank(result)) {
            return CryptUtils.aesDecrypt(result, CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
        }
        return result;
    }

    public static String getDecryptedValue(int dsId, String itemname) throws SQLException {
        String result = getValue(dsId, itemname);
        if (StringUtils.isNotBlank(result)) {
            return CryptUtils.aesDecrypt(result, CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @return
     * @throws SQLException
     */
    public static String getImportExportDir() throws SQLException {
        String result = null;
        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE ITEMNAME = '"
                + AppConsts.ITEM_IMPORT_EXPORT_ROOT_DIR + "'");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            log.debug("Executing query: " + sql.toString());

            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString("VALUE1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close(true);
        }

        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value
     * @param desc
     * @return
     * @throws SQLException
     */
    public int insertOrUpdate(int dsId, int compId, String itemname, String value1) throws SQLException {
        if (getCount(dsId, compId, itemname) > 0) {
            return update(dsId, compId, itemname, value1);
        }
        return insert(dsId, compId, itemname, value1);
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value1
     * @param value2
     * @return
     * @throws SQLException
     */
    public int insertOrUpdate(int dsId, int compId, String itemname, String value1, String value2, Date updateDate)
            throws SQLException {
        if (getCount(dsId, compId, itemname) > 0) {
            return update(dsId, compId, itemname, value1, value2, updateDate);
        }
        return insert(dsId, compId, itemname, value1, value2, updateDate);
    }

    public int insertOrUpdate(int dsId, int compId, String itemname, String value1, boolean updateDate)
            throws SQLException {
        if (getCount(dsId, compId, itemname) > 0) {
            return update(dsId, compId, itemname, value1, updateDate);
        }
        return insert(dsId, compId, itemname, value1, "", new Date());
    }

    public int insertOrUpdate2(int dsId, int compId, String itemname, String value2, Date updateDate)
            throws SQLException {
        if (getCount(dsId, compId, itemname) > 0) {
            return updateValue2(dsId, compId, itemname, value2, updateDate);
        }
        return insert(dsId, compId, itemname, "", value2, new Date());
    }

    public int insertOrUpdate(int dsId, int compId, String itemname, String value1, Date updateDate)
            throws SQLException {
        if (getCount(dsId, compId, itemname) > 0) {
            return update(dsId, compId, itemname, value1);
        }
        return insert(dsId, compId, itemname, value1);
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value1
     * @return
     * @throws SQLException
     */
    /*
	 * public int insertOrUpdateEncryptedValue(String custid, String fein, String
	 * itemname, String value1) throws SQLException { if (getCount(custid, fein,
	 * itemname) > 0) { return update(custid, fein, itemname,
	 * CryptUtils.aesEncrypt(value1, CryptUtils.TRANSFORMATION_AES_CBC_PKCS5)); }
	 * return insert(custid, fein, itemname, CryptUtils.aesEncrypt(value1,
	 * CryptUtils.TRANSFORMATION_AES_CBC_PKCS5)); }
     */
    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value
     * @param desc
     * @return
     * @throws SQLException
     */
    public static int update(int dsId, int compId, String itemname, String value) throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE OBX.BTOCONFIG SET VALUE1 = ? ");
        sql.append(" WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? AND VALUE1 <> ?");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setString(1, value);
            pstmt.setInt(2, dsId);
            pstmt.setInt(3, compId);
            pstmt.setString(4, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));
            pstmt.setString(5, value);

            log.debug("Executing update statement: " + sql.toString());
            log.debug("With parameters: ");
            log.debug(" [DATASETID = " + dsId + "]");
            log.debug(" [COMPID = " + compId + "]");
            log.debug(" [ITEMNAME = " + itemname + "]");

            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been updated. ]");

            adjustTimeZone(itemname, value);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    public static int update(int dsId, int compId, String itemname, String value1, String value2, Date updateDate)
            throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE OBX.BTOCONFIG SET VALUE1 = ?, VALUE2 = ?, UPDATEDAT = ? ");
        sql.append(" WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setString(1, value1);
            pstmt.setString(2, value2);
            pstmt.setObject(3, new java.sql.Timestamp(updateDate.getTime()));
            pstmt.setInt(4, dsId);
            pstmt.setInt(5, compId);
            pstmt.setString(6, StringUtils.rightPad(itemname.trim(), FLD_LEN_ITEMNAME));

            log.debug("Executing update statement: " + sql.toString());

            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been updated. ]");

            adjustTimeZone(itemname, value1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    public static int update(int dsId, int compId, String itemname, String value1, boolean updateDate)
            throws SQLException {
        int result = 0;
        int incr = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        Timestamp timeStamp = new Timestamp(new Date().getTime());

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE OBX.BTOCONFIG SET VALUE1 = ? ");
        if (updateDate == true) {
            sql.append(", UPDATEDAT = ?");
        }
        sql.append(" WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setString(++incr, value1);
            if (updateDate == true) {
                pstmt.setObject(++incr, timeStamp);
            }
            pstmt.setInt(++incr, dsId);
            pstmt.setInt(++incr, compId);
            pstmt.setString(++incr, StringUtils.rightPad(itemname.trim(), FLD_LEN_ITEMNAME));

            log.debug("Executing update statement: " + sql.toString());
            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been updated. ]");

            adjustTimeZone(itemname, value1);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    public static int updateValue1(int dsId, int compId, String itemname, String value, java.util.Date updateDate)
            throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE OBX.BTOCONFIG SET VALUE1 = ?, UPDATEDAT = ? ");
        sql.append(" WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setString(1, value);
            pstmt.setObject(2, new java.sql.Timestamp(updateDate.getTime()));
            pstmt.setInt(3, dsId);
            pstmt.setInt(4, compId);
            pstmt.setString(5, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing update statement: " + sql.toString());
            log.debug("With parameters: ");
            log.debug(" [DATASETID = " + dsId + "]");
            log.debug(" [COMPID = " + compId + "]");
            log.debug(" [ITEMNAME = " + itemname + "]");
            log.debug(" [VALUE = " + value + "]");

            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been updated. ]");

            adjustTimeZone(itemname, value);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    public static int updateValue2(int dsId, int compId, String itemname, String value, java.util.Date updateDate)
            throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();

        sql.append(" UPDATE OBX.BTOCONFIG SET VALUE2 = ?, UPDATEDAT = ? ");
        sql.append(" WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ? ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setString(1, value);
            pstmt.setObject(2, new java.sql.Timestamp(updateDate.getTime()));
            pstmt.setInt(3, dsId);
            pstmt.setInt(4, compId);
            pstmt.setString(5, StringUtils.rightPad(itemname, FLD_LEN_ITEMNAME));

            log.debug("Executing update statement: " + sql.toString());

            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been updated. ]");

            adjustTimeZone(itemname, value);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param filingYear
     * @param consentDate
     * @return
     * @throws SQLException
     */
    public int insertConsentDate(int dsId, int compId, String filingYear, String consentDate) throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        String sqlGetSubcat = "SELECT (" + AFxDBBase.ISNULL + "( MAX(SUBCAT), -1)+1) SUBCAT FROM OBX.BTOCONFIG"
                + " WHERE DATASETID = ? AND COMPID  = ? AND ITEMNAME = ? ";

        StringBuffer sql = new StringBuffer();

        try {
            dba = new dbAccess();

            pstmt = dba.getPreparedStatement(sqlGetSubcat);
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, AppConsts.ITEM_W2_FILING_YEAR);
            log.debug("Executing get statement: " + sql.toString());
            log.debug("With parameters: ");
            log.debug(" [DATASETID = " + dsId + "]");
            log.debug(" [COMPID = " + compId + "]");
            log.debug(" [ITEMNAME = " + itemname + "]");

            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                subcat = resultSet.getInt("SUBCAT");
            }
            log.debug(" subcat [" + subcat + "]");

            /*
			 * insert(custid, fein, AppConsts.ITEM_W2_FILING_YEAR, filingYear);
			 * insert(custid, fein, AppConsts.ITEM_W2_CONSENT_DATE, consentDate);
             */
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value1
     * @return
     * @throws SQLException
     */
    public int insert(int dsId, int compId, String itemname, String value1) throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO OBX.BTOCONFIG VALUES (?, ?, ?, ?, ?, ?, ?,?,?) ");

        try {
            java.util.Date createDt = new Date();
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, itemname);
            pstmt.setString(4, value1);
            pstmt.setString(5, value2);
            pstmt.setInt(6, category);
            if (itemname.equals(AppConsts.ITEM_SEC_MASTER) || itemname.equals(AppConsts.ITEM_BACKUP_RESTORE_FREQUENCY)) {
                pstmt.setInt(7, 9);
            } else {
                pstmt.setInt(7, subcat);
            }
            pstmt.setObject(8, new java.sql.Timestamp(createDt.getTime()));
            pstmt.setObject(9, null);

            log.debug("Executing insert statement: " + sql.toString());
            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been inserted. ]");

            adjustTimeZone(itemname, value1);
        } catch (SQLException e) {
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value1
     * @param value2
     * @return
     * @throws SQLException
     */
    public int insert(int dsId, int compId, String itemname, String value1, String value2, Date updateDate)
            throws SQLException {
        int result = 0;

        dbAccess dba = null;
        PreparedStatement pstmt = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" INSERT INTO OBX.BTOCONFIG VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ");

        try {
            java.util.Date createDt = new Date();

            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            pstmt.setString(3, itemname);
            pstmt.setString(4, value1);
            pstmt.setString(5, value2);
            pstmt.setInt(6, category);
            pstmt.setInt(7, subcat);
            pstmt.setObject(8, new java.sql.Timestamp(updateDate.getTime()));
            pstmt.setObject(9, new java.sql.Timestamp(updateDate.getTime()));

            log.debug("Executing insert statement: " + sql.toString());
            log.debug("With parameters: ");
            log.debug(" [DATASETID = " + dsId + "]");
            log.debug(" [COMPID = " + compId + "]");
            log.debug(" [ITEMNAME = " + itemname + "]");
            log.debug(" [VALUE = " + value1 + "]");
            log.debug(" [CREATEDAT = " + createDt.getTime() + "]");

            result = pstmt.executeUpdate();
            log.debug(" [" + result + " row(s) have been inserted. ]");

            adjustTimeZone(itemname, value1);
        } catch (SQLException e) {
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @return
     * @throws SQLException
     */
    public static String getImportStackingConfig() throws SQLException {
        String result = null;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE ITEMNAME = '"
                + AppConsts.ITEM_STACK_IMPORT_REQ + "'");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            log.debug("Executing query: " + sql.toString());

            resultSet = pstmt.executeQuery();

            if (resultSet.next()) {
                result = resultSet.getString("VALUE1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }

        return result;
    }

    /**
     * @param custid
     * @param fein
     * @param itemname
     * @param value1
     * @return
     * @throws SQLException
     */
    /*
	 * public int insertEncryptedValue(String custid, String fein, String itemname,
	 * String value1) throws SQLException { return insert(custid, fein, itemname,
	 * CryptUtils.aesEncrypt(value1, CryptUtils.TRANSFORMATION_AES_CBC_PKCS5)); }
     */
    /**
     * @return the custid
     */
    public String getCustid() {
        return custid;
    }

    /**
     * @param custid the custid to set
     */
    public void setCustid(String custid) {
        this.custid = custid;
    }

    /**
     * @return the fein
     */
    public String getFein() {
        return fein;
    }

    /**
     * @param fein the fein to set
     */
    public void setFein(String fein) {
        this.fein = fein;
    }

    /**
     * @return the itemname
     */
    public String getItemname() {
        return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    /**
     * @return the value1
     */
    public String getValue1() {
        return value1;
    }

    /**
     * @param value1 the value1 to set
     */
    public void setValue1(String value1) {
        this.value1 = value1;
    }

    /**
     * @return the value2
     */
    public String getValue2() {
        return value2;
    }

    /**
     * @param value2 the value2 to set
     */
    public void setValue2(String value2) {
        this.value2 = value2;
    }

    /**
     * @return the category
     */
    public int getCategory() {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * @return the subcat
     */
    public int getSubcat() {
        return subcat;
    }

    /**
     * @param subcat the subcat to set
     */
    public void setSubcat(int subcat) {
        this.subcat = subcat;
    }

    /**
     * Check if incoming change is a timezone change and save updated timezone
     * value.
     */
    private static void adjustTimeZone(String itemname, String value) {
        // Adjust Time Zone if applicable
        if (StringUtils.isNotBlank(itemname) && AppConsts.ITEM_TIME_ZONE_DISPLAY.equals(itemname.trim())) {
            int timezone = AppConsts.DFLT_TIME_ZONE;

            try {
                timezone = Integer.parseInt(value);
            } catch (NumberFormatException e) {
            }

            //TODO: Avoid propagating servlet context to DAO. Instead,
            //return value and store it in session in proper Web tier resource!
//			ServletActionContext.getRequest().getSession().setAttribute(AppConsts.TIME_ZONE_HOLDER, timezone);
        }
    }

    /**
     * getValuesForItemNamesByCompany
     *
     * @param custId
     * @param fein
     * @param itemnames
     * @return
     * @throws SQLException
     */
    public static Map<String, String> getValue1sForItemNamesByCompany(int datasetId, int compId, List<String> itemnames)
            throws SQLException {

        Map<String, String> mapItems = new HashMap<String, String>();
        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        StringBuffer sql = new StringBuffer();
        sql.append(" SELECT ITEMNAME, VALUE1 FROM OBX.BTOCONFIG with (nolock) WHERE DATASETID = ? AND COMPID = ? ");
        sql.append("AND ITEMNAME in(");

        int totItems = itemnames.size() - 1;
        for (int i = 0; i < itemnames.size(); i++) {
            if (i < (totItems)) {
                sql.append("?");
                sql.append(",");
            } else {
                sql.append("?");
            }
        }
        sql.append(" ) ");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, datasetId);
            pstmt.setInt(2, compId);
            int index = 3;
            for (String itemname : itemnames) {
                pstmt.setString(index++, itemname);
            }

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                mapItems.put(resultSet.getString("ITEMNAME").trim(), resultSet.getString("VALUE1"));
            }

            for (String itemname : itemnames) {
                if (!mapItems.containsKey(itemname)) {
                    mapItems.put(itemname, "NOT_FOUND");
                }
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemnames + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }
        return mapItems;
    }

    /**
     * @param custId
     * @param fein
     * @return
     */
    public static HashMap<String, String> getCustConfigValues(int dsId, int compId) {
        log.debug("Getting employer config values1 ");
        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, String> custConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(cust_config_values);
            ps.setInt(1, dsId);
            ps.setInt(2, compId);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                custConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE1").trim());

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting employer config values. Exception [" + e.fillInStackTrace()
                    + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return custConfigValues;
    }

    public static HashMap<String, String> getCMCPgConfigValues(int dsId, int compId) {

        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, String> cmcPgConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(cust_cmcpg_config_items);
            ps.setInt(1, dsId);
            ps.setInt(2, compId);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                cmcPgConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE1").trim());

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting cmcPg config values. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cmcPgConfigValues;
    }

    public static HashMap<String, String> getGeneralConfigValues(int dsId, int compId) {

        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, String> generalConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(General_config_items);
            ps.setInt(1, dsId);
            ps.setInt(2, compId);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                generalConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE1").trim());

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting cmcPg config values. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return generalConfigValues;
    }

    public static HashMap<String, String> getGeneralConfigValues2(int dsId, int compId) {

        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, String> generalConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(General_config_itemsValue2);
            ps.setInt(1, dsId);
            ps.setInt(2, compId);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                generalConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE2").trim());

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting cmcPg config values. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return generalConfigValues;
    }

    public List<BtoConfigDao> getDefConfigList(int dsId, int compId, int newCompId) throws SQLException {
        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<BtoConfigDao> configList = new ArrayList<BtoConfigDao>();
        StringBuffer sql = new StringBuffer();
        sql.append(
                " SELECT DATASETID,COMPID,ITEMNAME,VALUE1,VALUE2,CATGRY,SUBCAT FROM OBX.BTOCONFIG WHERE DATASETID = ? AND COMPID = ?");

        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());
            pstmt.setInt(1, dsId);
            pstmt.setInt(2, compId);
            log.debug("Executing query: " + sql.toString());
            log.debug("With parameters: [DATASETID = " + dsId + "] [COMPID = " + compId + "] [ITEMNAME = " + itemname
                    + "]");

            rs = pstmt.executeQuery();

            while (rs != null && rs.next()) {
                BtoConfigDao config = new BtoConfigDao();
                config.setDsId(rs.getInt("DATASETID"));
                config.setCompId(newCompId);
                config.setItemname(rs.getString("ITEMNAME"));

                if (!rs.getString("ITEMNAME").equalsIgnoreCase(AppConsts.ITEM_CONVERSION_DONE)) {
                    config.setValue1(rs.getString("VALUE1"));
                } else {
                    config.setValue1(AppConsts.NO);
                }
                config.setValue2(rs.getString("VALUE2"));
                config.setCategory(rs.getInt("CATGRY"));
                config.setSubcat(rs.getInt("SUBCAT"));

                // log.debug(" [Query result is " + result + "]");
                configList.add(config);
            }
        } catch (SQLException e) {
            log.error("Error occurred in getValue Item Name [" + itemname + "] " + e.fillInStackTrace());
            throw e;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            dba.close();
        }
        return configList;

    }

    public int[] insertConfig(List<BtoConfigDao> configList) throws SQLException {
        PreparedStatement ps = null;
        dbAccess dba = null;
        int[] ii = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(INSERT_SQL.toString());
            for (BtoConfigDao conf : configList) {
                ps.setInt(1, conf.getDsId());
                ps.setInt(2, conf.getCompId());
                ps.setString(3, conf.getItemname());
                ps.setString(4, conf.getValue1());
                ps.setString(5, conf.getValue2());
                ps.setInt(6, conf.getCategory());
                ps.setInt(7, conf.getSubcat());
                java.util.Date createdDate = new Date();
                ps.setTimestamp(8, new java.sql.Timestamp(createdDate.getTime()));
                ps.addBatch();
            }
            ii = ps.executeBatch();
            ps.clearBatch();
        } catch (BatchUpdateException e) {
            ii = e.getUpdateCounts();
            for (int i = 0; i < ii.length; i++) {
                if (ii[i] >= 0) {
                    System.out.println("Successfully executed");
                } else if (ii[i] == Statement.SUCCESS_NO_INFO) {
                } else if (ii[i] == Statement.EXECUTE_FAILED) {
                    System.out.println("Failed to execute UpdateBatch ");

                }
            }
        } catch (SQLException e) {
            System.out.println("Failed while updating UpdateSamlcid. Exception [" + e.fillInStackTrace() + "]");
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return ii;
    }

    public int[] insertConfigFromWizard(List<BtoConfigDao> configList, boolean backup) throws SQLException {
        PreparedStatement ps = null;
        dbAccess dba = null;
        int[] ii = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(INSERT_SQL_WZ.toString());
            for (BtoConfigDao conf : configList) {
                ps.setInt(1, conf.getDsId());
                ps.setInt(2, conf.getCompId());
                ps.setString(3, conf.getItemname());
                ps.setString(4, conf.getValue1());
                ps.setString(5, conf.getValue2());
                ps.setInt(6, conf.getCategory());
                ps.setInt(7, conf.getSubcat());

                java.util.Date createdDate = new Date();
                if (conf.getCreatedAt() == null) {
                    ps.setTimestamp(8, new java.sql.Timestamp(createdDate.getTime()));
                } else {
                    ps.setTimestamp(8, conf.getCreatedAt());
                }

                if (conf.getUpdatedAt() == null && !backup) {
                    ps.setTimestamp(9, new java.sql.Timestamp(createdDate.getTime()));
                } else {
                    ps.setTimestamp(9, conf.getUpdatedAt());
                }

                ps.addBatch();
            }
            ii = ps.executeBatch();
            ps.clearBatch();
        } catch (BatchUpdateException e) {
            log.info("Updating config from wizard");
            updateConfigFromWizard(configList);
            // throw e;
        } catch (SQLException e) {
            System.out.println("Failed while updating UpdateSamlcid. Exception [" + e.fillInStackTrace() + "]");
            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return ii;
    }

    public int[] updateConfigFromWizard(List<BtoConfigDao> configList) throws SQLException {
        PreparedStatement ps = null;
        dbAccess dba = null;
        int[] ii = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(UPDATE_CMC_INFO_SQL.toString());
            for (BtoConfigDao conf : configList) {
                ps.setString(1, conf.getValue1());
                ps.setTimestamp(2, conf.getUpdatedAt());
                ps.setString(3, conf.getItemname());
                ps.setInt(4, conf.getDsId());
                ps.setInt(5, conf.getCompId());
                ps.addBatch();
            }
            ii = ps.executeBatch();
            ps.clearBatch();
        } catch (BatchUpdateException e) {
            ii = e.getUpdateCounts();
            for (int i = 0; i < ii.length; i++) {
                if (ii[i] >= 0) {
                    System.out.println("Successfully executed");
                } else if (ii[i] == Statement.SUCCESS_NO_INFO) {
                } else if (ii[i] == Statement.EXECUTE_FAILED) {
                    System.out.println("Failed to execute UpdateBatch ");

                }
            }
        } catch (SQLException e) {

            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return ii;
    }

    /**
     * @return @throws SQLException
     */
    public boolean insertFromDefault(int newCompId) throws SQLException {
        dbAccess dba = null;
        PreparedStatement ps = null;

        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(INSERT_NEW_COMP_FROM_DEF);
            ps.setInt(1, newCompId);
            java.util.Date createdDate = new Date();
            ps.setTimestamp(2, new java.sql.Timestamp(createdDate.getTime()));
            ps.setTimestamp(3, new java.sql.Timestamp(createdDate.getTime()));
            ps.setInt(4, dsId);
            ps.setInt(5, compId);

            int cnt = ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("Failed in BtoCsmsgDao.insert ", e);
            throw new dbException(e);
        } finally {
            if (dba != null) {
                dba.close(ps);
            }
        }
    }

    public int getDsId() {
        return dsId;
    }

    public void setDsId(int dsId) {
        this.dsId = dsId;
    }

    public int[] updateCMCConfigItems(List<BtoConfigDao> configList) throws SQLException {
        PreparedStatement ps = null;
        dbAccess dba = null;
        int[] ii = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(UPDATE_CMC_INFO_SQL.toString());
            for (BtoConfigDao conf : configList) {
                ps.setString(1, conf.getValue1());
                ps.setTimestamp(2, conf.getUpdatedAt());
                ps.setString(3, conf.getItemname());
                ps.setInt(4, conf.getDsId());
                ps.setInt(5, conf.getCompId());
                ps.addBatch();
            }
            ii = ps.executeBatch();
            ps.clearBatch();
        } catch (BatchUpdateException e) {
            ii = e.getUpdateCounts();
            for (int i = 0; i < ii.length; i++) {
                if (ii[i] >= 0) {
                    System.out.println("Successfully executed");
                } else if (ii[i] == Statement.SUCCESS_NO_INFO) {
                } else if (ii[i] == Statement.EXECUTE_FAILED) {
                    System.out.println("Failed to execute UpdateBatch ");

                }
            }
        } catch (SQLException e) {

            throw e;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                System.out.println(e.fillInStackTrace());
            }
        }
        return ii;
    }

    public static int configAuditCompanyCount(int dsId) {

        log.debug("Getting company count for ConfigAudit ");
        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int cnt = 0;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(config_audit_comp_cnt);
            ps.setInt(1, dsId);
            rs = ps.executeQuery();
            if (rs != null && rs.next()) {
                cnt = rs.getInt(1);
            }

        } catch (SQLException e) {
            log.error("Exception occurred while Getting company count for ConfigAudit. Exception ["
                    + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cnt;

    }

    private static String getConfigAuditListStatement(String GET_CONFIG_AUDIT
    // ,Date lastUpDt, Date startDt, Date endDt, String feinValue,String
    // legalNameValue
    ) {
        StringBuilder sql = new StringBuilder();
        sql.append(GET_CONFIG_AUDIT);
        sql.append(" AND  T1.DATASETID = ? ");
        sql.append(" order by C.fein,T2.ITEMNAME");

        return sql.toString();
    }

    public static List<ConfigurationAudit> getConfigurationAudit(int dsId, Properties configItemProp
    // ,Date lastUpdatedAtDt, Date startDt,Date endDt, String feinValue, String
    // legalNameValue
    ) throws SQLException {
        List<ConfigurationAudit> configAuditList = new ArrayList<ConfigurationAudit>();
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;
        dbAccess dba = null;
        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(getConfigAuditListStatement(GET_CONFIG_AUDIT));
            int count = 0;
            pstmt.setInt(++count, dsId);
            resultSet = pstmt.executeQuery();
            log.debug("Populating config audit list from result set");
            while (resultSet != null && resultSet.next()) {
                String itemName = resultSet.getString("ITEMNAME").trim().toLowerCase();
                log.debug("Populating values for itemname " + itemName);
                if (resultSet.getString("DSVALUE1") != null && resultSet.getString("COMPVALUE1") != null
                        && !(resultSet.getString("DSVALUE1").equalsIgnoreCase(resultSet.getString("COMPVALUE1")))) {
                    ConfigurationAudit config = new ConfigurationAudit();
                    config.setDsId(resultSet.getInt("DATASETID"));
                    config.setCompId(resultSet.getInt("COMPID"));
                    config.setLastUpdateAt(resultSet.getTimestamp("UPDATEDAT"));
                    config.setFein(resultSet.getString("FEIN").trim());
                    config.setLegalName(resultSet.getString("LEGALNAME").trim());
                    config.setConfigItem(itemName);

                    String dsValue = resultSet.getString("DSVALUE1").trim();
                    config.setDsValue(dsValue);
                    String compValue = resultSet.getString("COMPVALUE1").trim();
                    config.setCompValue(compValue);
                    config = populateValueDesc(itemName, configItemProp, config);
                    config.setConfigItemDesc(populateItemDesc(itemName, true, configItemProp));
                    config.setValue(AppConsts.VALUE1);
                    configAuditList.add(config);
                }
                if (resultSet.getString("DSVALUE2") != null && resultSet.getString("COMPVALUE2") != null
                        && v1v2ItemList.contains(itemName)
                        && !(resultSet.getString("DSVALUE2").equalsIgnoreCase(resultSet.getString("COMPVALUE2")))) {
                    ConfigurationAudit config = new ConfigurationAudit();
                    config.setDsId(resultSet.getInt("DATASETID"));
                    config.setCompId(resultSet.getInt("COMPID"));
                    config.setLastUpdateAt(resultSet.getTimestamp("UPDATEDAT"));
                    config.setFein(resultSet.getString("FEIN").trim());
                    config.setLegalName(resultSet.getString("LEGALNAME").trim());
                    config.setConfigItem(itemName);
                    String dsValue = resultSet.getString("DSVALUE2").trim();
                    config.setDsValue(dsValue);
                    String compValue = resultSet.getString("COMPVALUE2").trim();
                    config.setCompValue(compValue);
                    config = populateValueDesc(itemName, configItemProp, config);
                    config.setConfigItemDesc(populateItemDesc(itemName, false, configItemProp));
                    config.setValue(AppConsts.VALUE2);
                    configAuditList.add(config);
                }

            }
        } catch (SQLException e) {
            log.error("Error occurred in BtoConfigDao.getConfigurationAudit " + e.fillInStackTrace());
            throw new dbException(e);
        } finally {
            dba.close(resultSet, pstmt);
        }
        return configAuditList;
    }

    private static String populateItemDesc(String itemName, boolean isV1, Properties configItemProp) {
        log.debug("inside populateItemDesc for itemname " + itemName);
        // item description
        String itemDesc = null;
        if (StringUtils.isNotBlank(itemName) && !v1v2ItemList.contains(itemName)) {
            itemDesc = configItemProp.getProperty(itemName);
            if (itemDesc == null) {
                itemDesc = itemName;
            }
        } else if (StringUtils.isNotBlank(itemName) && v1v2ItemList.contains(itemName) && isV1) {
            itemDesc = configItemProp.getProperty("v1_" + itemName);
            if (itemDesc == null) {
                itemDesc = itemName;
            }
        } else if (StringUtils.isNotBlank(itemName) && v1v2ItemList.contains(itemName) && !isV1) {
            itemDesc = configItemProp.getProperty("v2_" + itemName);
            if (itemDesc == null) {
                itemDesc = itemName;
            }
        }
        return itemDesc;
    }

    private static ConfigurationAudit populateValueDesc(String itemName, Properties configItemProp,
            ConfigurationAudit config) {
        log.debug("inside populateValueDesc for itemname " + itemName);

        // decrypt values.Dont need to pull descr from config properties
        if (StringUtils.isNotBlank(itemName) && decryptValueItemList.contains(itemName)) {
            config.setDsValueDesc(CryptUtils.aesDecrypt(config.getDsValue(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5));
            config.setCompValueDesc(
                    CryptUtils.aesDecrypt(config.getCompValue(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5));
        } else if (StringUtils.isNotBlank(itemName) && obfuscateItemList.contains(itemName)) {
            config.setDsValueDesc(BtoConfigDao.OBFSE_STR);
            config.setCompValueDesc(BtoConfigDao.OBFSE_STR);
        } // to pull spl value descr,so far we have only in value1
        else if (StringUtils.isNotBlank(itemName) && splValueItemList.contains(itemName)) {
            String dsValueDesc = configItemProp.getProperty(itemName + "_" + config.getDsValue());
            if (dsValueDesc == null) {
                dsValueDesc = config.getDsValue();
            }
            config.setDsValueDesc(dsValueDesc);

            String compValueDesc = configItemProp.getProperty(itemName + "_" + config.getCompValue());
            if (compValueDesc == null) {
                compValueDesc = config.getCompValue();
            }
            config.setCompValueDesc(compValueDesc);
        } else {
            // to pull value descr
            String dsValueDesc = config.getDsValue().toLowerCase();
            if (!blockConversionList.contains(configItemProp.getProperty(itemName))) {
                dsValueDesc = configItemProp.getProperty(dsValueDesc);
            }
            if (dsValueDesc == null) {
                dsValueDesc = config.getDsValue();
            }
            config.setDsValueDesc(dsValueDesc);

            String compValueDesc = config.getCompValue().toLowerCase();
            if (!blockConversionList.contains(configItemProp.getProperty(itemName))) {
                compValueDesc = configItemProp.getProperty(compValueDesc);
            }

            if (compValueDesc == null) {
                compValueDesc = config.getCompValue();
            }
            config.setCompValueDesc(compValueDesc);
        }
        return config;
    }

    public Date getLastUpdatedAt(int dsId, int compId) throws SQLException {
        Date result = null;

        dbAccess dba = null;
        PreparedStatement pstmt = null;
        ResultSet resultSet = null;

        StringBuffer sql = new StringBuffer();
        sql.append("Select max(updatedat) LASTUPDATEDAT from OBX.BTOCONFIG where Datasetid= ?  and COMPID = ? ");
        try {
            dba = new dbAccess();
            pstmt = dba.getPreparedStatement(sql.toString());

            if (pstmt != null) {
                pstmt.setInt(1, dsId);
                pstmt.setInt(2, compId);

                log.debug("Executing query: " + sql.toString());
                log.debug("With parameters: ");
                log.debug(" [DATASETID = " + dsId + "]");
                log.debug(" [COMPID = " + compId + "]");

                resultSet = pstmt.executeQuery();

                if (resultSet.next()) {
                    result = resultSet.getDate("LASTUPDATEDAT");
                    log.debug(" [Query result is " + result + "]");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(e.fillInStackTrace());
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            dba.close();
        }

        return result;
    }

    public static boolean updateConfigInBatch(List<ConfigurationAudit> configList, boolean isValue1) {
        log.info("updateConfigInBatch : " + isValue1);
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE OBX.BTOCONFIG SET ");
        if (isValue1) {
            sql.append(" VALUE1 = ? ");
        } else {
            sql.append(" VALUE2 = ? ");
        }
        sql.append(" ,UPDATEDAT = ? WHERE DATASETID = ? AND COMPID = ? AND ITEMNAME = ?  ");

        PreparedStatement ps = null;
        dbAccess dba = null;
        int batchSize = 50;
        int count = 0;
        int[] updateConfigInBatch = null;
        try {
            dba = new dbAccess();
            ps = dba.getDbConnection().prepareStatement(sql.toString());
            java.util.Date updatedDate = new Date();

            for (ConfigurationAudit config : configList) {

                ps.setString(1, config.getDsValue());
                ps.setTimestamp(2, new java.sql.Timestamp(updatedDate.getTime()));
                ps.setInt(3, config.getDsId());
                ps.setInt(4, config.getCompId());
                ps.setString(5, config.getConfigItem());

                ps.addBatch();
                count++;
                if (count % batchSize == 0) {
                    updateConfigInBatch = ps.executeBatch();
                    ps.clearBatch();
                }
            }
            updateConfigInBatch = ps.executeBatch();
            ps.clearBatch();
        } catch (BatchUpdateException e) {
            updateConfigInBatch = e.getUpdateCounts();
            log.error("Failed while updateRunlocatInBatch : " + updateConfigInBatch);
        } catch (SQLException e) {
            log.error("Unable to update updateRunlocatInBatch table!", e);
        } finally {
            dba.close(ps);
        }
        return true;
    }

    /**
     * getGlobalConfigValues
     *
     * @return
     */
    public static HashMap<String, String> getScheduledImportGlobalConfigValues() {

        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap<String, String> custConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(scheduled_import_global_config_values);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                custConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE1").trim());

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting Scheduled Import Global Config Values values. Exception ["
                    + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return custConfigValues;
    }

    public static Boolean isScheduledImportFeatureAvailable(String provider) {
        HashMap<String, String> si_globalConfig = getScheduledImportGlobalConfigValues();
        String scheduleImport = null;
        Boolean isScheduledImportFeatureAvailable = false;
        if (provider.equalsIgnoreCase(AppConsts.WORKDAY_SYSTEM)) {
            scheduleImport = si_globalConfig.get(AppConsts.WORKDAY_SCHEDULED_IMPORT_AVAILABLE);
        } else if (provider.equalsIgnoreCase(AppConsts.SAP_SYSTEM)) {
            scheduleImport = si_globalConfig.get(AppConsts.SAP_SCHEDULED_IMPORT_AVAILABLE);
        } else if (provider.equalsIgnoreCase(AppConsts.SAP_CLOUD_SYSTEM)) {
            scheduleImport = si_globalConfig.get(AppConsts.SAP_SCHEDULED_IMPORT_AVAILABLE);
        } else if (provider.equalsIgnoreCase(AppConsts.LAWSON_SYSTEM)) {
            scheduleImport = si_globalConfig.get(AppConsts.LAWSON_SCHEDULED_IMPORT_AVAILABLE);
        } else if (provider.equalsIgnoreCase(AppConsts.NO_ERPSYSTEM)) {
            scheduleImport = si_globalConfig.get(AppConsts.NONE_SCHEDULED_IMPORT_AVAILABLE);
        }
        if (scheduleImport != null && scheduleImport.equalsIgnoreCase(SCHEDULED_IMPORT_AVAILABLE)) {
            isScheduledImportFeatureAvailable = true;
        }
        return isScheduledImportFeatureAvailable;
    }

    public static CompanyConfigInfo getCompConfigInfo(int dsId, int compId) throws SQLException {

        dbAccess dba = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String samlcid = null;
        String samluid = null;
        String dsetName = null;
        CompanyConfigInfo companyInfo = new CompanyConfigInfo();
        companyInfo.setDsId(dsId);
        companyInfo.setCompId(compId);

        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(get_comp_config_items);
            ps.setInt(1, dsId);
            ps.setInt(2, compId);

            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                if (samlcid == null) {
                    samlcid = rs.getString("SAMLCID").trim();
                    companyInfo.setSamlcid(samlcid);
                }
                if (samluid == null) {
                    samluid = rs.getString("SAMLUID").trim();
                    companyInfo.setSamluid(samluid);
                }
                if (dsetName == null) {
                    dsetName = rs.getString("NAME").trim();
                    companyInfo.setDsetName(dsetName);
                }
                String itemname = rs.getString("ITEMNAME").trim();
                if (itemname.equalsIgnoreCase(AppConsts.ERPSYSTEM)) {
                    companyInfo.setErpSource(rs.getString("VALUE1").trim());
                } else if (itemname.equalsIgnoreCase(AppConsts.ERPSYTEM_DEST)) {
                    companyInfo.setErpDest(rs.getString("VALUE1").trim());
                } else if (itemname.equalsIgnoreCase(AppConsts.ITEM_DATA_TRANSFER_TYPE)) {
                    companyInfo.setDataTransType(rs.getString("VALUE1").trim());
                } else if (itemname.equalsIgnoreCase(AppConsts.ITEM_USERNAME)) {
                    companyInfo.setUserName(rs.getString("VALUE1").trim());
                } else if (itemname.equalsIgnoreCase(AppConsts.ITEM_PASSWORD)) {
                    companyInfo.setPassword(rs.getString("VALUE1").trim());
                }

            }

        } catch (SQLException e) {
            log.error("Exception occurred while getting cmcPg config values. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return companyInfo;
    }

    public int getCompId() {
        return compId;
    }

    public void setCompId(int compId) {
        this.compId = compId;
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

    public static HashMap<String, String> getTJCGlobalConfigValues() {

        PreparedStatement ps = null;
        ResultSet rs = null;
        dbAccess dba = null;
        HashMap<String, String> custConfigValues = new HashMap<String, String>();
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(TJC_GLOBAL_CONFIG_VALUES);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                custConfigValues.put(rs.getString("ITEMNAME").trim(), rs.getString("VALUE1").trim());
            }
        } catch (SQLException e) {
            log.error("Exception occurred while getting employer Global Config Values values. Exception ["
                    + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return custConfigValues;
    }

    public static String getDBProductVersion() {
        String dbVer = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        dbAccess dba = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(DB_PRODUCT_VER);
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                dbVer = rs.getString("VALUE1").trim();
            }
        } catch (SQLException e) {
            log.error("Exception occurred while getting getDBProductVersion. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbVer;
    }

    public static String getSAMLOption(String companyUUID) {
        String samlConfig = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        dbAccess dba = null;
        try {
            dba = new dbAccess();
            ps = dba.getPreparedStatement(SAML_OPTION);
            ps.setString(1, CryptUtils.aesEncrypt(companyUUID, CryptUtils.TRANSFORMATION_AES_CBC_PKCS5));
            rs = ps.executeQuery();
            while (rs != null && rs.next()) {
                samlConfig = rs.getString("VALUE1").trim();
            }
        } catch (SQLException e) {
            log.error("Exception occurred while getting getSAMLOption. Exception [" + e.fillInStackTrace() + "]");
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return samlConfig;
    }

}
