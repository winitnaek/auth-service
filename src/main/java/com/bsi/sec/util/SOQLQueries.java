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
public final class SOQLQueries {

    public static final String GET_ACTIVE_ENTITLEMENTS = "select e.Account_Name__c,"
            + " e.Account_18_Digit_ID__c, e.Product__c, e.Product_Name__c,"
            + " e.Product_Family__c, e.Product_Display_Name__c, e.Dataset_1__c,"
            + " e.Dataset_2__c, e.Dataset_3__c from Entitlement e"
            + " where e.SaaS_Active_Entitlement__c = 1 and e.Account_Name__c != null"
            + " and e.Product_Name__c != null and e.Dataset_1__c != null"
            + " and e.isDeleted = false and e.CreatedDate > :createddate"
            + " order by e.Product__c, e.Account_18_Digit_ID__c";
}
