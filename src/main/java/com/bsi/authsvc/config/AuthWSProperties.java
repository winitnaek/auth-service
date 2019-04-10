/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.authsvc.config;

import com.bsi.authsvc.util.CryptUtils;
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
@ConfigurationProperties(prefix = "authws", ignoreUnknownFields = true)
@Validated
public class AuthWSProperties {

    @Value("${spring.application.name}")
    private String appName;

    private boolean debugMode = false;

    @Valid
    private Ldap ldap = new Ldap();

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

    @Override
    public String toString() {
        return "AuthWSProperties{" + "appName=" + appName + ", debugMode=" + debugMode + ", ldap=" + ldap + ", user=" + user + ", paging=" + paging + ", directories=" + directories + ", ajp=" + ajp + '}';
    }

}
