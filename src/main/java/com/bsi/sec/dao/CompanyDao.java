/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.Company;
import static com.bsi.sec.util.CacheConstants.COMPANY_CACHE;
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
public class CompanyDao {

    private final static Logger log = LoggerFactory.getLogger(CompanyDao.class);

    @Autowired
    private Ignite ignite;

    /**
     *
     * @param dset
     * @param companyCID
     * @return
     */
    public Company getCompByDsetCompCID(String dset, String companyCID) {
        SqlFieldsQuery sql = new SqlFieldsQuery(JpaQueries.GET_COMP_ID_BY_DSET_COMPANYCID);
        IgniteCache<Long, Company> cache = ignite.cache(COMPANY_CACHE);
        Long compId = null;

        try (QueryCursor<List<?>> cursor = cache.query(sql.setArgs(dset, companyCID))) {
            for (List<?> row : cursor) {
                compId = (Long) row.get(0);

                if (log.isTraceEnabled()) {
                    log.trace(LogUtils.jsonize("getCompByDsetCompCID(...)", "dataset", dset,
                            "companyCID", companyCID));
                }

                break;
            }
        }

        Company company = null;

        if (compId != null) {
            company = cache.get(compId);
        }

        return company;
    }

}
