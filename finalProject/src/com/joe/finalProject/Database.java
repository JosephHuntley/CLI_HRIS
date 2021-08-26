package com.paulnchinnam.finalProject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {

	static Connection con() {
		// Establish database connection
		String url = "jdbc:mysql://localhost:3306/hris";
		String user = "root";
		String password = "admin";

		/*
		 * Declaring variable since java cannot return an undeclared. If an error occurs
		 * prior to assigning the connection variable the program would crash because
		 * java cannot return an empty variable
		 */
		Connection connection = null;

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, user, password);
		}

		catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		catch (SQLException throwables) {

			throwables.printStackTrace();
		}
		return connection;
	}

	public static boolean UserLogin() {
		Scanner sc = new Scanner(System.in);
		boolean quit = false;

		System.out.println("Hello, enter 'login' to continue or 'quit' to exit application.");
		String res = sc.nextLine().toLowerCase();
		System.out.println("\n \n \n \n  ");

		if (res.equals("login")) {
			System.out.println("HRIS LOGIN PORTAL");
			System.out.println("=================");

			System.out.println("Enter your employee ID: ");
			int employeeId = Integer.parseInt(sc.nextLine());

			System.out.println("\n");
			System.out.println("Enter your password: ");
			String employeePass = sc.nextLine();
			employeePass = sha1Hash(employeePass);

			System.out.println("\n");
			System.out.println("Login? Y/N ");
			String submit = sc.nextLine().toLowerCase();

			if (submit.equals("yes") || submit.equals("y")) {

				try {

					Connection con = con();

					String sql = "SELECT first_name, password, management, status FROM Employee JOIN positions USING(position_id) WHERE employee_id = ?;";

					PreparedStatement ps = con.prepareStatement(sql);

					ps.setInt(1, employeeId);

					ResultSet rs = ps.executeQuery();

					String dbEmployeePass = null;
					int management = 0;
					String employeeStatus = null;

					while (rs.next()) {

						management = rs.getInt("management");
						employeeStatus = rs.getString("status");
						dbEmployeePass = rs.getString("password");

					}

					if (employeeStatus.equals("Active") && employeePass.equals(dbEmployeePass)) {

						if (management > 0) {

							quit = managerPrompt(employeeId);
						}

						else {

							quit = employeePrompt(employeeId);
						}
					}

					else if (!employeeStatus.equals("Active") && employeePass.equals(dbEmployeePass)) {

						System.out.println("YOU ARE NOT AN ACTIVE EMPLOYEE \nPLEASE CHANGE YOUR STATUS");
					}

					else if (employeeStatus.equals("Active") && !employeePass.equals(dbEmployeePass)) {

						System.out.println("INCORRECT PASSWORD \nPLEASE TRY AGAIN");
					}

					else {

						System.out.print("INVALID CREDENTIALS");
					}
				}

				catch (SQLException e) {

					e.printStackTrace();
				}
			}

			else {

				return quit = true;
			}
		} else if (res.equals("quit"))
			return quit = true;
		else {
			System.out.println("invalid input try again.");
		}
		return quit;
	}

	static boolean managerPrompt(int id) {
		Connection con = con();
		final String sql = "SELECT department_id, management FROM Employee JOIN "
				+ "Positions USING(position_id) WHERE " + "employee_id = ?;";
		int depId = 0;
		int manageId = 1;
		try {
			PreparedStatement stmt = con.prepareStatement(sql);
			stmt.setInt(1, id);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				depId = rs.getInt("department_id");
				manageId = rs.getInt("management");

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		boolean quit = false;
		Scanner sc = new Scanner(System.in);
		String name = "";
		while (quit == false) {

			// update name in welcome prompt if it is changed
			String sql1 = "SELECT first_name FROM Employee WHERE employee_id = ?";
			try {
				PreparedStatement pst = con.prepareStatement(sql1);
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();

				while (rs.next()) {
					name = rs.getString("first_name");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			// *Print out options for the user to choose from.
			System.out.println("\n \n \n \n  ");
			System.out.println("Welcome " + name + ", what would you like to do?");
			System.out.println("============================================= \n");

			System.out.println("\nManager Functions");
			System.out.println("Option 1: Create a new employee");
			System.out.println("Option 2: View an employee");
			System.out.println("Option 3: Update an employee");
			System.out.println("Option 4: Delete an employee");
			System.out.println("Option 5: Write a preformace report");
			System.out.println("Option 6: Read employee request");

			System.out.println("\n\nEmployee Functions");
			System.out.println("Option 7: View profile information");
			System.out.println("Option 8: Update profile information");
			System.out.println("Option 9: View performance");
			System.out.println("Option 10: View health benefits");
			System.out.println("Option 11: View latest paystub");
			System.out.println("Option 12: Clock In/Out");
			System.out.println("Option 13: View Schedule");
			System.out.println("Option 14: Retirement");
			System.out.println("Option 15: Requests");
			System.out.println("Option 0: Logout");

			// * Receive user input of which database to connect to.
			System.out.print("Please select an option: ");
			String userInput = sc.nextLine().toLowerCase();
			// * Ensure that both Create or CrEaTe will work for option 1.

			switch (userInput) {

			case "1":
			case "Create":
				ManagerPrivileges.addEmployee(depId);
				break;

			case "2":
			case "view employee":
				ManagerPrivileges.readPrompt(depId, manageId);
				break;

			case "3":
			case "update employee":
				ManagerPrivileges.updatePrompt(depId, manageId);
				break;

			case "4":
			case "delete":
				ManagerPrivileges.deleteEmployee(depId, manageId);
				break;

			case "5":
				ManagerPrivileges.performanceReport(depId, manageId);
				break;

			case "6":
				ManagerPrivileges.empRequest(id, depId, manageId);
				break;

			case "7":
			case "view profile":
				ManagerPrivileges.viewProfile(id);
				break;

			case "8":
			case "update profile":
				ManagerPrivileges.updateProfile(id);
				break;

			case "9":
			case "view performance":
				ManagerPrivileges.viewPerformance(id);
				break;

			case "10":
			case "view benefits":
				EmployeePrivileges.viewBenefits(id);
				break;

			case "11":
			case "view paystub":
				EmployeePrivileges.viewPaystub(id);
				break;

			case "12":
				System.out.println("option 6 selected");
				ManagerPrivileges.clockInOut(id);
				break;
			
			case "13":
				System.out.println("option 7 selected");
				ManagerPrivileges.viewSchedule(id);
				break;
			
			case "14":
				System.out.println("option 8 selected");
				ManagerPrivileges.viewRetirement(id);
				break;
			
			case "15":
				System.out.println("option 9 selected");
				ManagerPrivileges.handleRequests(id);
				break;

			case "0":
				quit = true;
				sc.close();
				System.out.println("\nProgram is quiting................\n");
				break;

			default:
				System.out.println("\nThat is not a valid option.\nPlease use " + "the option number");
			}
		}
		return quit;
	}

	static boolean employeePrompt(int id) {
		Scanner sc = new Scanner(System.in);
		// while loop and boolean allows for user to return to main menu
		// after selecting different options until logout is selected
		String name = "";
		boolean inMainMenu = true;
		while (inMainMenu) {

			// update name in welcome prompt if it is changed
			String sql = "SELECT first_name FROM Employee WHERE employee_id = ?";
			try {
				Connection con = con();
				PreparedStatement pst = con.prepareStatement(sql);
				pst.setInt(1, id);
				ResultSet rs = pst.executeQuery();

				while (rs.next()) {
					name = rs.getString("first_name");
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}

			System.out.println("\n \n \n \n  ");
			System.out.println("Welcome " + name + ", what would you like to do?");
			System.out.println("============================================= \n");
			System.out.println("Option 1: View profile information");
			System.out.println("Option 2: Update profile information");
			System.out.println("Option 3: Performance");
			System.out.println("Option 4: Health benefits");
			System.out.println("Option 5: Paystub");
			System.out.println("Option 6: Clock In/Out");
			System.out.println("Option 7: Schedule");
			System.out.println("Option 8: Retirement");
			System.out.println("Option 9: Requests");
			System.out.println("Option 0: Logout");

			System.out.println("\n ");
			System.out.print("Please select an option: ");
			int selection = Integer.parseInt(sc.nextLine());

			switch (selection) {

			case 1:
				System.out.println("option 1 selected");
				EmployeePrivileges.viewProfile(id);
				break;
			case 2:
				System.out.println("option 2 selected");
				EmployeePrivileges.updateProfile(id);
				break;
			case 3:
				System.out.println("option 3 selected");
				EmployeePrivileges.viewPerformance(id);
				break;
			case 4:
				System.out.println("option 4 selected");
				EmployeePrivileges.viewBenefits(id);
				break;
			case 5:
				System.out.println("option 5 selected");
				EmployeePrivileges.viewPaystub(id);
				break;
			case 6:
				System.out.println("option 6 selected");
				EmployeePrivileges.clockInOut(id);
				break;
			case 7:
				System.out.println("option 7 selected");
				EmployeePrivileges.viewSchedule(id);
				break;
			case 8:
				System.out.println("option 8 selected");
				EmployeePrivileges.viewRetirement(id);
				break;
			case 9:
				System.out.println("option 9 selected");
				EmployeePrivileges.handleRequests(id);
				break;
			case 0:

				// *Sets inMainMenu to false to exit.
				inMainMenu = false;
				System.out.println("\n \n \n \n \n \n \n \n ");
				System.out.println("***********************");
				System.out.println("Goodbye " + name);
				System.out.println("***********************");
				System.out.println("\n \n \n \n \n \n \n \n ");
				break;
			default:
				System.out.println("invalid selection");
			}
		}
		return inMainMenu;
	}

	public static String sha1Hash(String input) {

		try {

			// getInstance() method is called with algorithm SHA-1
			MessageDigest md = MessageDigest.getInstance("SHA-1");

			// digest() method is called
			// to calculate message digest of the input string
			// returned as array of byte
			byte[] messageDigest = md.digest(input.getBytes());

			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);

			// Convert message digest into hex value
			String hashtext = no.toString(16);

			// Add preceding 0s to make it 32 bit
			while (hashtext.length() < 40) {

				hashtext = "0" + hashtext;
			}
			// return the HashText
			return hashtext;
		}
		// For specifying wrong message digest algorithms
		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String displayEmployee(int id, int depId, int manageId) {
		Connection con = con();

		// Used to display an employee name based on the Employee ID.
		try {
			PreparedStatement ps;
			if (depId == 7) {
				String sql = "SELECT first_name, last_name FROM Employee WHERE employee_id = ?";
				ps = con.prepareStatement(sql);

			} else {
				String sql = "SELECT first_name, last_name, department_id FROM Employee JOIN Positions USING(position_id) WHERE employee_id = ? AND department_id = ? AND management <= ?;";
				ps = con.prepareStatement(sql);
				ps.setInt(2, depId);
				ps.setInt(3, manageId);
			}

			ps.setLong(1, id);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {

				/*
				 * Retrieves the users first and last name and formats it. Returns the formated
				 * string
				 */
				String fName = rs.getString("first_name");
				String lName = rs.getString("last_name");
				String name = String.format("\n%s %s\n", fName, lName);

				return name;
			}
		}

		catch (SQLException e) {

			return "Error locating employee\nPlease try again\n";
		}

		/*
		 * Java requires that every method returns a specified variable because the try
		 * block may fail the return statement may not always get executed.
		 */
		return null;
	}
}