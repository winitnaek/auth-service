/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

/**
 *
 * @author vnaik
 */
public class JpaQueries {

    public static final String GET_CCOMPS = "from Btocomp c";
    public static final String GET_COMPANY_DATA_FOR_SYNC = "from Btocomp c where c.enabled = 1 and c.createdat > :fromDateTime";
    public static final String GET_TENANT_ID_BY_DSET_PROD_ACCT_FOR_SYNC = "select id from Tenant"
            + " where dataset = ? and prodName = ? and acctName = ? and imported = true";
    public static final String GET_TENANT_ID_BY_DSET_PROD_ACCT_FOR_INT_USER = "select id from Tenant"
            + " where dataset = ? and prodName = ? and acctName = ? and imported = false";
    public static final String GET_PROD_NAME_BY_ACCT_NAME = "select prodName from Tenant"
            + " where acctName = ? group by prodName";
    public static final String GET_COMP_ID_BY_DSET_COMPANYCID = "select id from Company"
            + " where dataset = ? and samlCid = ?";
    public static final String DELETE_COMP_ID_BY_DSET = "delete from Company"
            + " where dataset = ? and imported = false";
    public static final String GET_SSOCONFIGIDS_BY_ACCTNAME = "select id from SSOConfiguration c"
            + " where c.acctName = ?";
    public static final String DELETE_SSO_CONFIG = "DELETE FROM SSOConfiguration WHERE id = ?";
}
