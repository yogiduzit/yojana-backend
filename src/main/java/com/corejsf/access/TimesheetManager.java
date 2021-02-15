package com.corejsf.access;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.PathParam;
import javax.persistence.EntityManager;

import com.corejsf.model.timesheet.Timesheet;

@Dependent
@Stateless
public class TimesheetManager implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;

	
	public TimesheetManager() {}
	
	/** find a timesheet with id. */
	public Timesheet find(@PathParam("id") String id) {
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
	
	/** remove a timesheets. */
	public void remove(Timesheet timesheet, String id) {
        timesheet = find(id); 
        em.remove(timesheet);
    }
	
	/** get all timesheets. */
	public Timesheet[] getAll() {       
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t", Timesheet.class); 
        java.util.List<Timesheet> timesheets = query.getResultList();
        Timesheet[] suparray = new Timesheet[timesheets.size()];
        for (int i=0; i < suparray.length; i++) {
            suparray[i] = timesheets.get(i);
        }
        return suparray;
    }
	
	/** get all timesheets. */
	public Timesheet[] getAllForEmployee(String empId) {       
		TypedQuery<Timesheet> query = em.createQuery("select t from Timesheet t where EmpID = " + empId, Timesheet.class); 
        java.util.List<Timesheet> timesheets = query.getResultList();
        Timesheet[] suparray = new Timesheet[timesheets.size()];
        for (int i=0; i < suparray.length; i++) {
            suparray[i] = timesheets.get(i);
        }
        return suparray;
    }
}
