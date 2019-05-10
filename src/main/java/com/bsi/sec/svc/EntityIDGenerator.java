/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import javax.annotation.PostConstruct;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteAtomicSequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class EntityIDGenerator {

    private IgniteAtomicSequence sequence;

    @Autowired
    private Ignite igniteInstance;

    @PostConstruct
    public void initialize() {
        sequence = igniteInstance.atomicSequence(TENANT_CACHE, 1, true);
    }

    public long generate() {
        return sequence.getAndIncrement();
    }
}
