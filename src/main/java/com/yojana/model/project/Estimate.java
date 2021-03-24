package com.yojana.model.project;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;

@Entity
@Table(name = "Estimate")
@EntityListeners(AuditListener.class)
public class Estimate implements Auditable, Serializable {
    
    private static final long serialVersionUID = -8709713671106036600L;
    
    @Embedded
    private Audit audit;
    
    @Id
    @Column(name = "EstimateID", unique = true, columnDefinition = "uuid-char", updatable = false, insertable = false)
    @Type(type="uuid-char")
    private UUID estimateId;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JoinColumns({
        @JoinColumn(name = "WorkPackageID", referencedColumnName = "WorkPackageID", insertable = false, updatable = false),
        @JoinColumn(name = "ProjectID", referencedColumnName = "ProjectID", insertable = false, updatable = false)
    })
    private WorkPackage workPackage;
    
    @Column(name = "WorkPackageID", insertable = false, updatable = false)
    private String workPackageId;
    
    @Column(name = "ProjectID", insertable = false, updatable = false)
    private String projectId;
    
    @Column(name = "EstimateToComplete")
    private float estimateToComplete;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public UUID getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(UUID estimateId) {
        this.estimateId = estimateId;
    }

    public WorkPackage getWorkPackage() {
        return workPackage;
    }

    public void setWorkPackage(WorkPackage workPackage) {
        this.workPackage = workPackage;
    }

    public String getWorkPackageId() {
        return workPackageId;
    }

    public void setWorkPackageId(String workPackageId) {
        this.workPackageId = workPackageId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public float getEstimateToComplete() {
        return estimateToComplete;
    }

    public void setEstimateToComplete(float estimateToComplete) {
        this.estimateToComplete = estimateToComplete;
    }
    
    
}
