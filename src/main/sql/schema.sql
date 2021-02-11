DROP DATABASE IF EXISTS project_management_system;
CREATE DATABASE project_management_system;

CREATE USER IF NOT EXISTS 'admin'@'localhost' IDENTIFIED BY 'admin';
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin';
GRANT ALL ON project_management_system.* TO 'admin'@'localhost';
GRANT ALL ON project_management_system.* TO 'admin'@'%';

USE project_management_system;

DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee(
    EmpID INT(5) AUTO_INCREMENT NOT NULL UNIQUE,
    EmpName VARCHAR(50) NOT NULL,
    EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    CONSTRAINT PKEmployee PRIMARY KEY (EmpID)
);

DROP TABLE IF EXISTS Credential;
CREATE TABLE Credential(
    CredID INT(5) NOT NULL UNIQUE,
    EmpUserName VARCHAR(10) NOT NULL UNIQUE,
    EmpPassword VARCHAR(15) NOT NULL,
    CONSTRAINT FKCredentialEmpUserName
        FOREIGN KEY (EmpUserName)
            REFERENCES Employee(EmpUserName)
            ON UPDATE CASCADE
            ON DELETE CASCADE
);

DROP TABLE IF EXISTS Timesheet;
CREATE TABLE Timesheet(
	TimesheetID INT AUTO_INCREMENT,
    EmpID INT(5),
	EndWeek DATE,
    ReviewedBy INT,
    Signature TINYTEXT,
    Feedback TINYTEXT,
	PRIMARY KEY (TimesheetID),
	FOREIGN KEY (ReviewedBy) REFERENCES Employee(EmpID),
    FOREIGN KEY (EmpID) REFERENCES Employee(EmpID)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

DROP TABLE IF EXISTS TimesheetRow;
CREATE TABLE TimesheetRow(
	TimesheetID int,
	ProjectID int,
	WorkPackageID VARCHAR(64),
	Notes TINYTEXT,
	Saturday FLOAT,
	Sunday FLOAT,
	Monday FLOAT, 
	Tuesday FLOAT,
	Wednesday FLOAT,
	Thursday FLOAT,
	Friday FLOAT,
	PRIMARY KEY(ProjectID, WorkPackageID, TimesheetID),
	FOREIGN KEY (TimesheetID) REFERENCES Timesheet(TimesheetID)
		ON UPDATE CASCADE
        ON DELETE CASCADE
);

INSERT INTO Employee VALUES (1, "Bruce Link", "bdlink");
INSERT INTO Employee VALUES (2, "Yogesh Verma", "yogiduzit");

INSERT INTO Timesheet (EmpID, EndWeek) VALUES (1, DATE '2000/3/10');
INSERT INTO Timesheet (EmpID, EndWeek) VALUES (1, DATE '2000/3/17');
INSERT INTO Timesheet (EmpID, EndWeek) VALUES (2, DATE '2000/3/24');

INSERT INTO Credential VALUES (1, "bdlink", "bruce");
INSERT INTO Credential VALUES (2, "yogiduzit", "yogesh");