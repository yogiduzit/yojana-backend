package com.corejsf.model.employee;

import javax.persistence.*;

import com.corejsf.model.auditable.Audit;

/**
 * Entity implementation class for Entity: PayGrade
 *
 */
@Entity
@Table(name = "PayGrade")

public class PayGrade {
    
    @Embedded
    private Audit audit;
    
    @Id
    @Column(name = "LabourGrade")
    private String labourGrade;
    
    @Column(name = "ChargeRate")
    private float chargeRate;

    public Audit getAudit() {
        return audit;
    }

    public void setAudit(Audit audit) {
        this.audit = audit;
    }

    public String getLabourGrade() {
        return labourGrade;
    }

    public void setLabourGrade(String labourGrade) {
        this.labourGrade = labourGrade;
    }

    public float getChargeRate() {
        return chargeRate;
    }

    public void setChargeRate(float chargeRate) {
        this.chargeRate = chargeRate;
    }
   
}
