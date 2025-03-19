package com.tarikkamat.taskmanagement.common;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntityAudit extends BaseEntity {
    private String createdBy;
    private String updatedBy;
    private String deletedBy;

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
                Objects.equals(createdAt, that.createdAt) &&
                Objects.equals(updatedAt, that.updatedAt) &&
                Objects.equals(deletedBy, that.deletedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdBy, updatedBy, deletedBy, createdAt, updatedAt, deletedAt);
    }

    @Override
    public String toString() {
        return "BaseEntityAudit{" +
                "createdBy='" + (createdBy != null ? createdBy : "null") + '\'' +
                ", updatedBy='" + (updatedBy != null ? updatedBy : "null") + '\'' +
                ", deletedBy='" + (deletedBy != null ? deletedBy : "null") + '\'' +
                ", createdAt=" + (createdAt != null ? createdAt : "null") +
                ", updatedAt=" + (updatedAt != null ? updatedAt : "null") +
                ", deletedAt=" + (deletedAt != null ? deletedAt : "null") +
                "}" +
                super.toString();
    }
}