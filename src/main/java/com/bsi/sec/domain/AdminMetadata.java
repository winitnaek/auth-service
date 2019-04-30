package com.bsi.sec.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "is_full_sf_sync_on", nullable = false)
    private Boolean isFullSFSyncOn;

    @NotNull
    @Column(name = "is_sf_sync_on", nullable = false)
    private Boolean isSFSyncOn;

    @NotNull
    @Column(name = "is_tpf_sync_on", nullable = false)
    private Boolean isTPFSyncOn;

    @NotNull
    @Column(name = "last_full_sf_sync", nullable = false)
    private LocalDate lastFullSFSync;

    @NotNull
    @Column(name = "last_sf_sync", nullable = false)
    private LocalDate lastSFSync;

    @NotNull
    @Column(name = "last_tpf_sync", nullable = false)
    private LocalDate lastTPFSync;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIsFullSFSyncOn() {
        return isFullSFSyncOn;
    }

    public AdminMetadata isFullSFSyncOn(Boolean isFullSFSyncOn) {
        this.isFullSFSyncOn = isFullSFSyncOn;
        return this;
    }

    public void setIsFullSFSyncOn(Boolean isFullSFSyncOn) {
        this.isFullSFSyncOn = isFullSFSyncOn;
    }

    public Boolean isIsSFSyncOn() {
        return isSFSyncOn;
    }

    public AdminMetadata isSFSyncOn(Boolean isSFSyncOn) {
        this.isSFSyncOn = isSFSyncOn;
        return this;
    }

    public void setIsSFSyncOn(Boolean isSFSyncOn) {
        this.isSFSyncOn = isSFSyncOn;
    }

    public Boolean isIsTPFSyncOn() {
        return isTPFSyncOn;
    }

    public AdminMetadata isTPFSyncOn(Boolean isTPFSyncOn) {
        this.isTPFSyncOn = isTPFSyncOn;
        return this;
    }

    public void setIsTPFSyncOn(Boolean isTPFSyncOn) {
        this.isTPFSyncOn = isTPFSyncOn;
    }

    public LocalDate getLastFullSFSync() {
        return lastFullSFSync;
    }

    public AdminMetadata lastFullSFSync(LocalDate lastFullSFSync) {
        this.lastFullSFSync = lastFullSFSync;
        return this;
    }

    public void setLastFullSFSync(LocalDate lastFullSFSync) {
        this.lastFullSFSync = lastFullSFSync;
    }

    public LocalDate getLastSFSync() {
        return lastSFSync;
    }

    public AdminMetadata lastSFSync(LocalDate lastSFSync) {
        this.lastSFSync = lastSFSync;
        return this;
    }

    public void setLastSFSync(LocalDate lastSFSync) {
        this.lastSFSync = lastSFSync;
    }

    public LocalDate getLastTPFSync() {
        return lastTPFSync;
    }

    public AdminMetadata lastTPFSync(LocalDate lastTPFSync) {
        this.lastTPFSync = lastTPFSync;
        return this;
    }

    public void setLastTPFSync(LocalDate lastTPFSync) {
        this.lastTPFSync = lastTPFSync;
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
        return "AdminMetadata{"
                + "id=" + getId()
                + ", isFullSFSyncOn='" + isIsFullSFSyncOn() + "'"
                + ", isSFSyncOn='" + isIsSFSyncOn() + "'"
                + ", isTPFSyncOn='" + isIsTPFSyncOn() + "'"
                + ", lastFullSFSync='" + getLastFullSFSync() + "'"
                + ", lastSFSync='" + getLastSFSync() + "'"
                + ", lastTPFSync='" + getLastTPFSync() + "'"
                + "}";
    }
}
