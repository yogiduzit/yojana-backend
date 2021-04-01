package com.yojana.access;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.yojana.model.timesheet.TimesheetRow;
import com.yojana.model.timesheet.TimesheetRowPK;

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
	public TimesheetRow find(UUID timesheetId, int index) {
        return em.find(TimesheetRow.class, new TimesheetRowPK(timesheetId, index));
    }
	
	/** add a timesheetrow. */
	public void persist(TimesheetRow timesheetrow) {
	    System.out.println("heyo wutup");
	    em.persist(timesheetrow);
    } 
	
	/** update a timesheetrow. */
	public void merge(TimesheetRow timesheetrow) {
        em.merge(timesheetrow);
    }
	
	/** remove a timesheetrow. */
	public void remove(TimesheetRow timesheetrow, UUID timesheetId, int rowIndex) {
        timesheetrow = find(timesheetId, rowIndex); 
        em.remove(timesheetrow);
    }
	
	public List<TimesheetRow> getAllForTimesheet(UUID timesheetId) {
	    TypedQuery<TimesheetRow> query = em.createQuery("select t from TimesheetRow t where t.id.timesheetId = :timesheetId", TimesheetRow.class);
        query.setParameter("timesheetId", timesheetId);
        return query.getResultList();
	}
}
