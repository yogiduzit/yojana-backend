package com.yojana.model.estimate;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yojana.model.employee.PayGrade;

@Entity
@Table(name = "EstimateRow")
public class EstimateRow implements Serializable {
    
    private static final long serialVersionUID = -8709713671106036600L;
    
    @EmbeddedId
    private EstimateRowPK estimateRowPk;
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE})
    @JoinColumn(name = "EstimateID", referencedColumnName = "EstimateID", insertable = false, updatable = false)
    private Estimate estimate;
    
    @ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.MERGE})
    @JoinColumn(name = "PayGradeID", referencedColumnName = "LabourGrade")
    private PayGrade payGrade;
    
    @Column(name = "PayGradeID", insertable = false, updatable = false)
    private String paygradeId;
    
    @Column(name = "EmpDays")
    private float empDays;
    
    @Column(name = "EmpCount")
    private int empCount;

    public EstimateRowPK getEstimateRowPk() {
        return estimateRowPk;
    }

    public void setEstimateRowPk(EstimateRowPK estimateRowPk) {
        this.estimateRowPk = estimateRowPk;
    }

    public Estimate getEstimate() {
        return estimate;
    }

    public void setEstimate(Estimate estimate) {
        this.estimate = estimate;
    }

    public PayGrade getPayGrade() {
        return payGrade;
    }

    public void setPayGrade(PayGrade payGrade) {
        this.payGrade = payGrade;
    }

    public float getEmpDays() {
        return empDays;
    }

    public void setEmpDays(float empDays) {
        this.empDays = empDays;
    }

    public int getEmpCount() {
        return empCount;
    }

    public void setEmpCount(int empCount) {
        this.empCount = empCount;
    }

	public String getPaygradeId() {
		return paygradeId;
	}

	public void setPaygradeId(String paygradeId) {
		this.paygradeId = paygradeId;
	}
	
	public float getTotalCost() {
		return empDays * 8 * payGrade.getChargeRate() * empCount;
	}
    
}
