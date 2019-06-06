/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import java.util.Map;
import javax.validation.constraints.NotNull;

/**
 *
 * @author SudhirP
 */
public class SSOResult {
     @NotNull
     private SSOAction action;
     Map<String,String> attributes; 

    public SSOAction getAction() {
        return action;
    }

    public void setAction(SSOAction action) {
        this.action = action;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }
    
}
