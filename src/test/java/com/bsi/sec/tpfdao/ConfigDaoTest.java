package com.bsi.sec.tpfdao;

import static org.junit.Assert.assertTrue;

import com.bsi.sec.base.BaseTest;
import com.bsi.sec.config.TPFStoreConfiguration;
import com.bsi.sec.config.TestApplicationContext;

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


@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestApplicationContext.class})
@ContextConfiguration(classes = {TPFStoreConfiguration.class})
@TestPropertySource("classpath:test.properties")
public class ConfigDaoTest extends BaseTest {

    @Autowired
    private BtoConfigDao configDao;
    
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
        String itemval = configDao.getValue(443, 1451, "ERPSYSTEM");
        assertTrue("Lawson".equals(itemval));
    }
}