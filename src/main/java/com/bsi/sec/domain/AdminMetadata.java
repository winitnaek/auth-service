package com.bsi.sec.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.hibernate.envers.Audited;

/**
 * A AdminMetadata.
 */
@Entity
@Audited
@Table(name = "admin_metadata")
public class AdminMetadata extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "last_full_sync", nullable = true)
    private LocalDateTime lastFullSync;

    @Column(name = "last_per_sync", nullable = true)
    private LocalDateTime lastPerSync;

    @Column(name = "is_per_sync_on", nullable = false)
    private boolean isPerSyncOn = true;

    @Column(name = "is_sync_inprogress", nullable = false)
    private boolean isSyncInProgress;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isIsSyncInProgress() {
        return isSyncInProgress;
    }

    public void setIsSyncInProgress(boolean isSyncInProgress) {
        this.isSyncInProgress = isSyncInProgress;
    }

    public boolean isIsPerSyncOn() {
        return isPerSyncOn;
    }

    public void setIsPerSyncOn(boolean isPerSyncOn) {
        this.isPerSyncOn = isPerSyncOn;
    }

    public LocalDateTime getLastPerSync() {
        return lastPerSync;
    }

    public void setLastPerSync(LocalDateTime lastPerSync) {
        this.lastPerSync = lastPerSync;
    }

    public LocalDateTime getLastFullSync() {
        return lastFullSync;
    }

    public void setLastFullSync(LocalDateTime lastFullSync) {
        this.lastFullSync = lastFullSync;
    }

    public AdminMetadata lastFullSync(LocalDateTime lastFullSync) {
        this.lastFullSync = lastFullSync;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.lastFullSync);
        hash = 79 * hash + Objects.hashCode(this.lastPerSync);
        hash = 79 * hash + (this.isPerSyncOn ? 1 : 0);
        hash = 79 * hash + (this.isSyncInProgress ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AdminMetadata other = (AdminMetadata) obj;
        if (this.isPerSyncOn != other.isPerSyncOn) {
            return false;
        }
        if (this.isSyncInProgress != other.isSyncInProgress) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.lastFullSync, other.lastFullSync)) {
            return false;
        }
        if (!Objects.equals(this.lastPerSync, other.lastPerSync)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AdminMetadata{" + "id=" + id + ", lastFullSync=" + lastFullSync + ", lastPerSync=" + lastPerSync + ", isPerSyncOn=" + isPerSyncOn + ", isSyncInProgress=" + isSyncInProgress + '}';
    }

}
