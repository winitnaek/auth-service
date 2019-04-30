/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.config;

import com.bsi.sec.util.CryptUtils;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

/**
 *
 * @author igorV
 */
@ConfigurationProperties(prefix = "sws", ignoreUnknownFields = true)
@Validated
public class SecurityServiceProperties {

    @Value("${spring.application.name}")
    private String appName;

    private boolean debugMode = false;

    @Valid
    private Ldap ldap = new Ldap();

    @Valid
    private Jpa jpa = new Jpa();
    
    @Valid
    private TpfJpa tpfjpa = new TpfJpa();

    @Valid
    private DataSource dataSource = new DataSource();
    
    @Valid
    private TpfDataSource tpfDataSource = new TpfDataSource();

    @Valid
    private IntUser user = new IntUser();

    @Valid
    private Paging paging = new Paging();

    @Valid
    private Directories directories = new Directories();

    @Valid
    private Ajp ajp = new Ajp();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public Ldap getLdap() {
        return ldap;
    }

    public void setLdap(Ldap ldap) {
        this.ldap = ldap;
    }

    public IntUser getUser() {
        return user;
    }

    public void setUser(IntUser user) {
        this.user = user;
    }

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Directories getDirectories() {
        return directories;
    }

    public void setDirectories(Directories directories) {
        this.directories = directories;
    }

    public Ajp getAjp() {
        return ajp;
    }

    public void setAjp(Ajp ajp) {
        this.ajp = ajp;
    }

    public Jpa getJpa() {
        return jpa;
    }

    public void setJpa(Jpa jpa) {
        this.jpa = jpa;
    }

    public TpfJpa getTpfjpa() {
        return tpfjpa;
    }

    public void setTpfjpa(TpfJpa tpfjpa) {
        this.tpfjpa = tpfjpa;
    }
    
    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TpfDataSource getTpfDataSource() {
        return tpfDataSource;
    }

    public void setTpfDataSource(TpfDataSource tpfDataSource) {
        this.tpfDataSource = tpfDataSource;
    }
    
    /**
     * JPA properties
     */
    public static class Jpa {

        @NotEmpty
        private String dialect;

        public String getDialect() {
            return dialect;
        }

        public void setDialect(String dialect) {
            this.dialect = dialect;
        }

        @Override
        public String toString() {
            return "Jpa{" + "dialect=" + dialect + '}';
        }

    }
    
    public static class TpfJpa {

        @NotEmpty
        private String dialect;
        @NotEmpty
        private String defaultSchema;

        /**
         * @return the dialect
         */
        public String getDialect() {
            return dialect;
        }

        /**
         * @param dialect the dialect to set
         */
        public void setDialect(String dialect) {
            this.dialect = dialect;
        }

        /*
        * (non-Javadoc)
        *
        * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "TpfJpa [dialect=" + dialect + ", dfltSchema=" + defaultSchema + "]";
        }

        /**
         * @return the defaultSchema
         */
        public String getDefaultSchema() {
            return defaultSchema;
        }

        /**
         * @param defaultSchema the defaultSchema to set
         */
        public void setDefaultSchema(String defaultSchema) {
            this.defaultSchema = defaultSchema;
        }
    }

    /**
     * cache configuration
     */
    public static class Ajp {

        private final static int DFLT_MIN_PACKET_SIZE = 8192; // in bytes!!
        private final static int DFLT_MAX_PACKET_SIZE = 65536; // in bytes!!

        @NotNull
        private String protocol = "AJP/1.3";

        @NotNull
        private String scheme = "http";

        @NotNull
        private int port;

        @NotNull
        private boolean secure = false;

        @NotNull
        private boolean enabled = true;

        @NotNull
        private boolean remoteAuth = false;

        @NotNull
        private boolean traceEnabled = false;

        @Min(DFLT_MIN_PACKET_SIZE)
        @Max(DFLT_MAX_PACKET_SIZE)
        private int packetSize = DFLT_MIN_PACKET_SIZE;

        public int getPacketSize() {
            return packetSize;
        }

        public void setPacketSize(int packetSize) {
            this.packetSize = packetSize;
        }

        public boolean isTraceEnabled() {
            return traceEnabled;
        }

        public void setTraceEnabled(boolean traceEnabled) {
            this.traceEnabled = traceEnabled;
        }

