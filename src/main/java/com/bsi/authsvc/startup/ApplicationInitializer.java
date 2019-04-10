/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.startup;

import com.bsi.authsvc.config.AuthWSProperties;
import com.bsi.authsvc.config.DispatcherConfigurationHelper;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import javax.annotation.PostConstruct;
import javax.naming.ConfigurationException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.context.WebApplicationContext;

/**
 *
 * @author igorV
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
    DataSourceTransactionManagerAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class, SecurityAutoConfiguration.class,
    MultipartAutoConfiguration.class, DispatcherServletAutoConfiguration.class},
        scanBasePackages = {"com.bsi.authsvc.startup", "com.bsi.authsvc.config", "com.bsi.authsvc.svc", "com.bsi.authsvc.sec", "com.bsi.authsvc.ws"})
@EnableConfigurationProperties({AuthWSProperties.class})
public class ApplicationInitializer implements WebServerFactoryCustomizer<TomcatServletWebServerFactory>, ServletContextInitializer {

    private static final Logger log = LoggerFactory.getLogger(ApplicationInitializer.class);

    @Autowired
    private AuthWSProperties props;

    @Autowired
    private AuthWSInitializer initializer;

    @Autowired
    private WebApplicationContext appContext;

    @Value("${server.http.port:#{-1}}")
    private int httpPort;

    /**
     * Main method, used to run the application.
     *
     * @param args the command line arguments
     * @throws UnknownHostException if the local host name could not be resolved
     * into an address
     * @throws ConfigurationException
     */
    public static void main(String[] args) throws UnknownHostException, ConfigurationException {
        SpringApplication app = new SpringApplication(ApplicationInitializer.class);

        Environment env = app.run(args).getEnvironment();

        if (log.isInfoEnabled()) {
            log.info("\nApplication '{}' is running at http://{}:{}!\n", env
                    .getProperty("spring.application.name"), InetAddress
                    .getLocalHost().getHostAddress(), env
                            .getProperty("server.port"));
        }

    }

    @Override
    public void customize(TomcatServletWebServerFactory container) {
        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("html", "text/html;charset=utf-8");
        container.setMimeMappings(mappings);

        if (httpPort > 0) {
            initializeHttp(container);
        }

        initializeAjp(container);
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    }

    /**
     * Initializes app.
     * <p>
     * Spring profiles can be configured with a program arguments
     * --spring.profiles.active=your-active-profile
     * <p>
     *
     * @throws ConfigurationException
     */
    @PostConstruct
    public void initApplication() throws ConfigurationException {
        if (log.isDebugEnabled()) {
            log.debug("post initialization" + appContext);
        }
        
        initializeLogging();
        initializeApplicationServices();
    }

    /**
     * Configure http
     *
     * @param container
     */
    private void initializeHttp(TomcatServletWebServerFactory container) {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        //
        @SuppressWarnings("rawtypes")
        AbstractHttp11Protocol protocol = ((AbstractHttp11Protocol<?>) connector.getProtocolHandler());
        protocol.setMaxSwallowSize(-1);
        connector.setPort(httpPort);
        connector.setMaxPostSize(-1);
        container.addAdditionalTomcatConnectors(connector);
    }

    /**
     * Configure AJP
     *
     * @param container
     */
    private void initializeAjp(TomcatServletWebServerFactory container) {

        Connector connector = new Connector(props.getAjp().getProtocol());
        connector.setPort(props.getAjp().getPort());
        connector.setSecure(props.getAjp().isSecure());
        connector.setAllowTrace(props.getAjp().isTraceEnabled());
        connector.setScheme(props.getAjp().getScheme());
        connector.setMaxPostSize(-1);
        connector.setAttribute("packetSize", props.getAjp().getPacketSize());
        container.addAdditionalTomcatConnectors(connector);

    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Sets default logging level globally as 'FINEST'.
     */
    private void initializeLogging() {
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        java.util.logging.Logger.getLogger("global").setLevel(Level.FINEST);
        java.util.logging.Logger.getLogger("org.apache.catalina").setLevel(Level.FINEST);
        java.util.logging.Logger.getLogger("org.apache.catalina.core.ContainerBase.[Tomcat].[localhost]").setLevel(Level.FINEST);
    }

    private void initializeApplicationServices() {
        initializer.initialize();
    }

}
