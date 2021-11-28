
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class displayTicket {

	private static int PAGE_LIMIT = 25;

	public int AllTicketDisplay(JSONObject ticketsJSON, int pageNumber) throws JSONException {
		JSONArray ticketsArr = new JSONArray();
		ticketsArr = ticketsJSON.getJSONArray("tickets");
		System.out.println(ticketsArr);

		int ticketCount = ticketsArr.length();
		int pageTotal = ticketCount / PAGE_LIMIT;

		if (pageNumber > pageTotal) {
			pageNumber = 1;
		} else if (pageNumber < 1) {
			pageNumber = pageTotal;
		}

		int ticketsOnPage = 0;
		int offset = (pageNumber - 1) * PAGE_LIMIT;

		for (int i = 0 + offset; i < PAGE_LIMIT + offset; i++) {
			if (ticketsArr.getJSONObject(i).isNull("id")) {
				break;
			}
			printTicket(ticketsArr.getJSONObject(i).getInt("id"), ticketsArr.getJSONObject(i).getString("status"),
					ticketsArr.getJSONObject(i).getString("subject"),
					ticketsArr.getJSONObject(i).getInt("submitter_id"),
					ticketsArr.getJSONObject(i).getString("updated_at"));
			ticketsOnPage++;
		}

		System.out.println("---------------------------------");
		System.out.println("Displaying " + ticketsOnPage + " tickets on Page " + pageNumber + " of " + pageTotal
				+ " \nPress n to go to next page \nPress b to go back to previous page \nType menu to return to Main Menu\nType q or quit to quit");

		return pageNumber;
	}

	public void SingleTicketDisplay(JSONObject ticketsJSON) throws JSONException {
		printTicket(ticketsJSON.getInt("id"), ticketsJSON.getString("status"), ticketsJSON.getString("subject"),
				ticketsJSON.getInt("requester_id"), ticketsJSON.getString("updated_at"));
	}

	public void printTicket(int id, String status, String subject, int submitterID, String updatedAt) {
		System.out.println("Ticket " + id + "\nStatus: " + status + "\nSubject: '" + subject + "'" + "\nSubmitted By:"
				+ submitterID + "\nUpdated At: " + updatedAt);
		System.out.println("---------------------------");
	}

}