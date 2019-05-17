/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import static com.bsi.sec.util.CacheConstants.ADMIN_METADATA_CACHE;
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

    @Autowired
    private Ignite igniteInstance;

    private IgniteAtomicSequence sequence;

    @PostConstruct
    public void initialize() {
        sequence = igniteInstance.atomicSequence(ADMIN_METADATA_CACHE, 0, true);
    }

    public long generate() {
        return sequence.incrementAndGet();
    }
}
