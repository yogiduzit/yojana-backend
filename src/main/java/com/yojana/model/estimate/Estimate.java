package com.yojana.model.estimate;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yojana.model.auditable.Audit;
import com.yojana.model.auditable.AuditListener;
import com.yojana.model.auditable.Auditable;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.timesheet.LocalDateDeserializer;
import com.yojana.model.timesheet.LocalDateSerializer;

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private WorkPackage workPackage;
    
    @Column(name = "WorkPackageID", insertable = false, updatable = false)
    private String workPackageId;
    
    @Column(name = "ProjectID", insertable = false, updatable = false)
    private String projectId;
    
    @Column(name = "EstimateToComplete")
    private float estimateToComplete;
    
    @JsonDeserialize(using = LocalDateDeserializer.class)  
	@JsonSerialize(using = LocalDateSerializer.class)  
	@Column(name = "ForWeek")
    private LocalDate forWeek;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "Type", columnDefinition = "enum")
    private EstimateType type;
    
//    @Column(name = "CreatedAt", insertable = false, updatable = false)
//    private Timestamp createdAt;
//    
//    @Column(name = "UpdatedAt", insertable = false, updatable = false)
//    private Timestamp updatedAt;
    
    @OneToMany(mappedBy = "estimate", fetch = FetchType.EAGER)
    private Set<EstimateRow> rows;

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

	public LocalDate getForWeek() {
		return forWeek;
	}

	public void setForWeek(LocalDate forWeek) {
		this.forWeek = forWeek;
	}

	public EstimateType getType() {
		return type;
	}

	public void setType(EstimateType type) {
		this.type = type;
	}

	public Set<EstimateRow> getRows() {
		return rows;
	}

	public void setRows(Set<EstimateRow> rows) {
		this.rows = rows;
	}

//	public Timestamp getCreatedAt() {
//		return createdAt;
//	}
//
//	public void setCreatedAt(Timestamp createdAt) {
//		this.createdAt = createdAt;
//	}
//
//	public Timestamp getUpdatedAt() {
//		return updatedAt;
//	}
//
//	public void setUpdatedAt(Timestamp updatedAt) {
//		this.updatedAt = updatedAt;
//	}

	

	
	
}
