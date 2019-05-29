/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.SSOConfiguration;
import com.bsi.sec.repository.SSOConfigurationRepository;
import static com.bsi.sec.util.CacheConstants.SSO_CONFIGURATION_CACHE;
import com.bsi.sec.util.JpaQueries;
import com.bsi.sec.util.LogUtils;
import java.util.List;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author igorV
 */
@Component
public class SSOConfigDAO {

    private final static Logger log = LoggerFactory.getLogger(SSOConfigDAO.class);

    @Autowired
    private Ignite ignite;

    @Autowired
    private SSOConfigurationRepository repo;

    /**
     *
     * @param acctName
     * @param id
     * @return
     */
    public SSOConfiguration markSSOConfigAsLinked(String acctName, long id,
            boolean toUnlink, List<SSOConfiguration> confsOut) {
        SqlFieldsQuery sql = new SqlFieldsQuery(JpaQueries.GET_SSOCONFIGIDS_BY_ACCTNAME);
        IgniteCache<Long, SSOConfiguration> cache = ignite.cache(SSO_CONFIGURATION_CACHE);
        SSOConfiguration targetConf = null;

        try (QueryCursor<List<?>> cursor = cache.query(sql.setArgs(acctName))) {
            for (List<?> row : cursor) {
                Long confId = (Long) row.get(0);

                if (confId == null) {
                    continue;
                }

                SSOConfiguration conf = cache.get(confId);

                if (log.isTraceEnabled()) {
                    log.trace(LogUtils.jsonize(null, "acctName", acctName,
                            "id", id), "conf", conf.toString());
                }

                conf.setLinked(id == conf.getId() ? !toUnlink : false);
                confsOut.add(conf);

                if (targetConf == null && id == conf.getId()) {
                    targetConf = conf;
                }
            }
        }

        return targetConf;
    }

}
