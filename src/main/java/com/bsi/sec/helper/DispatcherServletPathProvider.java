/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletPath;
import org.springframework.stereotype.Component;

@Component
public class DispatcherServletPathProvider implements DispatcherServletPath {

    @Value("${server.servlet.context-path}")
    private String servletContextPath;

    @Override
    public String getPath() {
         return servletContextPath;
    }

}