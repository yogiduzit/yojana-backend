DROP DATABASE IF EXISTS project_management_system;
CREATE DATABASE project_management_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON project_management_system.* TO 'admin'@'localhost';
GRANT ALL ON project_management_system.* TO 'admin'@'%';

USE project_management_system;
DROP TABLE IF EXISTS PayGrade;
CREATE TABLE PayGrade(
	LabourGrade VARCHAR(4),
    ChargeRate FLOAT(5,2),
    PRIMARY KEY(LabourGrade)
);

DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee(
    EmpID INT NOT NULL UNIQUE AUTO_INCREMENT,
    EmpName VARCHAR(50) NOT NULL,
    LabourGrade VARCHAR(4),
    CreatedBy VARCHAR(255),
    ManagedBy VARCHAR(255),
    TimesheetApproverID VARCHAR(255),
    ProfileImage TINYTEXT,
    IsHR BOOLEAN,
    IsAdmin BOOLEAN,
    IsProjectManager BOOLEAN,
    IsTimesheetApprover BOOLEAN,
    CONSTRAINT PKEmployee PRIMARY KEY (EmpID),
    CONSTRAINT FKEmployeeLabourGrade
		FOREIGN KEY (LabourGrade)
			REFERENCES PayGrade(LabourGrade),
	CONSTRAINT FKEmployeeCreatedBy
		FOREIGN KEY (CreatedBy)
			REFERENCES Employee(EmpID),
	CONSTRAINT FKEmployeeManagedBy
		FOREIGN KEY (ManagedBy)
			REFERENCES Employee(EmpID),
    CONSTRAINT FKEmployeeTimesheetApproverID
		FOREIGN KEY (TimesheetApproverID)
			REFERENCES Employee(EmpID)
);

