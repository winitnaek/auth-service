/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

/**
 * JSR-303 validation messages.
 *
 * @author igorV
 */
public final class ValidatorMessages {

    public static final String DATASET_EMPTY = "Dataset is required!";
    public static final String DATASET_EXCEEDS_SIZE = "Dataset '${validatedValue}' must not exceed {max} characters long!";
    public static final String USERID_EMPTY = "User ID is required!";
}
