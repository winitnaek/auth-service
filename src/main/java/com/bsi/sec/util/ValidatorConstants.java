/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

import java.time.Year;

/**
 *
 * <p>
 * Validation constants to be used by JSR-303 annotations and Spring
 * Validators,</p>
 *
 * @author igorV
 */
public final class ValidatorConstants {

    public final static int DATASETID_MAX_LEN = 25;
    public final static int TRANSMITTER_MAX_LEN = 9;
    public final static int COMPANYID_LEN = 9;
    public final static int TAXCODE_MAX_LEN = 50;
    public final static int EMPID_MAX_LEN = 20;
    public final static int EMPKEY_LEN = 32;

    public final static String FEIN_PAT = "^\\d{9}$";
    public final static String YEAR_PAT = "^\\d{4}$";

    public final static int MIN_YEAR = Year.now().getValue() - 9;
    public final static int MAX_YEAR = Year.now().getValue() + 6;
    public final static int YEAR_LEN = 4;

    public final static int MIN_QTR = 1;
    public final static int MAX_QTR = 4;

    public final static int MIN_MON_IN_QTR = 1;
    public final static int MAX_MON_IN_QTR = 3;

    public static final int MAX_DATES_RANGE_IN_MONTHS = 4;

    public static final int MIN_REQNO = 1;
}
