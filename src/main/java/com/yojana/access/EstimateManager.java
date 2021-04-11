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

import com.yojana.model.estimate.Estimate;

@Dependent
@Stateless
public class EstimateManager implements Serializable{
    
    private static final long serialVersionUID = -8709713671106036600L;
    
    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
    
    /**
     * finds an estimate with id.
     * @param id
     * @return estimate with associated id
     */
    public Estimate find(UUID id) {
        return em.find(Estimate.class, id);
    }
    
    /**
     * adds an estimate
     * @param estimate
     */
    @Transactional
    public void persist(Estimate estimate) {
        em.persist(estimate);
    }
    
    /**
     * updates an estimate
     * @param estimate
     */
    @Transactional
    public void merge(Estimate estimate) {
        em.merge(estimate);
    }
    
    /**
     * removes an estimate
     * @param estimate
     * @param id
     */
    @Transactional
    public void remove(UUID id) {
        Estimate estimate = find(id);
        em.remove(estimate);
    }
    
    /**
     * gets all estimates
     * @return a list of estimates
     */
    public List<Estimate> getAll() {
        TypedQuery<Estimate> query = em.createQuery("select e from Estimate e",
                Estimate.class); 
        List<Estimate> estimates = query.getResultList();
        return estimates;
    }
    
    /**
     * get all estimates for a work package
     * @param workPackageId
     * @param projectId
     * @return a list of estimates of that work package
     */
    public List<Estimate> getAllForWorkPackage(String workPackageId, String projectId) {
        TypedQuery<Estimate> query = em.createQuery("select e from Estimate e where"
                + " e.workPackageId = :workPackageId"
                + " and e.projectId = :projectId",
                Estimate.class); 
        query.setParameter("workPackageId", workPackageId);
        query.setParameter("projectId", projectId);
        List<Estimate> estimates = query.getResultList();
        return estimates;
    }
    
    /**
     * get all estimates for a project
     * @param projectId
     * @return a list of estimates of that project
     */
    public List<Estimate> getAllForProject(String projectId) {
        TypedQuery<Estimate> query = em.createQuery("select e from Estimate e where"
                + " e.projectId = :projectId",
                Estimate.class); 
        List<Estimate> estimates = query.getResultList();
        return estimates;
    }
}
