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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bsi.sec.tpfdomain.Btocomp;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
/**
 *
 * Component is responsible for providing functionality to pull data from the
 * TPF database.
 *
 * @author Vinit
 */
@Service
public class TPFDataPuller implements DataSync {
    
     private final static Logger log = LoggerFactory.getLogger(TPFDataPuller.class);
    
    @Autowired
    private BtoCompRepository btoCompRepository;
    
    /**
     * runInitialSync
     * @param fromDateTime
     * @return
     * @throws Exception 
     */
    @Override
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception {
        List<Btocomp> compnatList = getCompanyDataForSync(fromDateTime);
        DataSyncResponse dataSyncResponse = upsertCompanyDataForSync(compnatList);
        return dataSyncResponse;
    }
    
    /**
     * runPeriodicSync
     * @param fromDateTime
     * @return
     * @throws Exception 
     */
    @Override
    public DataSyncResponse runPeriodicSync(LocalDateTime fromDateTime) throws Exception {
        btoCompRepository.getAllComps();
        return null;
    }
    
    /**
     * initializeSync
     * @throws Exception 
     */
    @Override
    public void initializeSync() throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    /**
     * getCompanyDataForSync
     * @param fromDateTime
     * @return 
     */
    private List<Btocomp> getCompanyDataForSync(LocalDateTime fromDateTime) {
        List<Btocomp> compnatList = null;
        if (fromDateTime == null) {
            Calendar now = Calendar.getInstance();
            now.set(Calendar.HOUR_OF_DAY, 23);
            now.set(Calendar.MINUTE, 59);
            now.set(Calendar.SECOND, 59);
            log.debug("Current : " + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
            now.add(Calendar.YEAR, -10);
            log.debug("Current -10 Yrs : " + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
            Date fromDate = now.getTime();
            compnatList = btoCompRepository.getCompanyDataForSync(fromDate);
            System.out.println(compnatList.size());
            System.out.println(compnatList.get(0).getSamlcid());
        } else {
            Date fromDate = Date.from( fromDateTime.atZone( ZoneId.systemDefault()).toInstant());
            compnatList = btoCompRepository.getCompanyDataForSync(fromDate);
            System.out.println(compnatList.size());
            System.out.println(compnatList.get(0).getSamlcid());
        }
        return compnatList;
    }
    
    /**
     * upSertCompanyDataForSync
     * @param compnatList
     * @return 
     */
    private DataSyncResponse upsertCompanyDataForSync(List<Btocomp> compnatList){
        return null;
    }
}
