/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.tpfrepository.BtoCompRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * Component is responsible for providing functionality to pull data from the
 * TPF database.
 *
 * @author igorV
 */
@Service
public class TPFDataPuller implements DataPuller {
    
    @Autowired
    private BtoCompRepository btoCompRepository;

    @Override
    public void initialize() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initialPull() throws Exception {
        btoCompRepository.getAllComps();
    }

    @Override
    public void periodicPull() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
