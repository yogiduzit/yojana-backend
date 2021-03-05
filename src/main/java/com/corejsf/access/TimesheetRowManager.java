package com.corejsf.access;

import java.io.Serializable;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.corejsf.model.timesheet.TimesheetRow;
import com.corejsf.model.timesheet.TimesheetRowPK;


@Dependent
@Stateless
public class TimesheetRowManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext(unitName="comp4911-pms-rest-jpa") EntityManager em;
	
	public TimesheetRowManager() {}
	
	/** find a timesheetrow with id. */
	public TimesheetRow find(int timesheetId, int projectId, String workPackageId) {
        return em.find(TimesheetRow.class, new TimesheetRowPK(timesheetId, projectId, workPackageId));
    }
	
	/** add a timesheetrow. */
	public void persist(TimesheetRow timesheetrow) {
        em.persist(timesheetrow);
    } 
	
	/** update a timesheetrow. */
	public void merge(TimesheetRow timesheetrow) {
        em.merge(timesheetrow);
    }
	
	/** remove a timesheetrow. */
	public void remove(TimesheetRow timesheetrow, int timesheetId, int projectId, String workPackageId) {
        timesheetrow = find(timesheetId, projectId, workPackageId); 
        em.remove(timesheetrow);
    }
	
	/** get all timesheetrows. */
	public TimesheetRow[] getAll() {       
		TypedQuery<TimesheetRow> query = em.createQuery("select tsr from TimesheetRow tsr", TimesheetRow.class); 
        java.util.List<TimesheetRow> timesheetrows = query.getResultList();
        TimesheetRow[] suparray = new TimesheetRow[timesheetrows.size()];
        for (int i=0; i < suparray.length; i++) {
            suparray[i] = timesheetrows.get(i);
        }
        return suparray;
    }
	
	public TimesheetRow[] getAllForTimesheet(int timesheetId) {
		TypedQuery<TimesheetRow> query = em.createQuery("select t from TimesheetRow t where TimesheetID = " + timesheetId, TimesheetRow.class); 
        java.util.List<TimesheetRow> timesheetrows = query.getResultList();
        TimesheetRow[] suparray = new TimesheetRow[timesheetrows.size()];
        for (int i=0; i < suparray.length; i++) {
            suparray[i] = timesheetrows.get(i);
        }
        return suparray;
	}
	

}
