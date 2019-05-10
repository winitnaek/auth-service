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
    private boolean isPerSyncOn;

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdminMetadata adminMetadata = (AdminMetadata) o;
        if (adminMetadata.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), adminMetadata.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AdminMetadata{" + "id=" + id + ", lastFullSync=" + lastFullSync + ", lastPerSync=" + lastPerSync + ", isPerSyncOn=" + isPerSyncOn + ", isSyncInProgress=" + isSyncInProgress + '}';
    }

}
