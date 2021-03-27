package com.yojana.model.employee;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: PayGrade
 *
 */
@Entity
@Table(name = "PayGrade")
public class PayGrade {
    
    @Id
    @Column(name = "LabourGrade")
    private String labourGrade;
    
    @Column(name = "ChargeRate")
    private float chargeRate;

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
