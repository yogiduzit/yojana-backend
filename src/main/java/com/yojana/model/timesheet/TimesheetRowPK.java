package com.yojana.model.timesheet;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import org.hibernate.annotations.Type;

 
/**External class containing PK columns of TimesheetRow. 
 * A class representing the Composite primary key of a Timesheet Row.
 * PK/FK = timesheetID, workpackageId, projectId.
 * 
 * @author Abeer Haroon
 * @version 1.0
 * */
public class TimesheetRowPK implements Serializable {
    
    //class members
    private static final long serialVersionUID = 1L;

    //instance members
    
    /**Timesheet ID. */
    @Column(name = "TimesheetID",columnDefinition = "varchar", updatable = false, insertable = false)
    @Type(type="uuid-char")
    private UUID timesheetId;
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "RowIndex")
    private int index;

	public UUID getTimesheetId() {
		return timesheetId;
	}
	
	public TimesheetRowPK() {
	}

	public TimesheetRowPK(UUID timesheetId, int index) {
		super();
		this.timesheetId = timesheetId;
		this.index = index;
	}

	public void setTimesheetId(UUID timesheetId) {
		this.timesheetId = timesheetId;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		result = prime * result + ((timesheetId == null) ? 0 : timesheetId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimesheetRowPK other = (TimesheetRowPK) obj;
		if (index != other.index)
			return false;
		if (timesheetId == null) {
			if (other.timesheetId != null)
				return false;
		} else if (!timesheetId.equals(other.timesheetId))
			return false;
		return true;
	}
}
