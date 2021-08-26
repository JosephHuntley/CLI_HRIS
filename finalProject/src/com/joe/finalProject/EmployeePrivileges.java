package com.paulnchinnam.finalProject;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class EmployeePrivileges {
	// author: Cedric Patton

	// create db connection

	static Connection con = Database.con();

	// Scanner instance for receiving user input
	static Scanner sc = new Scanner(System.in);

	static DecimalFormat df = new DecimalFormat("$#0.00");

	static void viewProfile(int id) {

		String sql = "SELECT first_name, last_name, email, phone, title, type, " + "street, city, state FROM employee "
				+ "JOIN positions using(position_id) " + "JOIN PositionType using(position_type_id) "
				+ "JOIN Address using(employee_id)" + "WHERE employee_id = ?";

		try {

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			String firstName = "";
//			String middleInitial = "";
			String lastName = "";
			String email = "";
			String phone = "";
			String title = "";
			String type = "";
			String street = "";
			String city = "";
			String state = "";

			while (rs.next()) {

				firstName = rs.getString("first_name");
//				middleInitial = rs.getString("middle_initial");
				lastName = rs.getString("last_name");
				email = rs.getString("email");
				phone = rs.getString("phone");
				title = rs.getString("title");
				type = rs.getString("type");
				street = rs.getString("street");
				city = rs.getString("city");
				state = rs.getString("state");

			}

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			System.out.println("PROFILE INFO");
			System.out.println("============ \n");
			System.out.println("First Name: " + firstName);
//			System.out.println(middleInitial);
			System.out.println("Last Name " + lastName);
			System.out.println("Email " + email);
			System.out.println("Phone " + phone);
			System.out.println("Address: " + street + " " + city + ", " + state);
			System.out.println("Job Title: " + title);
			System.out.println("Position Type:" + type);
			System.out.println("******************************");

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

	static void updateProfile(int id) {

		boolean quit = false;

		do {
			System.out.println("\n \n \n \n  ");
			System.out.println("\n What would you like to update?");
			System.out.println("================================= \n");
			System.out.println("Option 1: Update First Name");
			System.out.println("Option 2: Update Last Name");
			System.out.println("Option 3: Update Email");
			System.out.println("Option 4: Update Phone Number");
			System.out.println("Option 5: Update Street Adress");
			System.out.println("Option 6: Update City Address");
			System.out.println("Option 7: Update State Address");
			System.out.println("Option 8: Update Password");
			System.out.println("Option 0: Return to Main Menu \n");

			String input = sc.nextLine();
			String infoToBeUpdated = "";
			String columnName = "";
			String newValue = "";
			String updatedValue = "";
			String sql = "";

			switch (input) {
			case "1":
				infoToBeUpdated = "first name";
				columnName = "first_name";
				break;
			case "2":
				infoToBeUpdated = "last name";
				columnName = "last_name";
				break;
			case "3":
				infoToBeUpdated = "email";
				columnName = "email";
				break;
			case "4":
				infoToBeUpdated = "phone number";
				columnName = "phone";
				break;
			case "5":
			case "6":
			case "7":

				if (input.equals("5")) {

					infoToBeUpdated = "street address";
					columnName = "street";

				} else if (input.equals("6")) {

					infoToBeUpdated = "city address";
					columnName = "city";

				} else {

					infoToBeUpdated = "state address";
					columnName = "state";

				}

				sql = String.format("UPDATE address SET %s = ? " + "WHERE employee_id = %s", columnName, id);
				break;
			case "8":
				infoToBeUpdated = "password";
				columnName = "password";
				break;
			case "0":

				System.out.println("This option will Return to Main Menu \n");
				quit = true;
				break;

			default:

				System.out.println("invalid selection");

			}

			if (!infoToBeUpdated.equals("")) {
				System.out.println(String.format(
						"%s has been selected to " + "update, what would you like to change it to?", infoToBeUpdated));

				updatedValue = sc.nextLine();

				if(columnName.equals("password")) {
					newValue = Database.sha1Hash(updatedValue);
				}else {
					newValue = updatedValue;
				}

				// set sql string to update employee table if none other is selected
				if (sql.equals("")) {

					sql = String.format("UPDATE employee SET %s = ? " + "WHERE employee_id = %s", columnName, id);
				}

				try {

					PreparedStatement pst = con.prepareStatement(sql);
					pst.setString(1, newValue);

					System.out.println(String.format(
							"%s will be changed to %s, are you sure? Enter 'y' to continue or 'n' to cancel. \n",
							infoToBeUpdated, updatedValue));

					boolean incorrectResponse = true;

					// while loop does continues until user enters y or n
					do {
						String response = sc.nextLine();

						if (response.equals("y")) {

							pst.executeUpdate();
							System.out.println(String.format("%s has been changed", infoToBeUpdated));
							incorrectResponse = false;
						} else if (response.equals("n")) {

							System.out.println("changes will be canceled");
							incorrectResponse = false;

						} else

							System.out.println("incorrect input, enter 'y' or 'n'.");

					} while (incorrectResponse);

				} catch (SQLException e) {

					e.printStackTrace();

				}
			}
		} while (quit != true);

	}

	static void viewPerformance(int id) {
		String sql = "SELECT * FROM Performance WHERE employee_id = ?";
		try {

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			int rating = 0;
			String remarks = "";

			while (rs.next()) {
				// primitive data types can't be used for null comparison
				// must use Integer Wrapper class to convert to object

				Integer dbRating = rs.getInt("rating");
				String dbRemarks = rs.getString("remarks");

				// conditionals check to see if database has null values
				// if so, we display a default value instead
				if (dbRating != null)
					rating = dbRating;

				if (dbRemarks == null)
					remarks = "no remarks added";
				else
					remarks = dbRemarks;
			}

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			System.out.println("PERFORMANCE");
			System.out.println("Permormance rating (Scale of 1-10): " + rating);
			System.out.println("Remarks: " + remarks);
			System.out.println("******************************");

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	static void viewBenefits(int id) {

		String sql = "SELECT * FROM HealthPlan " + "JOIN DentalPlan using(employee_id) "
				+ "JOIN VisionPlan using(employee_id) " + "WHERE employee_id = ?";

		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			Boolean hasFamilyCoverageHealthPlan = false, hasFamilyCoverageDentalPlan = false,
					hasFamilyCoverageVisionPlan = false;

			Integer deductableCostHealthPlan = 0, deductableCostDentalPlan = 0, deductableCostVisionPlan = 0;

			Float copayCostHealthPlan = 0.0f, coinsuranceCostHealthPlan = 0.0f, monthlyCostHealthPlan = 0.0f,
					copayCostDentalPlan = 0.0f, coinsuranceCostDentalPlan = 0.0f, monthlyCostDentalPlan = 0.0f,
					copayCostVisionPlan = 0.0f, coinsuranceCostVisionPlan = 0.0f, monthlyCostVisionPlan = 0.0f;

			String providerNameHealthPlan = "", providerNameDentalPlan = "", providerNameVisionPlan = "",
					coverageTypeHealthPlan = "", coverageTypeDentalPlan = "", coverageTypeVisionPlan = "";

			while (rs.next()) {
				hasFamilyCoverageHealthPlan = rs.getBoolean("HealthPlan.family_coverage");
				hasFamilyCoverageDentalPlan = rs.getBoolean("DentalPlan.family_coverage");
				hasFamilyCoverageVisionPlan = rs.getBoolean("VisionPlan.family_coverage");

				// determine what type of coverage employee has depending on boolean value
				if (hasFamilyCoverageHealthPlan)
					coverageTypeHealthPlan = "Family";
				else
					coverageTypeHealthPlan = "Single";

				if (hasFamilyCoverageDentalPlan)
					coverageTypeDentalPlan = "Family";
				else
					coverageTypeDentalPlan = "Single";

				if (hasFamilyCoverageVisionPlan)
					coverageTypeVisionPlan = "Family";
				else
					coverageTypeVisionPlan = "Single";

				// deductable cost for benefits
				deductableCostHealthPlan = rs.getInt("HealthPlan.deductable_cost");
				deductableCostDentalPlan = rs.getInt("DentalPlan.deductable_cost");
				deductableCostVisionPlan = rs.getInt("VisionPlan.deductable_cost");

				// copayment cost for benefits
				copayCostHealthPlan = rs.getFloat("HealthPlan.copayment_cost");
				copayCostDentalPlan = rs.getFloat("DentalPlan.copayment_cost");
				copayCostVisionPlan = rs.getFloat("VisionPlan.copayment_cost");

				// monthly cost for benefits
				monthlyCostHealthPlan = rs.getFloat("HealthPlan.monthly_cost");
				monthlyCostDentalPlan = rs.getFloat("DentalPlan.monthly_cost");
				monthlyCostVisionPlan = rs.getFloat("VisionPlan.monthly_cost");

				providerNameHealthPlan = rs.getString("HealthPlan.coverage_provider_name");
				providerNameDentalPlan = rs.getString("DentalPlan.coverage_provider_name");
				providerNameVisionPlan = rs.getString("VisionPlan.coverage_provider_name");
			}

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			// Health Coverage
			System.out.println("Health Plan");
			System.out.println("===========");
			System.out.println("Coverage type: " + coverageTypeHealthPlan);
			System.out.println("Deductable: " + deductableCostHealthPlan);
			System.out.println("Copay Cost: " + copayCostHealthPlan);
			System.out.println("Monthly Cost: " + monthlyCostHealthPlan + "\n");
			// Dental Coverage
			System.out.println("Dental Plan");
			System.out.println("===========");
			System.out.println("Coverage type: " + coverageTypeDentalPlan);
			System.out.println("Deductable: " + deductableCostDentalPlan);
			System.out.println("Copay Cost: " + copayCostDentalPlan);
			System.out.println("Monthly Cost: " + monthlyCostDentalPlan + "\n");
			// Vision Coverage
			System.out.println("Vision Plan");
			System.out.println("==========");
			System.out.println("Coverage type: " + coverageTypeVisionPlan);
			System.out.println("Deductable: " + deductableCostVisionPlan);
			System.out.println("Copay Cost: " + copayCostVisionPlan);
			System.out.println("Monthly Cost: " + monthlyCostVisionPlan);
			System.out.println("******************************");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void viewSchedule(int id) {
		System.out.println("employee will see schedule here.");
		String sql = "SELECT * FROM WorkSchedule WHERE employee_id = ?";

		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			String monStart = "", monEnd = "", tueStart = "", tueEnd = "", wedStart = "", wedEnd = "", thurStart = "",
					thurEnd = "", friStart = "", friEnd = "", satStart = "", satEnd = "", sunStart = "", sunEnd = "";

			while (rs.next()) {
				monStart = rs.getString("mon_start");
				monEnd = rs.getString("mon_end");
				tueStart = rs.getString("tue_start");
				tueEnd = rs.getString("tue_end");
				wedStart = rs.getString("wed_start");
				wedEnd = rs.getString("wed_end");
				thurStart = rs.getString("thur_start");
				thurEnd = rs.getString("thur_end");
				friStart = rs.getString("fri_start");
				friEnd = rs.getString("fri_end");
				satStart = rs.getString("sat_start");
				satEnd = rs.getString("sat_end");
				sunStart = rs.getString("sun_start");
				sunEnd = rs.getString("sun_end");
			}

			System.out.println("Monday: " + monStart + " - " + monEnd);
			System.out.println(monEnd);

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			System.out.println("Work Schedule");
			System.out.println("=============");
			// Monday
			System.out.println("Monday: " + monStart + " - " + monEnd + "\n");
			// Tuesday
			System.out.println("Tuesday: " + tueStart + " - " + tueEnd + "\n");
			// Wednesday
			System.out.println("Wednesday: " + wedStart + " - " + wedEnd + "\n");
			// Thursday
			System.out.println("Thursday: " + thurStart + " - " + thurEnd + "\n");
			// Friday
			System.out.println("Friday: " + friStart + " - " + friEnd + "\n");
			// Saturday
			System.out.println("Saturday: " + satStart + " - " + satEnd + "\n");
			// Sunday
			System.out.println("Sunday: " + sunStart + " - " + sunEnd);
			System.out.println("******************************");

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	static void viewPaystub(int id) {
		System.out.println("employee will see paystub here.");
		String sql = "SELECT employee_id AS ID, \n" + " CONCAT(first_name, ' ', last_name) AS Name, \n"
				+ " clocked_hours AS 'Clocked Hours', \n" + " ishourly AS 'Is Hourly?', \n"
				+ " isfulltime AS 'Is Full-Time?', \n" + " pay_rate AS 'Payrate', \n" + " biweekly_pay 'Gross Pay', \n"
				+ " healthplan.monthly_cost AS 'Health Plan Deduction', \n"
				+ " visionplan.monthly_cost AS 'Vision Plan Deduction',\n"
				+ " dentalplan.monthly_cost AS 'Dental Plan Deduction'\n" + " FROM hris.Payroll\n"
				+ " JOIN hris.Employee using(employee_id)\n" + " JOIN hris.Healthplan using(employee_id)\n"
				+ " JOIN hris.Visionplan using(employee_id)\n" + " JOIN hris.Dentalplan using(employee_id) "
				+ "WHERE employee_id = ?";

		try {

			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			int cHours = 0;
			String name = "", payType = "", employmentType = "";
			boolean isHourly = false, isFulltime = false;
			float payRate = 0.0f, grossPay = 0.0f, monthlyCostHealth = 0.0f, monthlyCostDental = 0.0f,
					monthlyCostVision = 0.0f, totalDeductions = 0.0f, netPay = 0.0f;

			while (rs.next()) {

				cHours = rs.getInt("Clocked Hours");
				name = rs.getString("Name");

				// determine pay type based on isHourly
				isHourly = rs.getBoolean("Is Hourly?");
				if (isHourly)
					payType = "Hourly Pay";
				else
					payType = "Salary Pay";
				// determine employment type based on isFulltime
				isFulltime = rs.getBoolean("Is Full-Time?");
				if (isFulltime)
					employmentType = "Full Time";
				else
					employmentType = "Part Time";

				payRate = rs.getFloat("Payrate");
				grossPay = rs.getFloat("Gross Pay");
				monthlyCostHealth = rs.getFloat("Health Plan Deduction");
				monthlyCostVision = rs.getFloat("Vision Plan Deduction");
				monthlyCostDental = rs.getFloat("Dental Plan Deduction");
				totalDeductions = monthlyCostHealth + monthlyCostVision + monthlyCostDental;
				netPay = grossPay - totalDeductions;
			}

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			System.out.println("Pay Stub");
			System.out.println("======== \n");
			System.out.println("Name: " + name);
			System.out.println("ID: " + id);
			System.out.println("Type of Pay: " + payType);
			System.out.println("Type of Employment?: " + employmentType);
			System.out.println("Pay Rate: " + payRate);
			System.out.println("Biweekly Hours Worked: " + cHours);
			System.out.println("Gross Pay: " + grossPay);
			System.out.println("Health Plan Deduction: " + monthlyCostHealth);
			System.out.println("Vision Plan Deduction: " + monthlyCostVision);
			System.out.println("Dental Plan Deduction: " + monthlyCostDental);
			System.out.println("Total Deductions: " + totalDeductions);
			System.out.println("Net Pay: " + netPay);
			System.out.println("******************************");

		} catch (SQLException e) {

			e.printStackTrace();

		}

	}

	static void clockInOut(int id) {

		String sql1 = "SELECT isClockedIn from ClockInOut WHERE employee_id = ?";
		String clockInStatus = "", lastClockedIn = null, lastClockedOut = null;
		Boolean isClockedIn = false, incorrectInput = true, usingClockInOutMenu = true;

		// First try catch to find clock-in status of user.
		// Updates after user clocks in/out until they quit main menu,
		// then usingClockInOutMenu will be set to false.
		while (usingClockInOutMenu) {

			// resets variable. Else inner while loop never runs again after
			// first execution and an infinite loop occurs
			incorrectInput = true;

			try {

				PreparedStatement pst = con.prepareStatement(sql1);
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();

				while (rs.next()) {
					isClockedIn = rs.getBoolean("isClockedIn");
					// set clockInStatus to true/false depending on isClockedIn's value
					if (isClockedIn == null || isClockedIn == false)
						clockInStatus = "clocked out";
					else
						clockInStatus = "clocked in";
				}

			} catch (SQLException e) {

				e.printStackTrace();

			}

			System.out.println("\n \n \n \n  ");
			System.out.println("==============================");
			System.out.println(String.format("You are currently %s.", clockInStatus));
			System.out.println("==============================");

			// User will continue to be prompted to enter 's' or 'q' until done so.
			while (incorrectInput) {
				System.out.println("\n \n  ");
				System.out.println("Enter 's' to switch status or 'q' to quit.");
				String input = sc.nextLine();
				input = input.toLowerCase();

				// Second try catch to change clock-in status of user.
				// also used to track hours worked and tardies
				if (input.equals("s")) {

					String sqlUpdateIsClockInStatus = "", sqlSelectLastClockIn = "", sqlUpdateLastClockIn,
							sqlUpdateClockedHours = "";
					// truncatedTo method removes the milliSeconds from the time and truncates to
					// just seconds
					LocalTime currentTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

					try {

						if (isClockedIn) {
							// if employee is clocked in, clock them out
							sqlUpdateIsClockInStatus = "UPDATE ClockInOut SET isClockedIn = 0 WHERE employee_id = ?";
							sqlSelectLastClockIn = "SELECT last_clockIn FROM ClockInOut WHERE employee_id = ?";
							clockInStatus = "clocked out";

							/*
							 * If user is already clocked in, we want to clock them out, and retrieve the
							 * last_clockIn value from the ClockInOut table. From there, we add the
							 * difference of the lastClockIn and currentTime variables to the clocked_hours
							 * column in our Payroll table.
							 */

							// Update clock in status
							PreparedStatement pst = con.prepareStatement(sqlUpdateIsClockInStatus);
							pst.setInt(1, id);
							pst.executeUpdate();

							// Find last_clockIn time in clockInOut table
							PreparedStatement pst2 = con.prepareStatement(sqlSelectLastClockIn);
							pst2.setInt(1, id);
							ResultSet rs = pst2.executeQuery();

							Time lastClockIn = new Time(0);

							while (rs.next()) {
								lastClockIn = rs.getTime("last_clockIn");
							}

							// Find difference between lastClockIn and currentTime
							// Calculating the difference in MilliSeconds
							long shiftDurationInMilliSeconds = Math
									.abs(lastClockIn.getTime() - Time.valueOf(currentTime).getTime());

							// Calculating the difference in Hours
							long shiftDurationInHours = (shiftDurationInMilliSeconds / (60 * 60 * 1000)) % 24;

							// Update clocked_hours in payroll table using shiftDurationInHours
							Statement pst3 = con.createStatement();
							// Prepared Statement didn't work with query, had to use a regular statement
							sqlUpdateClockedHours = String.format(
									"UPDATE Payroll SET clocked_hours = clocked_hours + %s WHERE employee_id = %s",
									(int) shiftDurationInHours, id);
							pst3.executeUpdate(sqlUpdateClockedHours);

							// Display to User
							System.out.println("\n \n \n \n");
							System.out.println("----------------------------");
							System.out.println("Shift Details");
							System.out.println("=============");
							System.out.println("Clock In Time: " + lastClockIn);
							System.out.println("Clock Out Time: " + currentTime);
							System.out.println("Clocked Hours: " + shiftDurationInHours);
							System.out.println("----------------------------");

						}

						else {
							// if employee is clocked out, clock them in
							sqlUpdateIsClockInStatus = " UPDATE ClockInOut SET isClockedIn = 1 WHERE employee_id = ?";
							sqlUpdateLastClockIn = "UPDATE ClockInOut SET last_clockIn = ? WHERE employee_id = ?";
							clockInStatus = "clocked in";
							/*
							 * If employee is clocked out, clock them in. Then we create a DateTime object
							 * and save it's value into last_clockIn column in clockInOut table.
							 */

							// Update clock in status
							PreparedStatement pst = con.prepareStatement(sqlUpdateIsClockInStatus);
							pst.setInt(1, id);
							pst.executeUpdate();

							// Update clock in time in ClockInOut table
							PreparedStatement pst2 = con.prepareStatement(sqlUpdateLastClockIn);
							pst2.setTime(1, Time.valueOf(currentTime));
							pst2.setInt(2, id);
							pst2.executeUpdate();

						}

					} catch (SQLException e) {

						e.printStackTrace();

					}

					incorrectInput = false;

				} else if (input.equals("q")) {

					incorrectInput = false;
					usingClockInOutMenu = false;

				} else {

					System.out.println("\n \n  ");
					System.out.println("++++++++++++++++++++++++");
					System.out.println("Incorrect Input Entered.");
					System.out.println("++++++++++++++++++++++++ \n");

				}
			}
		}
	}

	static void viewRetirement(int id) {
		System.out.println("retirement goes here.");
		String sql = "SELECT * FROM Retirement WHERE employee_id = ?";

		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.setInt(1, id);
			ResultSet rs = pst.executeQuery();

			Float savings = null;
			Integer incomePercentage = null;

			while (rs.next()) {

				savings = rs.getFloat("savings");

				incomePercentage = rs.getInt("income_percentage");

			}

			System.out.println("\n \n \n \n  ");
			System.out.println("******************************");
			System.out.println("Retirement");
			System.out.println("========== \n");
			if (savings == null)
				System.out.println("Savings: None Available");
			else
				System.out.println("Savings: " + savings + " USD");
			if (incomePercentage == null)
				System.out.println("Income Percentage: Retirement account not setup.");
			else
				System.out.println("Income Percentage: " + incomePercentage + "%");
			System.out.println("******************************");

		} catch (SQLException e) {

			e.printStackTrace();

		}
	}

	static void handleRequests(int id) {
		boolean inRequestsMenu = true;
		while (inRequestsMenu) {
			System.out.println("\n \n \n \n  ");
			System.out.println("Requests Menu");
			System.out.println("============= \n");
			System.out.println("Option 1: Make a request");
			System.out.println("Option 2: View past requests");
			System.out.println("Option 3: Return to main menu");
			String input = sc.nextLine();

			if (input.equals("1")) {
				// prompt keeps printing to user selects available option
				boolean incorrectInput = true;
				int requestTypeId = 0;
				String requestType = "";

				while (incorrectInput) {
					// user is able to make new requests
					System.out.println("\n \n \n \n  ");
					System.out.println("What type of request are you making?");
					System.out.println("Option 1: Vacation");
					System.out.println("Option 2: Sick Leave");
					System.out.println("Option 3: Emergency");
					System.out.println("Option 4: Retirement");
					System.out.println("Option 5: Benefits");
					System.out.println("Option 6: Assistance");
					System.out.println("Option 7: Other");
					String in = sc.nextLine();

					String sqlSelectRequestTypeID = "SELECT request_type_id FROM RequestType WHERE request_type = ?";

					// finds the matching request_type_id that relates to type of request the user
					// selected
					switch (in) {
					case "1":
						requestType = "VACATION";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "VACATION");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "2":
						requestType = "SICK LEAVE";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "SICK LEAVE");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "3":
						requestType = "EMERGENCY";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "EMERGENCY");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "4":
						requestType = "RETIREMENT";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "RETIREMENT");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "5":
						requestType = "BENEFITS";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "BENEFITS");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "6":
						requestType = "ASSISTANCE";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "ASSISTANCE");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;

						break;
					case "7":
						requestType = "OTHER";

						try {
							PreparedStatement pst = con.prepareStatement(sqlSelectRequestTypeID);
							pst.setString(1, "OTHER");
							ResultSet rs = pst.executeQuery();

							while (rs.next()) {
								requestTypeId = rs.getInt("request_type_id");
							}
						} catch (SQLException e) {
							e.printStackTrace();
						}
						incorrectInput = false;
						
						break;
					default:

						System.out.println("\n \n  ");
						System.out.println("++++++++++++++++++++++++");
						System.out.println("Incorrect Input Entered.");
						System.out.println("++++++++++++++++++++++++ \n");
					}
				}

				System.out.println("Request_type_id: " + requestTypeId);
				System.out.println("\n ");
				System.out.println("Explain what your quest is about...");
				String requestDetails = sc.nextLine();

				boolean wrongInput = true;
				while (wrongInput) {
					System.out.println("\n ");
					System.out.println("Request Preview:");
					System.out.println("--------------------------");
					System.out.println(requestType);
					System.out.println("Request Details: " + requestDetails);
					System.out.println("--------------------------");
					System.out.println("\n");
					System.out.println("Enter 'y' to submit or 'n' to cancel.");
					String r = sc.nextLine().toLowerCase();

					if (r.equals("y") || r.equals("yes")) {
						
						// submit request
					    String sqlCreateRequest = "INSERT INTO Requests (`request_details`, `date_of_request`, `is_request_open`, `requesting_employee`, `request_type_id`) VALUES ( ?, ?, ?, ?, ?)";
					    LocalDate currentDate = LocalDate.now();
					   
					    
					    
					    try {
					    	PreparedStatement pst = con.prepareStatement(sqlCreateRequest);
					    	pst.setString(1, requestDetails);
					    	pst.setDate(2, Date.valueOf(currentDate));
					    	pst.setInt(3, 1);
					    	pst.setInt(4, id);
					    	pst.setInt(5, requestTypeId);
					    	pst.execute();
					    	
					    	System.out.println("\n \n  ");
					    	System.out.println("==================");
					    	System.out.println("Request Submitted.");
					    	System.out.println("==================");
					    	
					    } catch(SQLException e) {
					    	
					    	e.printStackTrace();
					    	
					    }
					    
					    //exit loop
					    wrongInput = false;
					    
					} else if (r.equals("n") || r.equals("no")) {
						
						//cancel request 
						wrongInput = false;
						
					} else {
						
						System.out.println("\n \n  ");
						System.out.println("++++++++++++++++++++++++");
						System.out.println("Incorrect Input Entered.");
						System.out.println("++++++++++++++++++++++++ \n");
					
					}
				}
			} else if (input.equals("2")) {
				// user is shown a sub menu where can choose what requests to view
				boolean viewingPastRequests = true;
				while (viewingPastRequests) {
					System.out.println("\n \n \n \n  ");
					System.out.println("View Requests");
					System.out.println("============= \n");
					System.out.println("Option 1: View open requests");
					System.out.println("Option 2: View closed requests");
					System.out.println("Option 3: View all requests");
					System.out.println("Option 4: Return to Requests Menu");
					String input1 = sc.nextLine();

					if (input1.equals("1")) {
						// show open requests
						String sql = "SELECT request_details, date_of_request, request_type  FROM Requests JOIN RequestType using(request_type_id) WHERE requesting_employee = ? AND is_request_open = 1";
						try {
							PreparedStatement pst = con.prepareStatement(sql);
							pst.setInt(1, id);
							ResultSet rs = pst.executeQuery();

							String requestDetails = "", dateOfRequest = "", requestType = "";

							while (rs.next()) {
								requestType = rs.getString("request_type");
								requestDetails = rs.getString("request_details");
								dateOfRequest = rs.getString("date_of_request");

								System.out.println("\n");
								System.out.println("--------------------------");
								System.out.println(requestType.toUpperCase());
								System.out.println("Date of Request: " + dateOfRequest);
								System.out.println("Request Details: " + requestDetails);
								System.out.println("--------------------------");

							}

						} catch (SQLException e) {

							e.printStackTrace();

						}

					} else if (input1.equals("2")) {
						// show closed requests
						String sql = "SELECT * FROM Requests JOIN RequestType using(request_type_id) INNER JOIN EMPLOYEE ON Requests.responding_manager=Employee.employee_id JOIN Positions using(position_id) WHERE requesting_employee = ? AND is_request_open = 0";
						try {
							PreparedStatement pst = con.prepareStatement(sql);
							pst.setInt(1, id);
							ResultSet rs = pst.executeQuery();

							String requestDetails = "", dateOfRequest = "", requestType = "", managerFirstName = "",
									managerLastName = "", dateOfResponse = "", responseDetails = "", title = "";

							while (rs.next()) {
								requestType = rs.getString("request_type");
								requestDetails = rs.getString("request_details");
								dateOfRequest = rs.getString("date_of_request");
								managerFirstName = rs.getString("first_name");
								managerLastName = rs.getString("last_name");
								dateOfResponse = rs.getString("date_of_response");
								responseDetails = rs.getString("response_details");
								title = rs.getString("title");

								System.out.println("\n");
								System.out.println("--------------------------");
								System.out.println(requestType.toUpperCase());
								System.out.println("Date of Request: " + dateOfRequest);
								System.out.println("Request Details: " + requestDetails);
								System.out.println("-- -- -- -- -- -- -- -- --");
								System.out.println("Responding Manager: " + managerFirstName + " " + managerLastName);
								System.out.println("Title: " + title);
								System.out.println("Date Of Response: " + dateOfResponse);
								System.out.println("Response Details: " + responseDetails);
								System.out.println("--------------------------");

							}

						} catch (SQLException e) {

							e.printStackTrace();

						}
					} else if (input1.equals("3")) {
						/*
						 * we can show all quests, but without the additional info that shows when
						 * viewing all closed requests this is because that sql query required a bunch
						 * of joins, and to differentiate closed and open requests without reducing the
						 * information would require a lot more logic and coding. So for that reason I
						 * decided to forego including extra info in closed requests.
						 */
						String sql = "SELECT * FROM Requests JOIN RequestType using(request_type_id) WHERE requesting_employee = ?";
						try {
							PreparedStatement pst = con.prepareStatement(sql);
							pst.setInt(1, id);
							ResultSet rs = pst.executeQuery();

							boolean isRequestOpen = true;
							String requestDetails = "", dateOfRequest = "", requestType = "", dateOfResponse = "",
									responseDetails = "";

							while (rs.next()) {
								requestType = rs.getString("request_type");
								requestDetails = rs.getString("request_details");
								dateOfRequest = rs.getString("date_of_request");
								dateOfResponse = rs.getString("date_of_response");
								responseDetails = rs.getString("response_details");
								isRequestOpen = rs.getBoolean("is_request_open");

								if (isRequestOpen) {

									System.out.println("\n");
									System.out.println("--------------------------");
									System.out.println(requestType.toUpperCase());
									System.out.println("Request Status: OPEN");
									System.out.println("Date of Request: " + dateOfRequest);
									System.out.println("Request Details: " + requestDetails);
									System.out.println("--------------------------");

								} else {

									System.out.println("\n");
									System.out.println("--------------------------");
									System.out.println(requestType.toUpperCase());
									System.out.println("Request Status: CLOSED");
									System.out.println("Date of Request: " + dateOfRequest);
									System.out.println("Request Details: " + requestDetails);
									System.out.println("-- -- -- -- -- -- -- -- --");
									System.out.println("Date Of Response: " + dateOfResponse);
									System.out.println("Response Details: " + responseDetails);
									System.out.println();
									System.out.println("*** view all closed requests to see additional info ***");
									System.out.println("--------------------------");
								}

							}

						} catch (SQLException e) {

							e.printStackTrace();

						}
					} else if (input1.equals("4")) {
						// return to requests menu
						viewingPastRequests = false;
					} else {
						System.out.println("\n \n  ");
						System.out.println("++++++++++++++++++++++++");
						System.out.println("Incorrect Input Entered.");
						System.out.println("++++++++++++++++++++++++ \n");
					}
				}

			} else if (input.equals("3")) {
				// returns user to main menu
				System.out.println("return to main menu");
				inRequestsMenu = false;
			}

			else {
				System.out.println("\n \n  ");
				System.out.println("++++++++++++++++++++++++");
				System.out.println("Incorrect Input Entered.");
				System.out.println("++++++++++++++++++++++++ \n");
			}
		}

	}

}
