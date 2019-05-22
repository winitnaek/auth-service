/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.AdminMetadata;
import com.bsi.sec.repository.AdminMetadataRepository;
import com.bsi.sec.util.LogUtils;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class AdminMetadataDao {

    private final static Logger log = LoggerFactory.getLogger(AdminMetadataDao.class);

    @Autowired
    private AdminMetadataRepository repo;

    /**
     * Returns <code>AdminMetadata</code> record.
     *
     * @return
     */
    public AdminMetadata get() {
        Iterator<AdminMetadata> adminMetaIter = repo
                .findAll().iterator();
        AdminMetadata admMeta = null;

        if (adminMetaIter != null && adminMetaIter.hasNext()) {
            admMeta = adminMetaIter.next();
        }

        if (log.isTraceEnabled() && admMeta != null) {
            log.trace(LogUtils.jsonize("AdminMetadata record", "rec", admMeta.toString()));
        }

        return admMeta;
    }
}
