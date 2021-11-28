package View;


/* This contains code to display relevant messages
*  to the user. All messages are presented as methods,
*  to facilitate easy access acroos the program.
*/
public class displayMain {

	public void printMainMenu() {
		printMenu();
		return;
	}

	//Welcome message to be displayed the very first time the program is run
	public void printWelcome() {
		System.out.println("Welcome to the ticket viewer");
		return;
	}

	//Menu that is displayed to present the user with choices
	public void printMenu() {
		System.out.println("***************");
		System.out.println("Menu:");
		System.out.print("" + "1.Display All Tickets\n" + "2.Display A Single Ticket\n" + "3.Quit");
		System.out.println("\n***************");
		System.out.println("Please select a menu option: ");
		return;
	}

	//Message to displayed to let the user know that the program has terminaated successfully
	public void quit() {
		System.out.println("Quiting" + "\nThank you");
	}

	//Error message whenever the user enters an invalid input
	public void unrecognizedInput() {
		System.out.println("Unrecognized input, please enter valid input: ");
	}

	public void ticketIDRequest() {
		System.out.println("Enter a ticket ID: ");
	}

	public void InvalidTicketInput() {
		System.out.println(
				"ERROR: Invalid ticket ID, please provide valid ticket ID. (or type menu to return to the menu)");
	}

	//Error message when the user enters an invalid input when viewing all tickets page wise
	public void invalidPageSwitchInput() {
		System.out.println("ERROR: Invalid input, type n for next, b for back, menu for menu, q for quit:");
	}

}