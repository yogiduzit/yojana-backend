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
    EmpID VARCHAR(255) NOT NULL UNIQUE,
    EmpName VARCHAR(50) NOT NULL,
    LabourGrade VARCHAR(4),
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PKEmployee PRIMARY KEY (EmpID),
    CONSTRAINT FKEmployeeLabourGrade
		FOREIGN KEY (LabourGrade)
			REFERENCES PayGrade(LabourGrade)
);

DROP TABLE IF EXISTS Credential;
CREATE TABLE Credential(
    EmpID VARCHAR(255) NOT NULL UNIQUE,
	EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    EmpPassword VARCHAR(15) NOT NULL,
    CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT PKCredentialEmpID PRIMARY KEY (EmpID),
    CONSTRAINT FKCredentialEmpID
        FOREIGN KEY (EmpID)
            REFERENCES Employee(EmpID)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS Timesheet;
CREATE TABLE Timesheet(
	TimesheetID VARCHAR(255) NOT NULL UNIQUE,
    EmpID VARCHAR(255) NOT NULL,
	EndWeek DATE NOT NULL,
    Overtime INT,
    Flextime INT,
	Status ENUM('pending', 'submitted', 'approved', 'denied') NOT NULL DEFAULT 'pending',
    ReviewedBy VARCHAR(255),
    Signature TINYTEXT,
    Feedback TINYTEXT,
	CreatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	UpdatedAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ApprovedAt TIMESTAMP,
	CONSTRAINT PKTimesheetID 
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
	ProjectID VARCHAR(20) NOT NULL,
	WorkPackageID VARCHAR(20),
	Notes TINYTEXT,
	Hours FLOAT(5,2),
	CONSTRAINT PKTimesheetRowID 
		PRIMARY KEY(ProjectID, WorkPackageID, TimesheetID),
	CONSTRAINT FKTimesheetRow_Timesheet 
		FOREIGN KEY (TimesheetID) REFERENCES Timesheet(TimesheetID)
			ON UPDATE CASCADE
        	ON DELETE CASCADE
);

INSERT INTO PayGrade (LabourGrade, ChargeRate) VALUES ("PS", 3.50);

INSERT INTO Employee (EmpID, EmpName, LabourGrade) VALUES ("31000000-0000-0000-0000-000000000000", "Bruce Link",  "PS");
INSERT INTO Employee (EmpID, EmpName, LabourGrade) VALUES ("32000000-0000-0000-0000-000000000000", "Yogesh Verma",  "PS");

INSERT INTO Credential (EmpID, EmpUserName, EmpPassword) VALUES ("31000000-0000-0000-0000-000000000000", "bdlink", "bruce");
INSERT INTO Credential (EmpID, EmpUserName, EmpPassword) VALUES ("32000000-0000-0000-0000-000000000000", "yogiduzit", "yogesh");

INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("55000000-0000-0000-0000-000000000000", "31000000-0000-0000-0000-000000000000", DATE '2000/3/10');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("26000000-0000-0000-0000-000000000000", "31000000-0000-0000-0000-000000000000", DATE '2000/3/17');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("45700000-0000-0000-0000-000000000000", "32000000-0000-0000-0000-000000000000", DATE '2000/3/24');
