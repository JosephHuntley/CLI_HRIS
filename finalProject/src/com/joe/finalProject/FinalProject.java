package com.paulnchinnam.finalProject;

import java.sql.Connection;

public class FinalProject {

	public static void main(String[] args) {
		
		boolean quit = false;
		
		//Connects to the database and prints out a message if successful.
		Connection connection = Database.con();
		
		if (connection != null) {
			
			System.out.println("Successfully connected to the database! \n \n \n \n \n");
		}
		
		// Prints selection menu
		while (quit != true) {
			
			quit = Database.UserLogin();
			
		}
		
		System.out.println("Application Exited.");
	}
}
