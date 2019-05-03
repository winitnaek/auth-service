/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.tpfrepository.BtoCompRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
/**
 *
 * Component is responsible for providing functionality to pull data from the
 * TPF database.
 *
 * @author igorV
 */
@Service
public class TPFDataPuller implements DataSync {
    
    @Autowired
    private BtoCompRepository btoCompRepository;

    @Override
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DataSyncResponse runPeriodicSync(LocalDateTime fromDateTime) throws Exception {
        //btoCompRepository.getAllComps();
        return null;
    }

    @Override
    public void initializeSync() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
