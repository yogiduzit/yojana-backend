package com.yojana.access;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;
import javax.persistence.EntityManager;

import com.yojana.model.timesheet.Timesheet;
import com.yojana.model.project.WorkPackage;
import com.yojana.model.project.WorkPackagePK;

@Dependent
@Stateless
public class TimesheetManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	@Inject
    private WorkPackageManager wpManager;
	
	public TimesheetManager() {}
	
	/** find a timesheet with id. */
	public Timesheet find(@PathParam("id") UUID id) {
        return em.find(Timesheet.class, id);
    }
	
	/** add a timesheet. */
	public void persist(Timesheet timesheet) {
        em.persist(timesheet);
    } 
	
	/** update a timesheet. */
	public void merge(Timesheet timesheet) {
        em.merge(timesheet);
    }
	
	public void remove(UUID id) {
        Timesheet timesheet = find(id); 
        em.remove(timesheet);
    }
	
	/** get all timesheets. */
	public List<Timesheet> getAll() {       
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t", Timesheet.class); 
        return query.getResultList();
    }
	
	/** get all timesheets. */
	public List<Timesheet> getAllForEmployee(Integer empId) {       
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where EmpID = :empId", Timesheet.class);
		query.setParameter("empId", empId);
        return query.getResultList();
    }
	
	/** get all submitted timesheets. **/
	public List<Timesheet> getAllSubmittedTimesheets() {
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where Status = 'submitted'", Timesheet.class);
		return query.getResultList();
	}

	/** get all timesheets. */
	public List<Timesheet> getTimesheetsForWorkPackage(WorkPackagePK key) {
        WorkPackage wp = wpManager.find(key);
		int empID = wp.getResponsibleEngineer().getId();
		return getAllForEmployee(empID);
	}

	/** get all submitted timesheets. **/
	public List<Timesheet> getAllSubmittedTimesheetsForApprover(Integer empId) {
		TypedQuery<Timesheet> query = em.createQuery(
				"SELECT t FROM Timesheet t JOIN t.employee e WHERE e.timesheetApproverId = :empId AND t.status = 'submitted'",
				Timesheet.class);
		query.setParameter("empId", empId);
		return query.getResultList();
	}
}
