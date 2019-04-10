/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.svc;

import org.springframework.stereotype.Service;

/**
 *
 * @author igorV
 */
@Service
public class AuthenticationService {

    public String echo(String text) {
        return "Security Echo Service Says:  " + text;
    }
}
