package com.yojana.access;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.yojana.model.estimate.EstimateRow;
import com.yojana.model.estimate.EstimateRowPK;

@Dependent
@Stateless
public class EstimateRowManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
    
    public EstimateRow find(EstimateRowPK key) {
        return em.find(EstimateRow.class, key);
    }
    
    @Transactional
    public void persist(EstimateRow estimateRow) {
        em.persist(estimateRow);
    }
    
    @Transactional
    public void merge(EstimateRow estimateRow) {
        em.merge(estimateRow);
    }
    
    @Transactional
    public void remove(EstimateRowPK key) {
        EstimateRow temp = find(key);
        em.remove(temp);
    }
    
    public List<EstimateRow> getAll() {
        TypedQuery<EstimateRow> query = em.createQuery("select e from EstimateRow",
                EstimateRow.class);  
        List<EstimateRow> estimateRows = query.getResultList();
        return estimateRows;
    }
    
    public List<EstimateRow> getAllForEstimate(UUID estimateId) {
        TypedQuery<EstimateRow> query = em.createQuery("select e from EstimateRow e"
                + " where e.estimateRowPk.estimateId = :estimateId",
                EstimateRow.class);  
        List<EstimateRow> estimateRows = query.getResultList();
        return estimateRows;
    }
}
