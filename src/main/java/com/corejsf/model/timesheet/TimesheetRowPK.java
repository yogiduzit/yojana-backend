package com.corejsf.model.timesheet;

import java.io.Serializable;
import javax.persistence.IdClass;
 
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
    private int timesheetId;
    
    /**Work Package ID */
    private String workPackageId;
    
    /**Project ID */
    private int projectId;

    //getters and setters--------------------------
    
    /**@return Timesheet ID. */
    public int getTimesheetId() {
        return timesheetId;
    }
    /**@param timesheetId = the timesheet Id needed to be set. */
    public void setTimesheetId(int timesheetId) {
        this.timesheetId = timesheetId;
    }
    /**@return work package ID. */
    public String getWorkPackageId() {
        return workPackageId;
    }
    /**@param workPackageid is the work package Id to be set. */
    public void setWorkPackageId(String workPackageId) {
        this.workPackageId = workPackageId;
    }
    /**@return project ID. */
    public int getProjectId() {
        return projectId;
    }
    /**@param projectId is the project Id to be set. */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    
    //---------------------------------

    //Overriding implementations--------
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + projectId;
        result = prime * result + timesheetId;
        result = prime * result + ((workPackageId == null) ? 0 : workPackageId.hashCode());
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
        if (projectId != other.projectId)
            return false;
        if (timesheetId != other.timesheetId)
            return false;
        if (workPackageId == null) {
            if (other.workPackageId != null)
                return false;
        } else if (!workPackageId.equals(other.workPackageId))
            return false;
        return true;
    }
    //-----------------------------------
    
}