        public String getProtocol() {
            return protocol;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public boolean isSecure() {
            return secure;
        }

        public void setSecure(boolean secure) {
            this.secure = secure;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isRemoteAuth() {
            return remoteAuth;
        }

        public void setRemoteAuth(boolean remoteAuth) {
            this.remoteAuth = remoteAuth;
        }

        @Override
        public String toString() {
            return "Ajp{" + "protocol=" + protocol + ", scheme=" + scheme + ", port=" + port + ", secure=" + secure + ", enabled=" + enabled + ", remoteAuth=" + remoteAuth + ", traceEnabled=" + traceEnabled + '}';
        }

    }

    /**
     * @author igorv
     *
     */
    public static class Ldap {

        @NotNull
        private boolean enabled;

        @NotEmpty
        private String url;

        @NotEmpty
        private String tld;

        @NotEmpty
        private String userDNSuffix;

        @NotEmpty
        private String groupCN;

        @NotEmpty
        private String groupSearchFilter;

        public String getUserDNSuffix() {
            return userDNSuffix;
        }

        public void setUserDNSuffix(String userDNSuffix) {
            this.userDNSuffix = userDNSuffix;
        }

        public String getGroupCN() {
            return groupCN;
        }

        public void setGroupCN(String groupCN) {
            this.groupCN = groupCN;
        }

        public String getGroupSearchFilter() {
            return groupSearchFilter;
        }

        public void setGroupSearchFilter(String groupSearchFilter) {
            this.groupSearchFilter = groupSearchFilter;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        public String getTld() {
            return tld;
        }

        public void setTld(String tld) {
            this.tld = tld;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return "Ldap{" + "enabled=" + enabled + ", url=" + url + ", tld=" + tld + ", userDNSuffix=" + userDNSuffix + ", groupCN=" + groupCN + ", groupSearchFilter=" + groupSearchFilter + '}';
        }

    }

    /**
     * Used for internal authentication needs.
     *
     * @author sudhir
     */
    public static class IntUser {

        @NotNull
        private boolean enabled;
        @NotNull
        private boolean encrypted;
        @NotEmpty
        private String name;
        @NotEmpty
        private String passwd;

        public String getPasswd() {
            if (!this.encrypted) {
                return passwd != null ? passwd.trim() : passwd;
            } else {
                return CryptUtils.aesDecrypt(passwd.trim(), CryptUtils.TRANSFORMATION_AES_CBC_PKCS5);
            }
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name != null ? name.trim() : name;
        }

        public void setPasswd(String passwd) {
            this.passwd = passwd;
        }

        public boolean isEncrypted() {
            return encrypted;
        }

        public void setEncrypted(boolean encrypted) {
            this.encrypted = encrypted;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        @Override
        public String toString() {
            return "IntUser{" + "enabled=" + enabled + ", encrypted=" + encrypted + ", name=" + name + ", passwd=" + passwd + '}';
        }

    }

    /**
     * <p>
     * Contains various directories (e.g. output directory, etc.).
     * </p>
     *
     * @author igorv
     *
     */
    public static class Directories {

        @NotEmpty
        private String tempDir;

        public Directories() {
            super();

        }

        /**
         * @return the tempDir
         */
        public String getTempDir() {
            return tempDir;
        }

        /**
         * @param tempDir the tempDir to set
         */
        public void setTempDir(String tempDir) {
            this.tempDir = tempDir;
        }

        @Override
        public String toString() {
            return "Directories{" + "tempDir=" + tempDir + '}';
        }

    }

    /**
     * @author igorv
     *
     */
    public static class Paging {

        @Min(10)
        private int recsPerPage;

        public Paging() {
            super();

        }

        /**
         * @return the recsPerPage
         */
        public int getRecsPerPage() {
            return recsPerPage;
        }

        /**
         * @param recsPerPage the recsPerPage to set
         */
        public void setRecsPerPage(int recsPerPage) {
            this.recsPerPage = recsPerPage;
        }

        /*
        * (non-Javadoc)
        *
        * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "Paging [recsPerPage=" + recsPerPage + "]";
        }
    }

    /**
     * Data Source properties.
     */
    public static class DataSource {

        @NotEmpty
        private String driverClassName;
        @NotEmpty
        private String url;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        @Override
        public String toString() {
            return "DataSource{" + "driverClassName=" + driverClassName + ", url=" + url + '}';
        }

    }
    
     public static class TpfDataSource {
        @NotEmpty
        private String username;
        @NotEmpty
        private String password;
        @NotEmpty
        private String driverClassName;
        @NotEmpty
        private String url;
        @Min(1)
        private int maxWait;
        @Min(1)
        private int maxActive;
        @Min(1)
        private int maxIdle;

        @NotNull
        private boolean removeAbandoned;

        @NotNull
        private boolean logAbandoned;
        @Min(1)
        private int removeAbandonedTimeout;
        @NotEmpty
        private String validationQuery;
        @Min(1)
        private int validationQueryTimeout;

        /**
         * @return the username
         */
        public String getUsername() {
            return username;
        }

        /**
         * @param username the username to set
         */
        public void setUsername(String username) {
            this.username = username;
        }

        /**
         * @return the password
         */
        public String getPassword() {
            return password;
        }

        /**
         * @param password the password to set
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * @return the driverClassName
         */
        public String getDriverClassName() {
            return driverClassName;
        }

        /**
         * @param driverClassName the driverClassName to set
         */
        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        /**
         * @return the url
         */
        public String getUrl() {
            return url;
        }

        /**
         * @param url the url to set
         */
        public void setUrl(String url) {
            this.url = url;
        }

        /**
         * @return the maxWait
         */
        public int getMaxWait() {
            return maxWait;
        }

        /**
         * @param maxWait the maxWait to set
         */
        public void setMaxWait(int maxWait) {
            this.maxWait = maxWait;
        }

        /**
         * @return the maxActive
         */
        public int getMaxActive() {
            return maxActive;
        }

        /**
         * @param maxActive the maxActive to set
         */
        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        /**
         * @return the maxIdle
         */
        public int getMaxIdle() {
            return maxIdle;
        }

        /**
         * @param maxIdle the maxIdle to set
         */
        public void setMaxIdle(int maxIdle) {
            this.maxIdle = maxIdle;
        }

        /**
         * @return the removeAbandoned
         */
        public boolean isRemoveAbandoned() {
            return removeAbandoned;
        }

        /**
         * @param removeAbandoned the removeAbandoned to set
         */
        public void setRemoveAbandoned(boolean removeAbandoned) {
            this.removeAbandoned = removeAbandoned;
        }

        /**
         * @return the logAbandoned
         */
        public boolean isLogAbandoned() {
            return logAbandoned;
        }

        /**
         * @param logAbandoned the logAbandoned to set
         */
        public void setLogAbandoned(boolean logAbandoned) {
            this.logAbandoned = logAbandoned;
        }

        /**
         * @return the removeAbandonedTimeout
         */
        public int getRemoveAbandonedTimeout() {
            return removeAbandonedTimeout;
        }

        /**
         * @param removeAbandonedTimeout the removeAbandonedTimeout to set
         */
        public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
            this.removeAbandonedTimeout = removeAbandonedTimeout;
        }

        /**
         * @return the validationQuery
         */
        public String getValidationQuery() {
            return validationQuery;
        }

        /**
         * @param validationQuery the validationQuery to set
         */
        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        /**
         * @return the validationQueryTimeout
         */
        public int getValidationQueryTimeout() {
            return validationQueryTimeout;
        }

        /**
         * @param validationQueryTimeout the validationQueryTimeout to set
         */
        public void setValidationQueryTimeout(int validationQueryTimeout) {
            this.validationQueryTimeout = validationQueryTimeout;
        }

        /*
        * (non-Javadoc)
        *
        * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "TpfDataSource [username=" + username + ", password=" + password + ", driverClassName="
                    + driverClassName + ", url=" + url + ", maxWait=" + maxWait + ", maxActive=" + maxActive
                    + ", maxIdle=" + maxIdle + ", removeAbandoned=" + removeAbandoned + ", logAbandoned=" + logAbandoned
                    + ", removeAbandonedTimeout=" + removeAbandonedTimeout + ", validationQuery=" + validationQuery
                    + ", validationQueryTimeout=" + validationQueryTimeout + "]";
        }
    }

    @Override
    public String toString() {
        return "SecurityServiceProperties{" + "appName=" + appName + ", debugMode=" + debugMode + ", ldap=" + ldap + ", jpa=" + jpa + ", dataSource=" + dataSource + ", user=" + user + ", paging=" + paging + ", directories=" + directories + ", ajp=" + ajp + '}';
    }

}
