/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import java.time.LocalDateTime;

/**
 *
 * @author vnaik
 */
public class DataSyncResponse {
    private LocalDateTime lastRunDateTime;
    private Boolean isSucessfull;
    private String message;

    public LocalDateTime getLastRunDateTime() {
        return lastRunDateTime;
    }

    public void setLastRunDateTime(LocalDateTime lastRunDateTime) {
        this.lastRunDateTime = lastRunDateTime;
    }

    public Boolean getIsSucessfull() {
        return isSucessfull;
    }

    public void setIsSucessfull(Boolean isSucessfull) {
        this.isSucessfull = isSucessfull;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
