-- Authors Joseph Huntley, Cedric Patton

-- Author JH
-- If you already have this schema it drops it before processing the rest.
DROP SCHEMA IF EXISTS hris; 
CREATE SCHEMA hris;
-- This tells mySQL to use the hris scehema so that it isn't required to be specified the rest of the script.
USE hris;


-- Create a table for departments in the company
CREATE TABLE Department(
department_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
name VARCHAR(45) NOT NULL);

-- Creates a table for different position types
CREATE TABLE PositionType(
position_type_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
type VARCHAR(45) NOT NULL);

-- Create a table for positions in the company
CREATE TABLE Positions(
position_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
title VARCHAR(45) NOT NULL,
department_id INT NOT NULL, 
management TINYINT NOT NULL,
position_type_id INT,
FOREIGN KEY(position_type_id) REFERENCES PositionType(position_type_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
FOREIGN KEY(department_id) REFERENCES Department(department_id)
ON DELETE CASCADE
ON UPDATE CASCADE);

-- Creates a table for employees
CREATE TABLE Employee (
  employee_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  first_name VARCHAR(45) NOT NULL,
  middle_initial char,
  last_name VARCHAR(45) NOT NULL,
  password VARCHAR(45) NOT NULL,
  email VARCHAR(75) NOT NULL,
  phone VARCHAR(25) NOT NULL,
  position_id INT NOT NULL,
  status VARCHAR(45),
  FOREIGN KEY(position_id) REFERENCES Positions(position_id)
  ON DELETE CASCADE
  ON UPDATE CASCADE);

-- Creates a table for address
CREATE TABLE Address(
address_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
employee_id INT,
street VARCHAR(45) NOT NULL,
city VARCHAR(45) NOT NULL,
state VARCHAR(45) NOT NULL,
-- 1 is true 0 is false
primary_residence TINYINT NOT NULL,
FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
ON DELETE CASCADE
ON UPDATE CASCADE);

-- Creates a table for Training
CREATE TABLE Training (
class_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
class_name VARCHAR(45) NOT NULL);

-- Creates a table to keep track of class attendance
CREATE TABLE ClassAttendance (
attendance_id INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
employee_id INT NOT NULL,
class_id INT NOT NULL,
FOREIGN KEY(employee_id) REFERENCES Employee(employee_id)
ON DELETE CASCADE
ON UPDATE CASCADE,
FOREIGN KEY(class_id) REFERENCES Training(class_id)
ON DELETE CASCADE
ON UPDATE CASCADE);

-- Author CP
-- Employee Performance Table 
CREATE TABLE `hris`.`Performance` (
  `performance_id` INT NOT NULL AUTO_INCREMENT,
  `rating` INT NULL,
  `remarks` VARCHAR(255) DEFAULT "",
  `employee_id` INT NULL,
  PRIMARY KEY (`performance_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

    
-- Payroll Table
CREATE TABLE `hris`.`Payroll` (
  `payroll_id` INT NOT NULL AUTO_INCREMENT,
  `ishourly` BOOLEAN NULL,
  `isfulltime` BOOLEAN NULL,
  `pay_rate` DECIMAL(10,2) NULL,
  `biweekly_pay` DECIMAL(10, 2) NULL,
  `clocked_hours` INT NULL,
  `employee_id` INT NULL,
  PRIMARY KEY (`payroll_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

  
--   Health Plan Table
CREATE TABLE `hris`.`HealthPlan` (
  `healthplan_id` INT NOT NULL AUTO_INCREMENT,
  `family_coverage` BOOLEAN NULL,
  `deductable_cost` INT NULL,
  `copayment_cost` DECIMAL(7, 2) NULL,
  `coinsurance_cost` DECIMAL(7, 2) NULL,
  `monthly_cost` DECIMAL(6, 2) NULL,
  `coverage_provider_name` VARCHAR(45) NULL,
  `employee_id` INT NOT NULL,
  PRIMARY KEY (`healthplan_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee`(`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);
--   Vision Table
CREATE TABLE `hris`.`VisionPlan` (
  `visionplan_id` INT NOT NULL AUTO_INCREMENT,
  `family_coverage` BOOLEAN NULL,
  `deductable_cost` INT NULL,
  `copayment_cost` DECIMAL(7, 2) NULL,
  `coinsurance_cost` DECIMAL(7, 2) NULL,
  `monthly_cost` DECIMAL(6, 2) NULL,
  `coverage_provider_name` VARCHAR(45) NULL,
  `employee_id` INT NOT NULL,
  PRIMARY KEY (`visionplan_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee`(`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--   Dental Table 
 CREATE TABLE `hris`.`DentalPlan` (
  `dentalplan_id` INT NOT NULL AUTO_INCREMENT,
  `family_coverage` BOOLEAN NULL,
  `deductable_cost` INT NULL,
  `copayment_cost` DECIMAL(7, 2) NULL,
  `coinsurance_cost` DECIMAL(7, 2) NULL,
  `monthly_cost` DECIMAL(6, 2) NULL,
  `coverage_provider_name` VARCHAR(45) NULL,
  `employee_id` INT NOT NULL,
  PRIMARY KEY (`dentalplan_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee`(`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);
  
-- Work Schedule Table
CREATE TABLE `hris`.`WorkSchedule` (
    `workschedule_id` INT NOT NULL AUTO_INCREMENT,
    `mon_start` TIME NULL,
    `mon_end` TIME NULL,
    `tue_start` TIME NULL,
    `tue_end` TIME NULL,
    `wed_start` TIME NULL,
    `wed_end` TIME NULL,
    `thur_start` TIME NULL,
    `thur_end` TIME NULL,
    `fri_start` TIME NULL,
    `fri_end` TIME NULL,
    `sat_start` TIME NULL,
    `sat_end` TIME NULL,
    `sun_start` TIME NULL,
    `sun_end` TIME NULL,
    `employee_id` INT NULL,
    PRIMARY KEY (`workschedule_id`),
    FOREIGN KEY (`employee_id`)
	REFERENCES `hris`.`Employee` (`employee_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);
    

-- Attendence Table
CREATE TABLE `hris`.`Attendance` (
  `attendance_id` INT NOT NULL AUTO_INCREMENT,
  `absences` INT NULL,
  `tardies` INT NULL,
  `sick_leaves` INT NULL,
  `vacation_days` INT NULL,
  `employee_id` INT NULL,
  `workschedule_id` INT NULL,
  `clockinout_id` INT NULL,
  PRIMARY KEY (`attendance_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

--   ClockInOut Table 
CREATE TABLE `hris`.`ClockInOut` (
  `clockinout_id` INT NOT NULL AUTO_INCREMENT,
  `isClockedIn` BOOLEAN NOT NULL,
  `employee_id` INT NULL,
  `workSchedule_id` INT NULL,
  `attendance_id` INT NULL,
  `last_clockIn` TIME NULL,
  PRIMARY KEY (`clockInOut_id`, `isClockedIn`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (`workschedule_id`)
  REFERENCES `hris`.`WorkSchedule` (`workschedule_id`),
  FOREIGN KEY (`attendance_id`)
  REFERENCES `hris`.`Attendance` (`attendance_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

-- Retirement Table
CREATE TABLE `hris`.`Retirement` (
  `retirement_id` INT NOT NULL AUTO_INCREMENT,
  `savings` DECIMAL(20,8) NULL,
  `income_percentage` INT NULL,
  `employee_id` INT NULL,
  PRIMARY KEY (`retirement_id`),
  FOREIGN KEY (`employee_id`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);

-- Request Type Table
CREATE TABLE `hris`.`RequestType` (
  `request_type_id` INT NOT NULL AUTO_INCREMENT,
  `request_type` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`request_type_id`)
);

-- Requests Table
CREATE TABLE `hris`.`Requests` (
  `requests_id` INT NOT NULL AUTO_INCREMENT,
  `request_details` VARCHAR(255) NOT NULL,
  `date_of_request` DATE NOT NULL,
  `is_request_open` TINYINT NOT NULL,
  `requesting_employee` INT NOT NULL,
  `request_type_id` INT NOT NULL,
  `responding_manager` INT NULL,
  `date_of_response` DATE NULL,
  `response_details` VARCHAR(255) NULL,
  PRIMARY KEY (`requests_id`),
  FOREIGN KEY (`requesting_employee`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE,
  FOREIGN KEY (`request_type_id`)
  REFERENCES `hris`.`RequestType` (`request_type_id`),
  FOREIGN KEY (`responding_manager`)
  REFERENCES `hris`.`Employee` (`employee_id`)
  ON DELETE CASCADE
  ON UPDATE CASCADE
);


CREATE VIEW Paystub AS
 SELECT employee_id AS ID,
 payroll_id AS "Paystub ID",
 CONCAT(first_name, ' ', last_name) AS Name, 
 clocked_hours AS 'Clocked Hours', 
 ishourly AS 'Is Hourly?', 
 pay_rate AS 'Payrate', 
 biweekly_pay 'Net Pay', 
 healthplan.monthly_cost AS 'Health Plan Deduction', 
 visionplan.monthly_cost AS 'Vision Plan Deduction',
 dentalplan.monthly_cost AS 'Dental Plan Deduction'
 FROM hris.Payroll
 JOIN hris.Employee using(employee_id)
 JOIN hris.Healthplan using(employee_id)
 JOIN hris.Visionplan using(employee_id)
 JOIN hris.Dentalplan using(employee_id);



