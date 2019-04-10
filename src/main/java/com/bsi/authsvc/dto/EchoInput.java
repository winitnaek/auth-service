/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author SudhirP
 */
@XmlRootElement
public class EchoInput {

    @NotNull
    @Size(min = 1)
    private String name;

    @NotNull
    @Size(min = 1)
    private String dataset;

    public void setName(String name) {
        this.name = name;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    @XmlElement
    @NotNull
    public String getName() {
        return name;
    }

    @XmlElement
    @NotNull
    public String getDataset() {
        return dataset;
    }
}
