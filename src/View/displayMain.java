public class displayMain {

	public void printMainMenu() {
		printMenu();
		return;
	}

	public void printWelcome() {
		System.out.println("Welcome to the ticket viewer");
		return;
	}

	public void printMenu() {
		System.out.println("***************");
		System.out.println("Menu:");
		System.out.print("" + "1.Display All Tickets\n" + "2.Display A Single Ticket\n" + "3.Quit");
		System.out.println("\n***************");
		System.out.println("Please select a menu option: ");
		return;
	}

	public void quit() {
		System.out.println("Quiting" + "\nThank you");
	}

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

	public void invalidPageSwitchInput() {
		System.out.println("ERROR: Invalid input, type n for next, b for back, menu for menu, q for quit:");
	}

}