/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class EntityIDGenerator {

    public long generate(Object o) {
        return Objects.hashCode(o);
    }
}
