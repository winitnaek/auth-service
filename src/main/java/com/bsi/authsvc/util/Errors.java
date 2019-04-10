/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.util;

import static com.bsi.authsvc.util.Errors.Code.ERR_CODE_DATASET_NOT_FOUND;
import static com.bsi.authsvc.util.Errors.Code.ERR_CODE_INVALID_USER_NAME;
import static com.bsi.authsvc.util.Errors.Code.ERR_CODE_TEMP_PATH_FAILED_TO_CREATE_DIR;
import static com.bsi.authsvc.util.Errors.Code.ERR_CODE_UNABLE_TO_PROC_REQ;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author igorV
 */
public enum Errors {
    INVALID_USER_NAME(ERR_CODE_INVALID_USER_NAME,
            "Username [%s] not found for the dataset [%s]!"),
    DATASET_NOT_FOUND(ERR_CODE_DATASET_NOT_FOUND, "Dataset [%s] not found!"),
    FAILED_TO_CREATE_TEMPDIR(ERR_CODE_TEMP_PATH_FAILED_TO_CREATE_DIR, "Failed to create temp directory [%s]!"),
    UNABLE_TO_PROC_REQ(ERR_CODE_UNABLE_TO_PROC_REQ, "Unable to process request for Dataset ID [%d]! %s");

    private int code;
    private String msg;

    /**
     * @param code
     * @param msg
     */
    private Errors(String msg) {
        this.msg = msg;
    }

    /**
     * @param code
     * @param msg
     */
    private Errors(int code, String msg) {
        this(msg);
        this.code = code;
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    /**
     * @return the desc
     */
    public String getDesc() {
        return String.format("[%d] %s", code, msg);
    }

    /**
     * @return
     */
    public String getDesc(Object... args) {
        return String.format("Error Code -> [" + code + "]. Message -> "
                + StringUtils.trimToEmpty(msg), args);
    }

    /**
     *
     */
    public static class Code {

        // Configuration
        public static final int ERR_CODE_TEMP_PATH_FAILED_TO_CREATE_DIR = 1090;
        // Not Found
        public static final int ERR_CODE_DATASET_NOT_FOUND = 4010;
        // Invalid
        public static final int ERR_CODE_INVALID_USER_NAME = 5010;
        // General request processing
        public static final int ERR_CODE_UNABLE_TO_PROC_REQ = 9010;
    }
}
