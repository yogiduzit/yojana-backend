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
    CONSTRAINT PKEmployee PRIMARY KEY (EmpID),
    CONSTRAINT FKEmployeeLabourGrade
		FOREIGN KEY (LabourGrade)
			REFERENCES PayGrade(LabourGrade)
);

DROP TABLE IF EXISTS Credential;
CREATE TABLE Credential(
<<<<<<< HEAD
    EmpID VARCHAR(255) NOT NULL UNIQUE,
	EmpUserName VARCHAR(10) NOT NULL UNIQUE,
=======
    CredID VARCHAR(255) NOT NULL UNIQUE,
    EmpID INT NOT NULL UNIQUE,
>>>>>>> 0b1d1ae... Implemented basic JWT authentication
    EmpPassword VARCHAR(15) NOT NULL,
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
    EmpID INT NOT NULL,
	EndWeek DATE NOT NULL,
    Overtime int,
    Flextime int,
	Status ENUM('pending', 'submitted', 'approved', 'denied') NOT NULL DEFAULT 'pending',
    ReviewedBy INT,
    Signature TINYTEXT,
    Feedback TINYTEXT,
    UpdatedAt DATE,
    ApprovedAt DATE,
	PRIMARY KEY (TimesheetID),
	FOREIGN KEY (ReviewedBy) REFERENCES Employee(EmpID),
    FOREIGN KEY (EmpID) REFERENCES Employee(EmpID)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRow;
CREATE TABLE TimesheetRow(
	TimesheetID VARCHAR(255) NOT NULL,
	ProjectID VARCHAR(20) NOT NULL,
	WorkPackageID VARCHAR(20),
	Notes TINYTEXT,
	Hours FLOAT,
	PRIMARY KEY(ProjectID, WorkPackageID, TimesheetID),
	FOREIGN KEY (TimesheetID) REFERENCES Timesheet(TimesheetID)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO PayGrade VALUES ("PS", 3.50);

<<<<<<< HEAD
INSERT INTO Employee VALUES ("31000000-0000-0000-0000-000000000000", "Bruce Link",  "PS");
INSERT INTO Employee VALUES ("32000000-0000-0000-0000-000000000000", "Yogesh Verma",  "PS");

INSERT INTO Credential VALUES ("31000000-0000-0000-0000-000000000000","bdlink", "bruce");
INSERT INTO Credential VALUES ("32000000-0000-0000-0000-000000000000","yogiduzit", "yogesh");
=======
INSERT INTO Employee VALUES (1, "Bruce Link", "bdlink", "PS");
INSERT INTO Employee VALUES (2, "Yogesh Verma", "yogiduzit", "PS");

INSERT INTO Credential VALUES ("31000000-0000-0000-0000-000000000000", 1, "bruce");
INSERT INTO Credential VALUES ("32000000-0000-0000-0000-000000000000", 2, "yogesh");
>>>>>>> 0b1d1ae... Implemented basic JWT authentication

INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("55000000-0000-0000-0000-000000000000", 1, DATE '2000/3/10');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("26000000-0000-0000-0000-000000000000", 2, DATE '2000/3/17');
INSERT INTO Timesheet (TimesheetID, EmpID, EndWeek) VALUES ("45700000-0000-0000-0000-000000000000", 1, DATE '2000/3/24');
