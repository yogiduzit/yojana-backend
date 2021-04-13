package com.yojana.access;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.yojana.model.estimate.Estimate;
import com.yojana.model.estimate.EstimateRow;
import com.yojana.model.estimate.EstimateRowPK;

@Dependent
@Stateless
public class EstimateRowManager implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
    
    @Inject
    private EstimateManager estimateManager;
    
    public EstimateRow find(EstimateRowPK key) {
        return em.find(EstimateRow.class, key);
    }
    
    @Transactional
    public void persist(EstimateRow estimateRow) {
    	Estimate estimate = estimateManager.find(
    			estimateRow.getEstimateRowPk().getEstimateId()
    	);
    	estimate.setEstimateToComplete(
    			estimate.getEstimateToComplete() + estimateRow.getTotalCost()
    	);
    	estimateManager.merge(estimate);
        em.persist(estimateRow);
    }
    
    @Transactional
    public void merge(EstimateRow estimateRow) {
    	EstimateRow old = find(estimateRow.getEstimateRowPk());
    	Estimate estimate = estimateManager.find(
    			estimateRow.getEstimateRowPk().getEstimateId()
    	);
    	estimate.setEstimateToComplete(
    			estimate.getEstimateToComplete() - old.getTotalCost() + estimateRow.getTotalCost()
    	);
    	estimateManager.merge(estimate);
        em.merge(estimateRow);
    }
    
    @Transactional
    public void remove(EstimateRowPK key) {
        EstimateRow temp = find(key);
    	Estimate estimate = estimateManager.find(
    			temp.getEstimateRowPk().getEstimateId()
    	);
    	estimate.setEstimateToComplete(
    			estimate.getEstimateToComplete() - temp.getTotalCost()
    	);
    	estimateManager.merge(estimate);
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
        query.setParameter("estimateId", estimateId);
        List<EstimateRow> estimateRows = query.getResultList();
        return estimateRows;
    }
}
