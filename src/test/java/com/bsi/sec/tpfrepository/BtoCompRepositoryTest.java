package com.bsi.sec.tpfrepository;


import com.bsi.sec.base.BaseTest;
import com.bsi.sec.config.TPFStoreConfiguration;
import com.bsi.sec.startup.ApplicationInitializer;
import com.bsi.sec.tpfdomain.Btocomp;
import static com.bsi.sec.util.AppConstants.BEAN_TPF_TRANSACTION_MANAGER_FACTORY;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ApplicationInitializer.class})
@ContextConfiguration(classes = {TPFStoreConfiguration.class})
@TestPropertySource("classpath:test.properties")
@Transactional(transactionManager=BEAN_TPF_TRANSACTION_MANAGER_FACTORY)
public class BtoCompRepositoryTest extends BaseTest {

    @Autowired
    private BtoCompRepository btoCompRepository;
    
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
    }

    @After
    public void after() {
    }

    @Test
    public void getValueSuccessTest() throws Exception {
        List<Btocomp> btocomps = btoCompRepository.getAllComps();
        System.out.println(btocomps.get(0).getSamlcid());
    }
}