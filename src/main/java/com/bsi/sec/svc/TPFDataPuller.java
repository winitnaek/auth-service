/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

import com.bsi.sec.dto.DataSyncResponse;
import com.bsi.sec.domain.Company;
import com.bsi.sec.repository.CompanyRepository;
import com.bsi.sec.tpfrepository.BtoCompRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bsi.sec.tpfdomain.Btocomp;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;
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
    
    @Autowired
    private CompanyRepository companyRepository;
    
    
    /**
     * runInitialSync
     * @param fromDateTime
     * @return
     * @throws Exception 
     */
    @Override
    public DataSyncResponse runInitialSync(LocalDateTime fromDateTime) throws Exception {
        List<Btocomp> btoComplist = getCompanyDataForSync(fromDateTime);
        List<Company> companyList = prepareCompanyList(btoComplist);
        DataSyncResponse dataSyncResponse = upsertCompanyDataForSync(companyList);
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
        List<Btocomp> companies =  btoCompRepository.getAllComps();
        System.out.println("companies "+companies);
        if(companies !=null){
            System.out.println("companies "+companies.size());
            for (Btocomp company : companies) {
            System.out.println(company.toString());
            }
        }
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
        List<Btocomp> btoComplist = null;
        if (fromDateTime == null) {
            Calendar now = Calendar.getInstance();
            now.set(Calendar.HOUR_OF_DAY, 23);
            now.set(Calendar.MINUTE, 59);
            now.set(Calendar.SECOND, 59);
            log.debug("Current : " + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
            now.add(Calendar.YEAR, -10);
            log.debug("Current -10 Yrs : " + now.get(Calendar.YEAR) + "-" + (now.get(Calendar.MONTH) + 1) + "-" + now.get(Calendar.DATE) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
            Date fromDate = now.getTime();
            btoComplist = btoCompRepository.getCompanyDataForSync(fromDate);
        } else {
            Date fromDate = Date.from( fromDateTime.atZone( ZoneId.systemDefault()).toInstant());
            btoComplist = btoCompRepository.getCompanyDataForSync(fromDate);
            System.out.println(btoComplist.size());
            System.out.println(btoComplist.get(0).getSamlcid());
        }
        return btoComplist;
    }
    
    /**
     * prepareCompanyList
     * @param btoComplist
     * @return 
     */
    private List<Company> prepareCompanyList(List<Btocomp> btoComplist) {
        log.debug("Inside prepareCompanyList");
        log.debug("BtoComp Count : "+btoComplist.size());
        List<Company> companyList = new ArrayList<>();
        for (Btocomp btocomp : btoComplist) {
            Company company = new Company();
            company.setDataset(btocomp.getBtodset().getName());
            company.setEnabled(Boolean.TRUE);
            company.setId((long) btocomp.getBtocompPK().getCompid());
            company.setImported(Boolean.TRUE);
            company.setImportedDate(Instant.EPOCH);
            company.setName(btocomp.getLegalname());
            company.setSamlCid(btocomp.getSamlcid());
            companyList.add(company);
        }
        log.debug("Returning  Company List count: "+companyList.size());
        return companyList;
    }
    
    /**
     * upSertCompanyDataForSync
     * @param compnatList
     * @return 
     */
    private DataSyncResponse upsertCompanyDataForSync(List<Company> companyList){
        log.debug("Inside upsertCompanyDataForSync : ");
        log.debug("Received Company Count : "+companyList.size());
        companyRepository.saveAll(companyList);
        System.out.println("\n>>> Added " + companyRepository.count() + " Persons into the repository.");
        return null;
    }
}
