/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * TODO: Add JSR-303 validation, comments.
 *
 * @author igorV
 */
public final class TenantSSOConfigDTO {

    @Min(1L)
    private Long id;

    @NotNull
    private TenantDTO tenant;

    @NotNull
    private SSOConfigDTO ssoConfig;

    public TenantSSOConfigDTO() {
    }

    public TenantSSOConfigDTO(Long id, TenantDTO tenant, SSOConfigDTO ssoConfig) {
        this.id = id;
        this.tenant = tenant;
        this.ssoConfig = ssoConfig;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TenantDTO getTenant() {
        return tenant;
    }

    public void setTenant(TenantDTO tenant) {
        this.tenant = tenant;
    }

    public SSOConfigDTO getSsoConfig() {
        return ssoConfig;
    }

    public void setSsoConfig(SSOConfigDTO ssoConfig) {
        this.ssoConfig = ssoConfig;
    }

    @Override
    public String toString() {
        return "TenantSSOConfigDTO{" + "id=" + id + ", tenant=" + tenant + ", ssoConfig=" + ssoConfig + '}';
    }

}