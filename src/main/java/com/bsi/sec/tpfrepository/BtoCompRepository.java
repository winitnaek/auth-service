/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfrepository;
import static com.bsi.sec.util.AppConstants.BEAN_TPF_TRANSACTION_MANAGER_FACTORY;
import static com.bsi.sec.util.JpaQueries.GET_CCOMPS;
import static com.bsi.sec.util.JpaQueries.GET_COMPANY_DATA_FOR_SYNC;
import com.bsi.sec.tpfdomain.Btocomp;
import com.bsi.sec.tpfdomain.BtocompPK;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author vnaik
 */
@Repository
@Transactional(BEAN_TPF_TRANSACTION_MANAGER_FACTORY) 
public interface BtoCompRepository extends JpaRepository<Btocomp, BtocompPK> {

    @Query(GET_CCOMPS)
    public List<Btocomp> getAllComps();
    
    @Query(GET_COMPANY_DATA_FOR_SYNC)
    public List<Btocomp> getCompanyDataForSync();
}
