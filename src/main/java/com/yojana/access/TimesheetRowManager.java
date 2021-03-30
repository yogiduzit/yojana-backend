package com.yojana.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.yojana.model.employee.Employee;
import com.yojana.model.timesheet.Timesheet;
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
	public TimesheetRow find(UUID timesheetId, String projectId, String workPackageId) {
        return em.find(TimesheetRow.class, new TimesheetRowPK(timesheetId, projectId, workPackageId));
    }
	
	/** add a timesheetrow. */
	public void persist(TimesheetRow timesheetrow) {
		Query query = em.createNativeQuery("INSERT INTO TimesheetRow (Notes, Hours, ProjectID, TimesheetID, WorkPackageID) VALUES (?,?,?,?,?)");
		query.setParameter(1, timesheetrow.getNotes());
		query.setParameter(2, timesheetrow.getPackedHours());
		query.setParameter(3, timesheetrow.getProjectId());
		query.setParameter(4, timesheetrow.getTimesheetId().toString());
		query.setParameter(5, timesheetrow.getWorkPackageId());
        query.executeUpdate();
    } 
	
	/** update a timesheetrow. */
	public void merge(TimesheetRow timesheetrow) {
        em.merge(timesheetrow);
    }
	
	/** remove a timesheetrow. */
	public void remove(TimesheetRow timesheetrow, UUID timesheetId, String projectId, String workPackageId) {
        timesheetrow = find(timesheetId, projectId, workPackageId); 
        em.remove(timesheetrow);
    }
	
	public List<TimesheetRow> getAllForTimesheet(UUID timesheetId) {
	    TypedQuery<TimesheetRow> query = em.createQuery("select t from TimesheetRow t where t.timesheetId = :timesheetId", TimesheetRow.class);
        query.setParameter("timesheetId", timesheetId);
        return query.getResultList();
	}
	

}
