package com.paulnchinnam.finalProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ManagerPrivileges extends EmployeePrivileges {
	// author: Joseph Huntley
	
	static Scanner sc = new Scanner(System.in);
	
	// Creates a database connection
	static Connection con = Database.con();
	static DecimalFormat df = new DecimalFormat("$#0.00");

	static void updatePrompt(int depId, int manageId) {
		/*
		 * A method that questions the manager on who and what they would like to
		 * update, it also verifies that the employee is part
		 */
		boolean quit = false;
		int empId = 0;
		while (quit == false) {
			System.out.println("\nWhich employee would you like to update?");
			System.out.println("Employee Id:\n\t");
			empId = Integer.parseInt(sc.nextLine());
			String name = Database.displayEmployee(empId, depId, manageId);

			/*
			 * If the user is not in the DB it will return null. prints out a message
			 * accordingly.
			 */
			if (name == null) {
				System.out.println("User not found, make sure they are in your department\nPlease try again "
						+ "or use a different employee ID");
			} else {
				System.out.println(name);
				System.out.println("Is this the employee you would like to update(Y/N)? ");
				String answer = sc.nextLine().toLowerCase();
				if (answer.equals("yes") || answer.equals("y")) {

					System.out.println("Which would you like to update?");
					System.out.println("================================= \n");
					System.out.println("Option 1: Contact info");
					System.out.println("Option 2: Address");
					System.out.println("Option 3: Payroll");
					System.out.println("Option 4: Schedule\n\t");
					String userInput = sc.nextLine().toLowerCase();

					if (userInput.equals("1") || userInput.equals("contact")) {
						contactUpdate(empId);

					} else if (userInput.equals("2") || userInput.equals("address")) {
						addressUpdate(empId);

					} else if (userInput.equals("3") || userInput.equals("payroll")) {
						payrollUpdate(empId);

					} else if (userInput.equals("4") || userInput.equals("schedule")) {
						scheduleUpdate(empId);

					}

					quit = true;
				} else {
					System.out.println("Would you like to quit(Y/N): ");
					answer = sc.nextLine().toLowerCase();
					if (answer.equals("yes") || answer.equals("y")) {
						quit = true;
					}
				}

			}
		}

	}

	static void contactUpdate(int empId) {
		final String sql = "UPDATE Employee SET first_name = ?, last_name = ?, "
				+ "email = ?, password = ?, phone = ?, position_id = ?" + " WHERE employee_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(sql);
			// Prompts the user for employee data
			System.out.print("\nFirst Name:\n\t");
			String fName = sc.nextLine();

			System.out.print("\nLast Name:\n\t");
			String lName = sc.nextLine();

			System.out.print("\nEmail:\n\t");
			String email = sc.nextLine();

			System.out.print("\nPhone:\n\t");
			String phone = sc.nextLine();

			int positionId = 0;
			while (positionId == 0) {
				System.out.print("\nPosition:\n\t");
				String position = sc.nextLine();

				HashMap<String, Integer> positionTable = new HashMap<String, Integer>();
				positionTable.put("Senior Developer", 1);
				positionTable.put("Engineer", 2);
				positionTable.put("Finance Director", 3);
				positionTable.put("Accountant Director", 4);
				positionTable.put("Developer", 5);
				positionTable.put("Accountant", 6);
				positionTable.put("Marketing Lead", 7);
				positionTable.put("HR Director", 8);
				positionTable.put("Operations Manager", 9);
				positionTable.put("CEO", 10);
				positionTable.put("CFO", 11);
				positionTable.put("COO", 12);
				positionTable.put("Project Manager", 13);
				positionTable.put("Scrum Master", 14);
				positionTable.put("Operations Director", 15);
				positionTable.put("Lead Developer", 16);
				positionTable.put("IT Director", 17);
				positionTable.put("Operations Director", 18);
				positionTable.put("Marketing Director", 19);
				positionTable.put("Program Manager", 20);
				positionTable.put("Database Admin", 21);
				positionTable.put("Systems Admin", 22);
				positionTable.put("Technical Support", 23);
				positionTable.put("QA Analyst", 24);

				if (positionTable.containsKey(position)) {
					positionId = positionTable.get(position);
				} else {
					System.out.println("That is not a valid option\nPlease select " + "one from below");
					for (Map.Entry<String, Integer> entry : positionTable.entrySet()) {
						System.out.println(entry.getKey());
					}
				}
			}

			System.out.print("\nPassword:\n\t");
			String passwd = sc.nextLine();
			passwd = Database.sha1Hash(passwd);

			// SQL injection for employee table
			ps.setString(1, fName);
			ps.setString(2, lName);
			ps.setString(3, email);
			ps.setString(4, passwd);
			ps.setString(5, phone);
			ps.setInt(6, positionId);
			ps.setInt(7, empId);
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static void addressUpdate(int empId) {

		final String sql = "UPDATE Address SET primary_residence = 0" + " WHERE employee_id = ?";

		System.out.println("Option 1: Update an existing address");
		System.out.println("Option 2: Add a new address");
		System.out.println("Which operation would you like to perform:\n\t");
		String userInput = sc.nextLine().toLowerCase();

		if (userInput.equals("1") || userInput.equals("update")) {
			final String select = "SELECT * FROM Address WHERE employee_id = ?";
			final String update = "UPDATE Address SET " + "street = ?, city = ?, state = ?, "
					+ "primary_residence = ? WHERE address_id = ?";

			try {
				PreparedStatement ps = con.prepareStatement(select);
				PreparedStatement ps1 = con.prepareStatement(update);
				PreparedStatement ps2 = con.prepareStatement(sql);
				ps.setInt(1, empId);
				ps2.setInt(1, empId);
				ResultSet rs = ps.executeQuery();
				int i = 1;
				HashMap<Integer, Integer> residences = new HashMap<Integer, Integer>();

				while (rs.next()) {
					System.out.print("\n" + i + ":\n\t");
					String street = rs.getString("street");
					System.out.print(street + " ");
					String city = rs.getString("city");
					System.out.print(city + " ");
					String state = rs.getString("state");
					System.out.println(state + " ");
					boolean residency = rs.getBoolean("primary_residence");
					System.out.println("Primary Residence:\n\t" + residency);

					int addressId = rs.getInt("address_id");
					residences.put(i, addressId);
					i++;
				}
				boolean quit = false;
				while (quit == false) {
					System.out.println("\nWhich address would you like to update:\n\t");
					int option = Integer.parseInt(sc.nextLine());

					if (residences.containsKey(option)) {
						ps1.setInt(5, residences.get(option));
						quit = true;

					} else {
						System.out.println("Please select a valid option");
					}
				}

				// Prompts the user for address data
				System.out.print("\nStreet:\n\t");
				String street = sc.nextLine();

				System.out.print("\nCity:\n\t");
				String city = sc.nextLine();

				System.out.print("\nState:\n\t");
				String state = sc.nextLine();

				System.out.print("\nPrimary Residence(Y/N): ");
				String primary = sc.nextLine();
				boolean isPrimary = false;
				if (primary.equals("yes") || primary.equals("y")) {
					isPrimary = true;
				}

				// Injects the values into the Prepared update statement
				ps1.setString(1, street);
				ps1.setString(2, city);
				ps1.setString(3, state);
				ps1.setBoolean(4, isPrimary);

				// Executes the updates statement
				ps2.executeUpdate();
				ps1.executeUpdate();

			} catch (SQLException e) {
				System.out.println(e);
			}

		} else if (userInput.equals("2") || userInput.equals("add")) {
			final String create = "INSERT INTO Address " + "(street, city, state, primary_residence, employee_id)"
					+ "VALUES( ?, ?, ?, ?, ?)";

			try {
				PreparedStatement ps = con.prepareStatement(create);
				PreparedStatement ps1 = con.prepareStatement(sql);

				ps1.setInt(1, empId);

				// Prompts the user for address data
				System.out.print("\nStreet:\n\t");
				String street = sc.nextLine();

				System.out.print("\nCity:\n\t");
				String city = sc.nextLine();

				System.out.print("\nState:\n\t");
				String state = sc.nextLine();

				System.out.print("\nPrimary Residence(Y/N): ");
				String primary = sc.nextLine();
				boolean isPrimary = false;
				if (primary.equals("yes") || primary.equals("y")) {
					isPrimary = true;
				}

				// MySQL query for address table
				ps.setString(1, street);
				ps.setString(2, city);
				ps.setString(3, state);
				ps.setBoolean(4, isPrimary);
				ps.setInt(5, empId);

				ps1.executeUpdate();
				ps.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}

	static void payrollUpdate(int empId) {

		final String sql = "UPDATE Payroll SET " + "pay_rate = ?, ishourly = ?, isfulltime = ? WHERE employee_id = ?";
		try {
			PreparedStatement ps = con.prepareStatement(sql);

			System.out.print("\nIs the employee hourly(Y/N): ");
			String hourly = sc.nextLine();
			boolean isHourly = false;
			boolean isFulltime = false;
			if (hourly.equals("yes") || hourly.equals("y")) {
				isHourly = true;

				System.out.print("\nIs the employee full time(Y/N): ");
				String fulltime = sc.nextLine();
				if (fulltime.equals("yes") || fulltime.equals("y")) {
					isFulltime = true;
				}
			}

			System.out.print("\nPay Rate:\n\t");
			Double pay = Double.parseDouble(sc.nextLine());

			ps.setDouble(1, pay);
			ps.setBoolean(2, isHourly);
			ps.setBoolean(3, isFulltime);
			ps.setInt(4, empId);

			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static void scheduleUpdate(int empId) {
		final String select = "SELECT * FROM WorkSchedule WHERE employee_id = ?";

		try {
			PreparedStatement ps = con.prepareStatement(select);
			ps.setInt(1, empId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				String mStart = rs.getString("mon_start");
				String mEnd = rs.getString("mon_end");
				System.out.println("Monday:\n\t\t" + mStart + "\t" + mEnd);

				String tueStart = rs.getString("tue_start");
				String tueEnd = rs.getString("tue_end");
				System.out.println("Tuesday:\n\t\t" + tueStart + "\t" + tueEnd);

				String wedStart = rs.getString("wed_start");
				String wedEnd = rs.getString("wed_end");
				System.out.println("Wednesday:\n\t\t" + wedStart + "\t" + wedEnd);

				String thuStart = rs.getString("thur_start");
				String thuEnd = rs.getString("thur_end");
				System.out.println("Thursday:\n\t\t" + thuStart + "\t" + thuEnd);

				String friStart = rs.getString("fri_start");
				String friEnd = rs.getString("fri_end");
				System.out.println("Friday:\n\t\t" + friStart + "\t" + friEnd);

				String satStart = rs.getString("sat_start");
				String satEnd = rs.getString("sat_end");
				System.out.println("Saturday:\n\t\t" + satStart + "\t" + satEnd);

				String sunStart = rs.getString("sun_start");
				String sunEnd = rs.getString("sun_end");
				System.out.println("Sunday:\n\t\t" + sunStart + "\t" + sunEnd);
			}

			System.out.println("\nWhich day would you like to change:\n\t");
			String dow = sc.nextLine().toLowerCase();

			String day = null;
			while (day == null) {
				switch (dow) {
				case "sunday":
					day = "sun";
					break;
				case "monday":
					day = "mon";
					break;
				case "tuesday":
					day = "tue";
					break;
				case "wednesday":
					day = "wed";
					break;
				case "thursday":
					day = "thur";
					break;
				case "friday":
					day = "fri";
					break;
				case "saturday":
					day = "sat";
					break;
				default:
					System.out.println("Please type the day of the week");
				}
			}

			System.out.println("Please use the following format\nyyyy-mm-dd " + "hh:mm:ss\n");
			System.out.print("Start:\n\t");
			String start = sc.nextLine();
			System.out.print("End:\n\t");
			String end = sc.nextLine();

			String dayStart = day + "_start";
			String dayEnd = day + "_end";

			String update = String.format("UPDATE WorkSchedule SET %s = ?, %s = ? WHERE employee_id = ?", dayStart,
					dayEnd);

			PreparedStatement ps1 = con.prepareStatement(update);

			ps1.setString(1, start);
			ps1.setString(2, end);
			ps1.setInt(3, empId);
			ps1.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	static void addEmployee(int depId) {
		String[] sql = {
				"INSERT INTO Employee " + "(first_name, last_name, email, password, phone, position_id," + " status) "
						+ "VALUES( ?, ?, ?, ?, ?, ?, 'Active')",
				"INSERT INTO Address " + "(street, city, state, primary_residence, employee_id)"
						+ "VALUES( ?, ?, ?, ?, ?)",
				"INSERT INTO Payroll " + "(ishourly, isfulltime, pay_rate, employee_id) " + "VALUES(?, ?, ?, ?)",
				"INSERT INTO Performance (rating, employee_id) VALUES(?, ?)" };
		try {
			// Prepared statement object holds the SQL insertion query
			PreparedStatement ps = con.prepareStatement(sql[0]);
			PreparedStatement ps1 = con.prepareStatement(sql[1]);
			PreparedStatement ps2 = con.prepareStatement(sql[2]);
			PreparedStatement ps3 = con.prepareStatement(sql[3]);

			// Prompts the user for employee data
			System.out.print("\nFirst Name:\n\t");
			String fName = sc.nextLine();

			System.out.print("\nLast Name:\n\t");
			String lName = sc.nextLine();

			System.out.print("\nEmail:\n\t");
			String email = sc.nextLine();

			System.out.print("\nPhone:\n\t");
			String phone = sc.nextLine();

			int positionId = 0;
			while (positionId == 0) {
				System.out.print("\nPosition:\n\t");
				String position = sc.nextLine();

				HashMap<String, Integer> positionTable = new HashMap<String, Integer>();
				positionTable.put("Senior Developer", 1);
				positionTable.put("Engineer", 2);
				positionTable.put("Finance Director", 3);
				positionTable.put("Accountant Director", 4);
				positionTable.put("Developer", 5);
				positionTable.put("Accountant", 6);
				positionTable.put("Marketing Lead", 7);
				positionTable.put("HR Director", 8);
				positionTable.put("Operations Manager", 9);
				positionTable.put("CEO", 10);
				positionTable.put("CFO", 11);
				positionTable.put("COO", 12);
				positionTable.put("Project Manager", 13);
				positionTable.put("Scrum Master", 14);
				positionTable.put("Operations Director", 15);
				positionTable.put("Lead Developer", 16);
				positionTable.put("IT Director", 17);
				positionTable.put("Operations Director", 18);
				positionTable.put("Marketing Director", 19);
				positionTable.put("Program Manager", 20);
				positionTable.put("Database Admin", 21);
				positionTable.put("Systems Admin", 22);
				positionTable.put("Technical Support", 23);
				positionTable.put("QA Analyst", 24);

				if (positionTable.containsKey(position)) {
					positionId = positionTable.get(position);
				} else {
					System.out.println("That is not a valid option\nPlease select " + "one from below");
					for (Map.Entry<String, Integer> entry : positionTable.entrySet()) {
						System.out.println(entry.getKey());
					}
				}

			}
			System.out.print("\nPassword:\n\t");
			String passwd = sc.nextLine();
			passwd = Database.sha1Hash(passwd);

			// SQL injection for employee table
			ps.setString(1, fName);
			ps.setString(2, lName);
			ps.setString(3, email);
			ps.setString(4, passwd);
			ps.setString(5, phone);
			ps.setInt(6, positionId);

			ps.executeUpdate();

			// Prompts the user for address data
			System.out.print("\nStreet:\n\t");
			String street = sc.nextLine();

			System.out.print("\nCity:\n\t");
			String city = sc.nextLine();

			System.out.print("\nState:\n\t");
			String state = sc.nextLine();

			System.out.print("\nPrimary Residence(Y/N): ");
			String primary = sc.nextLine();
			boolean isPrimary = false;
			if (primary.equals("yes") || primary.equals("y")) {
				isPrimary = true;
			}

			String selectId = "SELECT MAX(employee_id) as employee_id FROM Employee";
			PreparedStatement stmt = con.prepareStatement(selectId);
			ResultSet rs = stmt.executeQuery();
			int empId = 0;
			while (rs.next()) {
				empId = rs.getInt("employee_id");
			}

			// MySQL query for address table
			ps1.setString(1, street);
			ps1.setString(2, city);
			ps1.setString(3, state);
			ps1.setBoolean(4, isPrimary);
			ps1.setInt(5, empId);

			System.out.print("\nIs the employee hourly(Y/N): ");
			String hourly = sc.nextLine();
			boolean isHourly = false;
			boolean isFulltime = false;
			if (hourly.equals("yes") || hourly.equals("y")) {
				isHourly = true;

				System.out.print("\nIs the employee full time(Y/N): ");
				String fulltime = sc.nextLine();
				if (fulltime.equals("yes") || fulltime.equals("y")) {
					isFulltime = true;
				}
			}

			System.out.print("\nPay Rate:\n\t");
			Double pay = Double.parseDouble(sc.nextLine());

			ps2.setBoolean(1, isHourly);
			ps2.setBoolean(2, isFulltime);
			ps2.setDouble(3, pay);
			ps2.setInt(4, empId);

			// Sets a default rating of 5
			ps3.setInt(1, 5);
			ps3.setInt(2, empId);

			// Executes SQL query
			ps1.executeUpdate();
			ps2.executeUpdate();
			ps3.executeUpdate();

		}

		catch (SQLException e) {

			System.out.println(e);
		}
	}

	static void performanceReport(int depId, int manageId) {
		boolean quit = false;

		final String sql = "INSERT INTO Performance " + "(rating, remarks, employee_id) VALUES (?, ?, ?)";

		try {
			PreparedStatement stmt = con.prepareStatement(sql);

			int empId;
			while (quit == false) {
				System.out.println("\nEmployee Id:\n");
				empId = Integer.parseInt(sc.nextLine());
				String name = Database.displayEmployee(empId, depId, manageId);

				/*
				 * If the user is not in the DB it will return null. prints out a message
				 * accordingly.
				 */
				if (name == null) {
					System.out.println("User not found, make sure they are in your department\nPlease try again "
							+ "or use a different employee ID");
				} else {
					while (quit == false) {
						System.out.println(Database.displayEmployee(empId, depId, manageId));
						System.out.println("Is this the employee?(Y/N):  ");
						String answer = sc.nextLine().toLowerCase();

						if (answer.equals("yes") || answer.equals("y")) {
							stmt.setInt(3, empId);
							quit = true;

							try {
								System.out.println("\nRating 1-10: ");
								int rating = Integer.parseInt(sc.nextLine());
								if (rating > 0 && rating <= 10) {
									stmt.setInt(1, rating);
									quit = true;
								} else {
									System.out.println("\nNot withint the specified range\n" + "Please try again!");
								}
							} catch (NumberFormatException e) {
								System.out.println("\nPlease use a number!");
							}

							System.out.println("\nRemarks:\n");
							String remarks = sc.nextLine();
							stmt.setString(2, remarks);

							stmt.executeUpdate();
							quit = true;
						} else if (answer.equals("no") || answer.equals("n")) {
							System.out.println("Would you like to quit(Y/N): ");

						} else {
							System.out.println("That is an invalid option!" + "\nPlease try again.");
						}

					}
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void readPrompt(int depId, int manageId) {

		boolean quit = false;

		// Prints out a menu for the user to choose from.
		do {
			System.out.println("\nWhat would you like to view?");
			System.out.println("================================= \n");
			System.out.println("Option 1: All employees in department");
			System.out.println("Option 2: Specific employee");
			System.out.println("Option 0: Return to main menu");
			System.out.print("Please choose an option: ");

			// User input is required so that the program knows which action to perform

			String userInput = sc.nextLine().toLowerCase();

			// If nextInt() is used instead of nextLine it will only read the integer and as
			// a result it will make the next nextLine() return a blank string
			// Runs the user input through a series of if/ else statements to determine
			// which action to take

			if (userInput.equals("1") || userInput.equals("all employees")) {

				readDepartment(depId, manageId);
			}

			else if (userInput.equals("2") || userInput.equals("specific")) {

				readEntry(depId, manageId);
			}

			else if (userInput.equals("0") || userInput.equals("quit")) {

				quit = true;
				System.out.println("Returning to the main menu");
			}

			else {

				System.out.println("That is not a valid option\n" + "Please try again!");
			}

			// Continues to print the prompt until either the user quits or another method
			// is called.

		} while (quit != true);
	}

	static void readDepartment(int depId, int manageId) {
		String sql;
		PreparedStatement stmt;

		try {

			if (depId < 7) {
				sql = "SELECT employee_id, " + "concat(first_name, ' ', last_name) AS Name FROM Employee"
						+ " JOIN Positions USING(position_id) WHERE " + "department_id = ? AND management <= ?"
						+ " ORDER BY Name ";
				stmt = con.prepareStatement(sql);
				stmt.setInt(1, depId);
				stmt.setInt(2, manageId);
			} else {
				sql = "SELECT employee_id, " + "concat(first_name, ' ', last_name) AS Name FROM Employee"
						+ " JOIN Positions USING(position_id) ORDER BY Name ";

				stmt = con.prepareStatement(sql);
			}

			ResultSet rs = stmt.executeQuery();

			System.out.println("\n******************************");
			System.out.println("EMPLOYEES");
			System.out.println("=========\n");
			while (rs.next()) {
				int id = rs.getInt("employee_id");
				String name = rs.getString("Name");
				System.out.println(id + "\t\t" + name);
			}
			System.out.println("******************************");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static void readEntry(int depId, int manageId) {
		boolean quit = true;
		String message, header, footer;

		String sql = null;
		PreparedStatement stmt = null;

		try {
			/*
			 * The highest level of management has the ability to view all employees from
			 * any department. Therefore I have it check if the id is lower then seven since
			 * seven is the highest level managers.
			 */
			if (depId < 7) {
				sql = "SELECT concat(first_name, ' ', last_name) AS Name, pay_rate,"
						+ "rating, title, remarks FROM Employee JOIN Positions USING("
						+ "position_id) JOIN Performance USING(employee_id) JOIN " + "Payroll USING(employee_id) "
						+ "WHERE employee_id = ? && department_id = ? && management <= ?";

				stmt = con.prepareStatement(sql);
				stmt.setInt(2, depId);
				stmt.setInt(3, manageId);
			} else {
				sql = "SELECT concat(first_name, ' ', last_name) AS Name, pay_rate,"
						+ "rating, title, remarks FROM Employee JOIN Positions USING("
						+ "position_id) JOIN Performance USING(employee_id)"
						+ "JOIN Payroll USING(employee_id) WHERE employee_id = ?";

				stmt = con.prepareStatement(sql);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		while (quit) {
			// Prompts the user for an employee ID.
			System.out.print("\nWhich employee ID would you like to view: ");

			// Receives the employee ID from the user
			int employeeID = Integer.parseInt(sc.nextLine());

			// The following lines check if the employee is in the managers department, and
			// below them in manager rankings
			String check = Database.displayEmployee(employeeID, depId, manageId);

			if (check == null) {
				System.out.println("User not found, make sure they are in your department\nPlease try again "
						+ "or use a different employee ID");
			} else {

				/*
				 * If nextInt() is used instead of nextLine it will only read the integer and as
				 * a result it will make the next nextLine() return a blank string
				 */

				try {

					stmt.setLong(1, employeeID);

					ResultSet rs = stmt.executeQuery();

					/*
					 * Initializes two strings which will later be used as the header and footer for
					 * the given output.
					 */
					header = String.format("%-20s%-15s%-15s", "Name:", "Pay Rate:", "Position:");

					while (rs.next()) {

						/*
						 * Collects information from the employee info table and prints it out in a user
						 * friendly format.
						 */
						String name = rs.getString("Name");
						Double p = rs.getDouble("pay_rate");
						String pay = df.format(p);
						String position = rs.getString("title");
						String rating = rs.getString("rating");
						String remarks = rs.getString("remarks");

						message = String.format("%-20s%-15s%-15s\n", name, pay, position);

						System.out.println("\n**********************************************");
						System.out.println(header);
						System.out.println(message);
						System.out.printf("Performance Rating: %s\tRemarks:\n%s", rating, remarks);
						System.out.println("\n**********************************************");

					}
				}

				catch (SQLException e) {

					e.printStackTrace();
				}
				quit = false;
			}
		}
	}

	static void deleteEmployee(int depId, int manageId) {

		/*
		 * A method that receives an employee ID from the user and deletes that employee
		 * from both the entire database.
		 */

		boolean quit = false;

		while (quit != true) {

			try {

				/*
				 * Ask the user which row they would like to delete based on the primary key
				 * (employee ID)
				 */
				System.out.println("\nPlease use the employee's Id!");
				System.out.print("Which employee would you like to delete: ");

				// Retrieves the empID from the user.
				String input = sc.nextLine().toLowerCase();

				/*
				 * If nextInt() is used instead of nextLine it will only read the integer and as
				 * a result it will make the next nextLine() return a blank string
				 */

				// An if statement to allow the user to quit if the choose
				if (input.equals("quit") || input.equals("q") || input.equals("0")) {

					System.out.println("Returning to the main menu......");
					quit = true;
				}

				else {

					/*
					 * If the user doesn't want to quit it checks the input for an integer value and
					 * uses that as the employee ID.
					 */
					int id = Integer.parseInt(input);

					// Print out the employee name prior to deleting.
					String name = Database.displayEmployee(id, depId, manageId);

					/*
					 * If the user is not in the DB it will return null. prints out a message
					 * accordingly.
					 */
					if (name == null) {

						System.out.println("\nUser not found, make sure they are in your department\nPlease try again "
								+ "or use a different employee ID");
					}

					else {

						/*
						 * Verify that the user is sure they want to delete the employee.
						 */
						System.out.println(name);
						System.out.println("Are you sure you would like to delete this user?(Y/N): ");
						String y_n = sc.nextLine().toLowerCase();

						if (y_n.equals("yes") || y_n.equals("y")) {

							String[] sql = { "DELETE FROM Retirement WHERE employee_id = ?",
									"DELETE FROM ClockInOut WHERE employee_id = ?",
									"DELETE FROM Attendance WHERE employee_id = ?",
									"DELETE FROM WorkSchedule WHERE employee_id = ?",
									"DELETE FROM DentalPlan WHERE employee_id = ?",
									"DELETE FROM VisionPlan WHERE employee_id = ?",
									"DELETE FROM HealthPlan WHERE employee_id = ?",
									"DELETE FROM Payroll WHERE employee_id = ?",
									"DELETE FROM Performance WHERE employee_id = ?",
									"DELETE FROM ClassAttendance WHERE employee_id = ?",
									"DELETE FROM Address WHERE employee_id = ?",
									"DELETE FROM Employee WHERE employee_id = ?",
									"DELETE FROM Performance WHERE employee_id = ?" };

							try {
								/*
								 * Deletes the employee data in a specified order so that it doesn't delete data
								 * another table is currently referencing.
								 */
								for (int i = 0; i < sql.length; i++) {

									PreparedStatement ps = con.prepareStatement(sql[i]);
									ps.setLong(1, id);
									ps.executeUpdate();
								}
								System.out.println("\nEmployee was deleted sucesfully!\n");
								quit = true;
							}

							catch (SQLException e) {

								System.out.print(e);
								System.out.println("Please try again or " + "type quit if you want to quit\n");
							}
						}

						else if (y_n.equals("n") || y_n.equals("no")) {

							System.out.println("\nThe employee was not deleted");
							System.out.println("Please try again or " + "type quit if you want to quit\n");
						}

						else {

							System.out.println(
									"\nInvalid option!\n " + "Please either type y or n " + "\nor type quit\n");
						}
					}
				}
			}

			catch (NumberFormatException e) {

				System.out.println("\nThat is not an employee ID!");
				System.out.println("Please try again or " + "type quit if you want to quit\n");
			}
		}

	}

	static void empRequest(int empId, int depId, int manageId) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

		try {
			String sql;
			PreparedStatement ps;

			if (depId < 7) {
				sql = "SELECT request_details, date_of_request, is_request_open, request_type, CONCAT(first_name, ' ', last_name) AS name, requests_id FROM Requests "
						+ "JOIN RequestType USING(request_type_id) "
						+ "INNER JOIN employee ON Requests.requesting_employee = Employee.employee_id "
						+ "JOIN Positions USING(position_id) WHERE management <= ? && department_id = ?";
				ps = con.prepareStatement(sql);
				ps.setInt(2, depId);
			} else {
				sql = "SELECT request_details, date_of_request, is_request_open, request_type, CONCAT(first_name, ' ', last_name) AS name, requests_id FROM Requests "
						+ "JOIN RequestType USING(request_type_id) "
						+ "INNER JOIN employee ON Requests.requesting_employee = Employee.employee_id "
						+ "JOIN Positions USING(position_id) WHERE management <= ?";
				ps = con.prepareStatement(sql);
			}

			ps.setInt(1, manageId);
			ResultSet rs = ps.executeQuery();
			String details, requestDate, name, type;
			boolean requestOpen;
			while (rs.next()) {
				int reqId = rs.getInt("requests_id");
				details = rs.getString("request_details");
				requestDate = rs.getString("date_of_request");
				name = rs.getString("name");
				requestOpen = rs.getBoolean("is_request_open");
				type = rs.getString("request_type");

				// Prints out two menus, one for open request, and one for past request
				if (requestOpen) {
					System.out.println("\n************************");
					System.out.println("Active Request");
					System.out.println("===============\n");
					System.out.printf("%-20s%-25s%-20s\n", "Name:", "Date of Request", "Request Type");
					System.out.printf("%-20s%-25s%-20s\n", name, requestDate, type);
					System.out.println("Details:");
					System.out.println(details);
					System.out.println("\n************************");

					// Update the request in the DB
					String update = "UPDATE Requests SET is_request_open = 0, responding_manager = ?, date_of_response = ?, response_details = ? WHERE requests_id = ? ";
					PreparedStatement stmt = con.prepareStatement(update);
					stmt.setInt(1, empId);
					stmt.setInt(4, reqId);
					// Gets the current time and date and updates it to the db
					LocalDateTime now = LocalDateTime.now();
					String date = dtf.format(now);
					stmt.setString(2, date);
					// Asks the user for the details regarding the request.
					System.out.println(
							"Response, please include a definite answer and other details that may pertain to the request: ");
					String response = sc.nextLine();
					stmt.setString(3, response);

					// Executes the update
					stmt.executeUpdate();
					System.out.println("Response uploaded sucessfully!");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}