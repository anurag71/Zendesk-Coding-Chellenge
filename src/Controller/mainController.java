package Controller;

import View.*;

import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;
import Model.connection;

/*
 * This code is used to handle user input and accordingly interact with connection.java.
 * The data obtained from conenction.java is passed on to appropriate methods in displayMain
 * and displayTicket to provide the output to the user.
 */
public class mainController {
	displayMain displayMenu;
	displayTicket TicketDisplay;
	private Scanner sc;
	boolean firstTime = true;

	public mainController() {
		// TODO Auto-generated constructor stub
		displayMenu = new displayMain();
		TicketDisplay = new displayTicket();
	}

	public String getInput() {
		sc = new Scanner(System.in);
		String input = "";
		input = sc.next();
		return input;
	}

	public void MainMenu() throws JSONException {
		String input = "";

		while (true) {

			//Display the welcome message only once
			if (firstTime) {
				displayMenu.printWelcome();
				firstTime = false;
			}

			//Display the menu to the user
			displayMenu.printMainMenu();
			input = getInput();
			if (input.contains("1")) {
				displayAllTickets();
			} 
			else if (input.contains("2")) {
				displaySingleTicket();
			} 
			else if (input.contains("3") || input.contains("q")) {
				displayMenu.quit();
				return;
			} 
			else {
				displayMenu.unrecognizedInput();
			}
			input = "";
		}

	}

	public void displayAllTickets() throws JSONException {
		connection conn = new connection();
		String input = "";
		int pageNumber = 1;
		JSONObject ticketsJSON = new JSONObject();

		//Obtain the tickets from the API
		try {
			ticketsJSON = conn.getAllTickets();
		} 
		catch (Exception e) {
			System.out.println("ERROR: Could not successfully get your tickets.");
			return;
		}
		pageNumber = TicketDisplay.AllTicketDisplay(ticketsJSON, pageNumber);

		while (true) {
			input = getInput();


			if (input.contains("menu")) {
				MainMenu();
			}
			else if (input.contains("n")) {
				pageNumber++;
				pageNumber = TicketDisplay.AllTicketDisplay(ticketsJSON, pageNumber);
			} 
			else if (input.contains("b")) {
				pageNumber--;
				pageNumber = TicketDisplay.AllTicketDisplay(ticketsJSON, pageNumber);
			} 
			else if (input.contains("q") || input.contains("quit")) {
				displayMenu.quit();
			} 
			else {
				displayMenu.invalidPageSwitchInput();
			}
			input = "";

		}
	}

	//Obtain the ticekt for the user specified ticketID
	public void displaySingleTicket() throws JSONException {
		String id = "";
		JSONObject ticketsJSON = new JSONObject();

		displayMenu.ticketIDRequest();
		id = getInput();

		connection conn = new connection();
		try {
			ticketsJSON = conn.getTicketByID(id);
		} 
		catch (Exception e) {
			System.out.println("ERROR: Could not find ticket " + id + ". Please check the ID and try again.");
			return;
		}

		TicketDisplay.SingleTicketDisplay(ticketsJSON);
		return;
	}
}