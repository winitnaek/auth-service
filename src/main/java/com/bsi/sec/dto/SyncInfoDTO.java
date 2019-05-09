/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bsi.sec.dto;

import java.time.LocalDateTime;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

    private boolean isFullSFSyncOn;

    private boolean isSFSyncOn;

    private boolean isTPFSyncOn;

    @NotNull
    private LocalDateTime lastFullSFSync;

    @NotNull
    private LocalDateTime lastSFSync;

    @NotNull
    private LocalDateTime lastTPFSync;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isIsFullSFSyncOn() {
        return isFullSFSyncOn;
    }

    public void setIsFullSFSyncOn(boolean isFullSFSyncOn) {
        this.isFullSFSyncOn = isFullSFSyncOn;
    }

    public boolean isIsSFSyncOn() {
        return isSFSyncOn;
    }

    public void setIsSFSyncOn(boolean isSFSyncOn) {
        this.isSFSyncOn = isSFSyncOn;
    }

    public boolean isIsTPFSyncOn() {
        return isTPFSyncOn;
    }

    public void setIsTPFSyncOn(boolean isTPFSyncOn) {
        this.isTPFSyncOn = isTPFSyncOn;
    }

    public LocalDateTime getLastFullSFSync() {
        return lastFullSFSync;
    }

    public void setLastFullSFSync(LocalDateTime lastFullSFSync) {
        this.lastFullSFSync = lastFullSFSync;
    }

    public LocalDateTime getLastSFSync() {
        return lastSFSync;
    }

    public void setLastSFSync(LocalDateTime lastSFSync) {
        this.lastSFSync = lastSFSync;
    }

    public LocalDateTime getLastTPFSync() {
        return lastTPFSync;
    }

    public void setLastTPFSync(LocalDateTime lastTPFSync) {
        this.lastTPFSync = lastTPFSync;
    }

    @Override
    public String toString() {
        return "SyncInfoDTO{" + "id=" + id + ", isFullSFSyncOn=" + isFullSFSyncOn + ", isSFSyncOn=" + isSFSyncOn + ", isTPFSyncOn=" + isTPFSyncOn + ", lastFullSFSync=" + lastFullSFSync + ", lastSFSync=" + lastSFSync + ", lastTPFSync=" + lastTPFSync + '}';
    }

}