DROP TABLE IF EXISTS Credential;
CREATE TABLE Credential(
    EmpID INT NOT NULL UNIQUE,
	EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    EmpPassword VARCHAR(15) NOT NULL,
	CONSTRAINT PKCredentialEmp PRIMARY KEY (EmpID),
    CONSTRAINT FKCredentialEmpID
        FOREIGN KEY (EmpID)
            REFERENCES Employee(EmpID)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS Timesheet;
CREATE TABLE Timesheet(
	TimesheetID VARCHAR(255) NOT NULL UNIQUE,
    EmpID INT NOT NULL,
	EndWeek DATE NOT NULL,
    Overtime INT,
    Flextime INT,
	Status ENUM('pending', 'submitted', 'approved', 'denied') NOT NULL DEFAULT 'pending',
    ReviewedBy INT,
    Signature TINYTEXT,
    Feedback TINYTEXT,
	CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ApprovedAt TIMESTAMP,
	CONSTRAINT PKTimesheet
		PRIMARY KEY (TimesheetID),
	CONSTRAINT FKTimesheetRevieweer 
		FOREIGN KEY (ReviewedBy) 
			REFERENCES Employee(EmpID),
    CONSTRAINT FKTimesheetCreator 
    	FOREIGN KEY (EmpID) 
    		REFERENCES Employee(EmpID)
			ON UPDATE CASCADE
        	ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRow;
CREATE TABLE TimesheetRow(
	TimesheetID VARCHAR(255) NOT NULL,
	ProjectID VARCHAR(20),
	WorkPackageID VARCHAR(20),
	Notes TINYTEXT,
	Hours FLOAT,
	CONSTRAINT PKTimesheetRow
		PRIMARY KEY(ProjectID, WorkPackageID, TimesheetID),
	CONSTRAINT FKTimesheetRow_Timesheet 
		FOREIGN KEY (TimesheetID) REFERENCES Timesheet(TimesheetID)
			ON UPDATE CASCADE
        	ON DELETE CASCADE
);

DROP TABLE IF EXISTS LeaveRequest;
CREATE TABLE LeaveRequest(
	LeaveRequestID VARCHAR(255) NOT NULL UNIQUE,
    EmpID INT NOT NULL,
    StartDate DATE,
    EndDate DATE,
    Type VARCHAR(125),
    Description VARCHAR(255),
	CONSTRAINT PKLeaveRequest PRIMARY KEY (LeaveRequestID),
    CONSTRAINT FKRequestLeaveEmpID
        FOREIGN KEY (EmpID)
            REFERENCES Employee(EmpID)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS Project;
CREATE TABLE Project(
	ProjectID VARCHAR(255) NOT NULL,
	ProjectManagerID INT NOT NULL,
    ProjectName VARCHAR(100),
    Budget FLOAT(14,2),
    InitialEstimate FLOAT(14,2),
    Descrip VARCHAR(255),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Stat ENUM('pending', 'submitted', 'open', 'closed') NOT NULL DEFAULT 'pending',
	CONSTRAINT PKProject
		PRIMARY KEY(ProjectID),
	CONSTRAINT FKProjectProjectManagerID 
		FOREIGN KEY (ProjectManagerID) REFERENCES Employee(EmpID)
			ON UPDATE CASCADE
);

DROP TABLE IF EXISTS ProjectEmployee;
CREATE TABLE ProjectEmployee(
	ProjectID VARCHAR(255) NOT NULL,
	EmpID INT,
	CONSTRAINT PKProjectEmployee
		PRIMARY KEY(ProjectID, EmpID),
	CONSTRAINT FKProjectEmployeeProjectID 
		FOREIGN KEY (ProjectID) REFERENCES Project(ProjectID)
			ON UPDATE CASCADE
        	ON DELETE CASCADE,
	CONSTRAINT FKProjectEmployeeEmpID 
		FOREIGN KEY (EmpID) REFERENCES Employee(EmpID)
			ON UPDATE CASCADE
        	ON DELETE CASCADE
);

DROP TABLE IF EXISTS Report;
CREATE TABLE Report(
	ReportID VARCHAR(255) NOT NULL,
	ProjectID VARCHAR(20),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    Info JSON,
	CONSTRAINT PKReport
		PRIMARY KEY(ReportID),
	CONSTRAINT FKReportProjectID 
		FOREIGN KEY (ProjectID) REFERENCES Project(ProjectID)
			ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS WorkPackage;
CREATE TABLE WorkPackage(
	WorkPackageID VARCHAR(20) NOT NULL,
	ProjectID VARCHAR(20) NOT NULL,
    ParentWorkPackageID VARCHAR(20),
    ResponsibleEngineerID VARCHAR(255),
    WorkPackageName VARCHAR(100),
    Descrip VARCHAR(255),
    IsLowestLevel BOOLEAN,
    AllocatedBudget FLOAT(14,2),
    InitialEstimate FLOAT(14,2),
    Charged FLOAT(14,2),
    CostAtCompletion FLOAT(14,2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    DueAt DATE,
    Stat ENUM('complete', 'open', 'closed') NOT NULL DEFAULT 'open',
	CONSTRAINT PKWorkPackage
		PRIMARY KEY(WorkPackageID, ProjectID),
	CONSTRAINT FKWorkPackageProjectID 
		FOREIGN KEY (ProjectID) REFERENCES Project(ProjectID)
			ON UPDATE CASCADE
            ON DELETE CASCADE,
	CONSTRAINT FKWorkPackageParentWorkPackageID 
		FOREIGN KEY (ParentWorkPackageID, ProjectID) REFERENCES WorkPackage(WorkPackageID, ProjectID)
			ON UPDATE CASCADE
            ON DELETE CASCADE,
	CONSTRAINT FKWorkPackageResponsibleEngineerID
		FOREIGN KEY (ResponsibleEngineerID) REFERENCES Employee(EmpID)
			ON UPDATE CASCADE         
);

DROP TABLE IF EXISTS EmployeePackages;
CREATE TABLE EmployeePackages(
	WorkPackageID VARCHAR(20) NOT NULL,
	ProjectID VARCHAR(20) NOT NULL,
    EmpID VARCHAR(255) NOT NULL,
	CONSTRAINT PKEmployeePackages
		PRIMARY KEY(WorkPackageID, ProjectID, EmpID),
	CONSTRAINT FKEmployeePackagesWorkPackageIDProjectID
		FOREIGN KEY (WorkPackageID, ProjectID) REFERENCES WorkPackage(WorkPackageID, ProjectID)
			ON UPDATE CASCADE
            ON DELETE CASCADE,
	CONSTRAINT FKEmployeePackagesEmpID
		FOREIGN KEY (EmpID) REFERENCES Employee(EmpID)
			ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS Estimate;
CREATE TABLE Estimate(
	EstimateID VARCHAR(255) NOT NULL,
    WorkPackageID VARCHAR(20) NOT NULL,
	ProjectID VARCHAR(20) NOT NULL,
    EstimateToComplete FLOAT(14,2),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PKEstimate
		PRIMARY KEY(EstimateID),
	CONSTRAINT FKEstimateWorkPackageIDProjectID 
		FOREIGN KEY (WorkPackageID, ProjectID) REFERENCES WorkPackage(WorkPackageID, ProjectID)
			ON UPDATE CASCADE
            ON DELETE CASCADE
);



INSERT INTO PayGrade (LabourGrade, ChargeRate) VALUES ("PS", 3.50);

INSERT INTO Employee (EmpID, EmpName, LabourGrade) VALUES (1, "Bruce Link",  "PS");
INSERT INTO Employee (EmpID, EmpName, LabourGrade) VALUES (2, "Yogesh Verma",  "PS");

INSERT INTO Credential (EmpID, EmpUserName, EmpPassword) VALUES (1, "bdlink", "bruce");
INSERT INTO Credential (EmpID, EmpUserName, EmpPassword) VALUES (2, "yogiduzit", "yogesh");

INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("55000000-0000-0000-0000-000000000000", 1, DATE '2000/3/10');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("26000000-0000-0000-0000-000000000000", 1, DATE '2000/3/17');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("45700000-0000-0000-0000-000000000000", 2, DATE '2000/3/24');
