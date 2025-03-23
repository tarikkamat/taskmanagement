package com.tarikkamat.taskmanagement.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityAudit extends BaseEntity {
    
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    private String deletedBy;
    
    @Version
    private Integer version;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "deleted_at")
    private Date deletedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntityAudit that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(createdBy, that.createdBy) &&
                Objects.equals(updatedBy, that.updatedBy) &&
                Objects.equals(version, that.version) &&
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, deletedBy, version, createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "BaseEntityAudit{" +
                "createdBy='" + (createdBy != null ? createdBy : "null") + '\'' +
                ", updatedBy='" + (updatedBy != null ? updatedBy : "null") + '\'' +
                ", deletedBy='" + (deletedBy != null ? deletedBy : "null") + '\'' +
                ", version=" + (version != null ? version : "null") +
                ", createdAt=" + (createdAt != null ? createdAt : "null") +
                ", updatedAt=" + (updatedAt != null ? updatedAt : "null") +
                ", deletedAt=" + (deletedAt != null ? deletedAt : "null") +
                "}" +
                super.toString();
    }
}