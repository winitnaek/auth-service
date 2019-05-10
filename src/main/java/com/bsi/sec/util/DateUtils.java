/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Put all date/time manipulation related operations here!
 *
 * @author igorV
 */
public final class DateUtils {

    /**
     * Returns Epoch as default "from" date/time.
     *
     * @return
     */
    public static LocalDateTime defaultFromSyncTime() {
        return LocalDateTime.ofInstant(Instant.EPOCH, ZoneOffset.UTC);
    }
}
