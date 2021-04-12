package com.yojana.access;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;
import javax.persistence.EntityManager;

import com.yojana.model.timesheet.Timesheet;

@Dependent
@Stateless
public class TimesheetManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

	
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
//		Query query = em.createNativeQuery("UPDATE Timesheet SET"
//				+ "EndWeek = ?1, Overtime = ?2, Flextime = ?3, Status = ?4, ReviewedBy = ?5, Signature = ?6,"
//				+ "Feedback = ?7, UpdatedAt = ?8, ApprovedAt = ?9"
//				+ "WHERE TimesheetID = ?10");
//		query.setParameter(1, timesheet.getEndWeek());
//		query.setParameter(2, timesheet.getOvertime());
//		query.setParameter(3, timesheet.getFlextime());
//		query.setParameter(4, timesheet.getStatus());
//		query.setParameter(5, timesheet.getReviewerId());
//		query.setParameter(6, timesheet.getSignature());
//		query.setParameter(7, timesheet.getFeedback());
//		query.setParameter(8, timesheet.getAudit().getUpdatedAt());
//		query.setParameter(9, timesheet.getApprovedAt());
//		query.setParameter(10, timesheet.getId());
//		query.executeUpdate();
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
	public List<Timesheet> getAllForEmployee(UUID empId) {       
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where EmpID = :empId", Timesheet.class);
		query.setParameter("empId", empId);
        return query.getResultList();
    }
	
	/** get all submitted timesheets. **/
	public List<Timesheet> getAllSubmittedTimesheets() {
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where Status = 'submitted'", Timesheet.class);
		return query.getResultList();
	}
}
