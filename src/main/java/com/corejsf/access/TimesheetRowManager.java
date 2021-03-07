package com.corejsf.access;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.activation.DataSource;
import javax.annotation.Resource;
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
	
	@Resource(mappedName = "java:jboss/datasources/MySQLDS")
	private DataSource dataSource;
	
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
		ArrayList<TimesheetRow> timesheetrows = new ArrayList<TimesheetRow>();
		try (Connection connection = ((Statement) dataSource).getConnection();
		Statement stmt = connection.createStatement();
		PreparedStatement pstmt = connection.prepareStatement("select t from TimesheetRow t where TimesheetID = ? ")) {
			pstmt.setInt(1, timesheetId);
			ResultSet results = pstmt.executeQuery();
			while (results.next()) {
				timesheetrows.add(new TimesheetRow(Integer.parseInt(results.getString("ProjectID"))
						, results.getString("WorkPackageID"), results.getString("Notes"), results.getFloat("Hours")));
			}
		} catch (SQLException ex) {
            System.out.println("Error in getAllForTimesheet");
            ex.printStackTrace();
            return null;
        }

        TimesheetRow[] suparray = new TimesheetRow[timesheetrows.size()];
        return timesheetrows.toArray(suparray);
	}
	

}
