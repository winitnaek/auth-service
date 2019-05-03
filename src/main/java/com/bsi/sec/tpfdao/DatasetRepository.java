/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.tpfdao;

import com.bsi.sec.tpfdomain.Btodset;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author SudhirP
 */
@Repository
public interface DatasetRepository extends CrudRepository<Btodset, Btodset>{

    Optional<Btodset> findByDatasetid(@Param("datasetid") int datasetid) ;  
    
}
