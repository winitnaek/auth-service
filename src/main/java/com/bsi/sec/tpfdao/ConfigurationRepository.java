/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfdao;

import com.bsi.sec.tpfdomain.Btoconfig;
import com.bsi.sec.tpfdomain.BtoconfigPK;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author SudhirP
 */
@Repository
public interface ConfigurationRepository extends CrudRepository<Btoconfig, BtoconfigPK>{
    @Query("select c from Btoconfig c where c.btoconfigPK.datasetid=:datasetid and "
             + "c.btoconfigPK.compid=:compid and c.btoconfigPK.itemname=:itemname")
    Optional<Btoconfig> findConfiguration(@Param("datasetid") int datasetid,
                                 @Param("compid") int compid, @Param("itemname") String itemname);
}
