/**
 *
 */
package com.yojana.access;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import com.yojana.model.employee.Credential;
import com.yojana.model.employee.Employee;
import com.yojana.model.projectemployee.ProjectEmployee;

/**
 * This is the class called CredentialManager
 *
 * @author Yogesh Verma
 * @version 1.0
 *
 */
@Dependent
public class ProjectEmployeeManager implements Serializable {

    /**
     * Variable for implementing serializable
     */
    private static final long serialVersionUID = -6478292740340769939L;

    @PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

    public ProjectEmployeeManager() {}
    
    
    /*
     adds the model to the ProjectEmployee table
    */
	@Transactional
	public ProjectEmployee persist(ProjectEmployee model) {
        em.persist(model);
        return model;
    }
	
	
	public List<ProjectEmployee> findAllEmployeesForProject(String id) {
		TypedQuery<ProjectEmployee> query = em.createQuery("Select p from ProjectEmployee p Where p.project.id = :projectId", ProjectEmployee.class);
		query.setParameter("projectId", id);
		
		return query.getResultList();
	}
	
}