package com.yojana.access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.yojana.model.employee.PayGrade;

@Dependent
@Stateless
public class PayGradeManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
    
    public PayGrade find(String labourGrade) {
        return em.find(PayGrade.class, labourGrade);
    }
    
    public void persist(PayGrade payGrade) {
        em.persist(payGrade);
    }
    
    public void merge(PayGrade payGrade) {
        em.merge(payGrade);
    }
    
    public void remove(PayGrade payGrade, String labourGrade) {
        payGrade = find(labourGrade);
        em.remove(payGrade);
    }
    
    public List<PayGrade> getAll() {
        TypedQuery<PayGrade> query = em.createQuery("select p from PayGrade p",
                PayGrade.class);
        List<PayGrade> payGrades = query.getResultList();
        return payGrades;
    }
}
