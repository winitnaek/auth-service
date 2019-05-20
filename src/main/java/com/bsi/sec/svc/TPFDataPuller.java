/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.svc;

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
import static com.bsi.sec.util.AppConstants.BEAN_TPF_TRANSACTION_MANAGER_FACTORY;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * Component is responsible for providing functionality to pull data from the
 * TPF database.
 *
 * @author Vinit
 */
@Service
@Transactional(transactionManager = BEAN_TPF_TRANSACTION_MANAGER_FACTORY)
public class TPFDataPuller implements DataPuller {

    private final static Logger log = LoggerFactory.getLogger(TPFDataPuller.class);

    @Autowired
    private BtoCompRepository btoCompRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Override
    public List<Company> pullAll(LocalDateTime fromDtTm) throws Exception {
        List<Btocomp> btoComplist = getCompanyDataForSync(fromDtTm);
        List<Company> companyList = prepareCompanyList(btoComplist);
        return companyList;
    }

    @Override
    public List<Company> pullUpdates(LocalDateTime fromDtTm) throws Exception {
        return pullAll(fromDtTm);
    }

    /**
     * initialize
     *
     * @throws Exception
     */
    @Override
    public void initialize() throws Exception {
    }

    @Override
    public void postCleanup() throws Exception {
    }

    /**
     * getCompanyDataForSync
     *
     * @param fromDateTime
     * @return
     */
    private List<Btocomp> getCompanyDataForSync(LocalDateTime fromDateTime) {
        List<Btocomp> btoComplist = null;
        Date fromDate = getPeriodicSyncDateTime(fromDateTime);
        log.debug("fromDate : for periodic sync : " + fromDate);
        btoComplist = btoCompRepository.getCompanyDataForSync(fromDate);
        return btoComplist;
    }

    /**
     * getPeriodicSyncDateTime
     *
     * @param fromDateTime
     * @return
     */
    private Date getPeriodicSyncDateTime(LocalDateTime fromDateTime) {
        Date fromDate = Date.from(fromDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return fromDate;
    }

    /**
     * prepareCompanyList
     *
     * @param btoComplist
     * @return
     */
    private List<Company> prepareCompanyList(List<Btocomp> btoComplist) {
        log.debug("Inside prepareCompanyList");
        log.debug("BtoComp Count : " + btoComplist.size());
        List<Company> companyList = new ArrayList<>();
        btoComplist.stream().map((btocomp) -> {
            Company company = new Company();
            company.setDataset(btocomp.getBtodset().getName());
            company.setEnabled(Boolean.TRUE);
            company.setId((long) btocomp.getBtocompPK().getCompid());
            company.setImported(Boolean.TRUE);
            company.setImportedDate(Instant.now());
            company.setName(btocomp.getLegalname());
            company.setSamlCid(btocomp.getSamlcid());
            return company;
        }).forEachOrdered((company) -> {
            companyList.add(company);
        });
        log.debug("Returning  Company List count: " + companyList.size());
        return companyList;
    }

}
