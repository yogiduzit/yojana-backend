package com.yojana.model.project;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

@Embeddable
public class EstimateRowPK implements Serializable {

    private static final long serialVersionUID = -8709713671106036600L;
    
    @Column(name="TimesheetID", nullable = false, insertable = false, updatable = false)
    @Type(type="uuid-char")
    private UUID estimateId;
    
    @Column(name = "PayGradeID", insertable = false, updatable = false)
    private String payGradeId;

    public UUID getEstimateId() {
        return estimateId;
    }

    public void setEstimateId(UUID estimateId) {
        this.estimateId = estimateId;
    }

    public String getPayGradeId() {
        return payGradeId;
    }

    public void setPayGradeId(String payGradeId) {
        this.payGradeId = payGradeId;
    }
    
    public EstimateRowPK() {
        
    }
    
    public EstimateRowPK(UUID estimateId, String payGradeId) {
        super();
        this.estimateId = estimateId;
        this.payGradeId = payGradeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((estimateId == null) ? 0 : estimateId.hashCode());
        result = prime * result + ((payGradeId == null) ? 0 : payGradeId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof EstimateRowPK) {
            EstimateRowPK temp = (EstimateRowPK) obj;
            if (!temp.getEstimateId().equals(this.getEstimateId())) {
                return false;
            }
            if (!temp.getPayGradeId().equals(this.getPayGradeId())) {
                return false;
            }
            return true;
        }
        return false;
    }
    
    
    
    
}
