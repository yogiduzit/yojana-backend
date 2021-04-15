package com.yojana.access;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.yojana.model.project.Project;

@Dependent
@Stateless
public class ProjectManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8709713671106036600L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	
	/** find an employee with id. */
	public Project find(String id) {
        return em.find(Project.class, id);
    }
	
	/** add an employee. */
	@Transactional
	public void persist(Project project) {
        em.persist(project);
    }
	
	/** update an employee. */
	@Transactional
	public void merge(Project project) {
        em.merge(project);
    }
	
	/** remove an employee. */
	@Transactional
	public void remove(Project project, String id) {
        project = find(id);
        em.remove(project);
    }
	
	public List<Project> getAll() {       
		TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p",
                Project.class); 
        List<Project> projects = query.getResultList();
        return projects;
    }
	
	public List<Project> getAllForEmployee(int empId) {       
        TypedQuery<Project> query = em.createQuery("SELECT DISTINCT p FROM Project p JOIN FETCH" 
                + " p.employees e where e.id = :empId", Project.class); 
        query.setParameter("empId", empId);
        List<Project> projects = query.getResultList();
        return projects;
    }
	
	public List<Project> getAllForProjectManager(int empId) {       
		TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p WHERE p.projectManagerId = :empId", Project.class); 
		query.setParameter("empId", empId);
        List<Project> projects = query.getResultList();
        return projects;
    }
	
	public Double getAllocatedInitialEstimate(String projectId) {       
		Query query = em.createQuery("SELECT SUM(wp.initialEstimate) FROM WorkPackage wp" 
				+ " WHERE wp.workPackagePk.projectID = :projectId"
				+ " AND wp.hierarchyLevel = 0"); 
		query.setParameter("projectId", projectId);
        
        return (Double) query.getSingleResult();
    }
	
	public Double getAllocatedBudget(String projectId) {       
		Query query = em.createQuery("SELECT SUM(wp.budget) FROM WorkPackage wp" 
				+ " WHERE wp.workPackagePk.projectID = :projectId"
				+ " AND wp.hierarchyLevel = 0"); 
		query.setParameter("projectId", projectId);
        
        return (Double) query.getSingleResult();
    }
}
