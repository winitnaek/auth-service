/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dao;

import com.bsi.sec.domain.Tenant;
import com.bsi.sec.dto.ProductDTO;
import com.bsi.sec.svc.EntityIDGenerator;
import static com.bsi.sec.util.CacheConstants.TENANT_CACHE;
import com.bsi.sec.util.JpaQueries;
import com.bsi.sec.util.LogUtils;
import java.util.ArrayList;
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
public class TenantDao {

    private final static Logger log = LoggerFactory.getLogger(TenantDao.class);

    @Autowired
    private Ignite ignite;

    @Autowired
    private EntityIDGenerator idGen;

    /**
     *
     * @param dset
     * @param prodName
     * @param acctName
     * @return
     */
    public Tenant getTenantByDsetProdAcct(String dset, String prodName, String acctName, boolean intUser) {
        SqlFieldsQuery sql = new SqlFieldsQuery(!intUser
                ? JpaQueries.GET_TENANT_ID_BY_DSET_PROD_ACCT_FOR_SYNC
                : JpaQueries.GET_TENANT_ID_BY_DSET_PROD_ACCT_FOR_INT_USER);
        IgniteCache<Long, Tenant> cache = ignite.cache(TENANT_CACHE);
        Long tenId = null;

        try (QueryCursor<List<?>> cursor = cache.query(sql.setArgs(dset, prodName, acctName))) {
            for (List<?> row : cursor) {
                tenId = (Long) row.get(0);

                if (log.isTraceEnabled()) {
                    log.trace(LogUtils.jsonize("getTenantByDsetProdAcct(...)", "dataset", dset,
                            "prodname", prodName, "acctname", acctName, "tenantId", tenId));
                }

                break;
            }
        }

        Tenant tenant = null;

        if (tenId != null) {
            tenant = cache.get(tenId);
        }

        return tenant;
    }

    /**
     *
     * @param acctName
     * @return
     */
    public List<ProductDTO> getProductsByAcctname(String acctName) {
        SqlFieldsQuery sql = new SqlFieldsQuery(
                JpaQueries.GET_PROD_NAME_BY_ACCT_NAME);
        IgniteCache<Long, Tenant> cache = ignite.cache(TENANT_CACHE);
        List<ProductDTO> products = new ArrayList<>(0);

        try (QueryCursor<List<?>> cursor = cache.query(sql.setArgs(acctName))) {
            for (List<?> row : cursor) {
                String prodName = (String) row.get(0);

                if (log.isTraceEnabled()) {
                    log.trace(LogUtils.jsonize("getProductsByAcctname(...)",
                            "prodname", prodName, "acctname", acctName));
                }

                ProductDTO prod = new ProductDTO(idGen.generate(), acctName, prodName);
                products.add(prod);
            }
        }

        return products;
    }
}
