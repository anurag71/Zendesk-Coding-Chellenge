package Model;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
 * This code is responsible for interacting with the Zendesk API and send that information
 * to mainController. This code is also responsible for formatting the date to a 
 * more user friendly format.
 */

public class connection {
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
	private Scanner sc;

	//Method to get all the tickets
	public JSONObject getAllTickets() throws JSONException {
		System.out.println("Fetching your Ticket(s)...");
		JSONObject ticketsJSON = new JSONObject();
		boolean multi = true;
		ticketsJSON = connectToAPI(multi, "");
		ticketsJSON = formatMultiJSON(ticketsJSON);
		return ticketsJSON;
	}

	//Method to get ticket for a specific ticketID
	public JSONObject getTicketByID(String ticketID) throws JSONException {
		System.out.println("Fetching your Ticket " + ticketID + "...");
		JSONObject ticketsJSON = new JSONObject();
		boolean multi = false;
		ticketsJSON = connectToAPI(multi, ticketID);

		ticketsJSON = formatSingleJSON(ticketsJSON);
		return ticketsJSON;
	}

	// Method that establishes connection to the API and stores the API response in a JSON.
	public JSONObject connectToAPI(boolean multi, String id) {
		
		//Variables to store the agent's conenction information
		String subdomain = "<EnterYourSubdomain>";
		String email_address = "<EnterYourEmail>";
		String password = "<EnterPassword>";
		String stringURL = "";
		if (multi == true) {
			stringURL = "https://" + subdomain + ".zendesk.com/api/v2/tickets.json";
		} else if (multi == false) {
			stringURL = "https://" + subdomain + ".zendesk.com/api/v2/tickets/" + id + ".json";

		}

		String basicAuth = "";
		JSONObject ticketsJSON = new JSONObject();
		URL url;
		try {
			url = new URL(stringURL);
			URLConnection urlConnection = url.openConnection();
			basicAuth = basicAuthentication(email_address, password);
			urlConnection.setRequestProperty("Authorization", basicAuth);
			urlConnection.connect();
			InputStream inputStream = urlConnection.getInputStream();

			sc = new Scanner(inputStream);
			sc = sc.useDelimiter("\\A");
			String result = sc.hasNext() ? sc.next() : "";
			sc.close();
			ticketsJSON = new JSONObject(result);

		} catch (MalformedURLException e) {
			System.out.println("ERROR: Failed connection. Malformed URL.");
			return null;

		} catch (Exception e) {
			System.out.println("ERROR: There was an error connecting to the API.");
			return null;
		}
		return ticketsJSON;
	}

	//Method to encode the agent's credentials
	public String basicAuthentication(String email_address, String password) {
		String basicAuth = "";
		String userAuthentication = email_address + ":" + password;
		basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userAuthentication.getBytes());

		return basicAuth;
	}

	//Method to format all the tickets obtained.
	public JSONObject formatMultiJSON(JSONObject ticketsJSON) throws JSONException {
		JSONArray ticketsArr = new JSONArray();
		ticketsArr = ticketsJSON.getJSONArray("tickets");
		int fetchedID = -1;
		String formattedID = "";
		Date date = new Date();
		String dateStr = "";
		for (int i = 0; i < ticketsArr.length(); i++) {
			try {
				date = dateFormat.parse(ticketsArr.getJSONObject(i).getString("updated_at"));
				dateStr = date.toString();
			} catch (ParseException e) {
				System.out.println(
						"ERROR: There was an issue regarding the last updated date on one of the tickets. Skipping Ticket...");
				continue;
			}
			fetchedID = ticketsArr.getJSONObject(i).getInt("requester_id");
			formattedID = formatRequesterID(fetchedID);
			ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("requester_id", formattedID);
			ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("updated_at", dateStr);

		}
		return ticketsJSON;
	}

	//Method to format the information that needs to be displayed for a single ticket
	public JSONObject formatSingleJSON(JSONObject ticketsJSON) throws JSONException {
		JSONObject ticket = new JSONObject();
		ticket = ticketsJSON.getJSONObject("ticket");

		int fetchedID = -1;
		String formattedrequester = "";
		String formattedAssignee = "";
		int submitter_id;
		int assignee_id;
		String subject = "";
		String status = "";

		submitter_id = ticket.getInt("submitter_id");
		assignee_id = ticket.getInt("assignee_id");
		subject = ticket.getString("subject");
		status = ticket.getString("status");
		formattedrequester = formatRequesterID(submitter_id);
		formattedAssignee = formatRequesterID(assignee_id);

		ticket.put("Submitter ID", formattedrequester);
		ticket.put("Assignee ID", formattedAssignee);
		ticket.put("Subject", subject);
		ticket.put("Status", status);

		return ticket;
	}

	public String formatRequesterID(int submitter_id) {
		String strReqID = "";
		strReqID = Integer.toString(submitter_id);
		strReqID = strReqID.replace("-", "");
		return strReqID;

	}

}