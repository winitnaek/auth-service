/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;

/**
 * <p>
 * DTO for {@link com.bsi.sec.domain.AdminMetadata}.
 * </p>
 *
 * @author igorV
 */
public final class SyncInfoDTO {

    @Min(1L)
    private long id;

    private LocalDateTime lastFullSync;

    private LocalDateTime lastPerSync;

    private boolean isPerSyncOn;

    private boolean isSyncInProgress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getLastFullSync() {
        return lastFullSync;
    }

    public void setLastFullSync(LocalDateTime lastFullSync) {
        this.lastFullSync = lastFullSync;
    }

    public LocalDateTime getLastPerSync() {
        return lastPerSync;
    }

    public void setLastPerSync(LocalDateTime lastPerSync) {
        this.lastPerSync = lastPerSync;
    }

    public boolean isIsPerSyncOn() {
        return isPerSyncOn;
    }

    public void setIsPerSyncOn(boolean isPerSyncOn) {
        this.isPerSyncOn = isPerSyncOn;
    }

    public boolean isIsSyncInProgress() {
        return isSyncInProgress;
    }

    public void setIsSyncInProgress(boolean isSyncInProgress) {
        this.isSyncInProgress = isSyncInProgress;
    }

    @Override
    public String toString() {
        return "SyncInfoDTO{" + "id=" + id + ", lastFullSync=" + lastFullSync + ", lastPerSync=" + lastPerSync + ", isPerSyncOn=" + isPerSyncOn + ", isSyncInProgress=" + isSyncInProgress + '}';
    }

}
