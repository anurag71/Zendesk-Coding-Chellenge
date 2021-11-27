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
 * The APIHandler does as the name suggests, it handles connecting
 * to the Zendesk servers, fetching ticket information, as well as
 * formatting the data.
 */
public class connection {
	// a date format to follow
	private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
	private Scanner sc;

	// Get ALL the tickets
	public JSONObject getAllTickets() throws JSONException {
		// connect to API and get the tickets in JSON format
		System.out.println("Fetching your Ticket(s)...");
		JSONObject ticketsJSON = new JSONObject();
		boolean multi = true;

		// get tickets JSON
		ticketsJSON = connectToAPI(multi, "");

		// format the tickets into neater JSON
		ticketsJSON = formatMultiJSON(ticketsJSON);

		// return the formatted tickets
		return ticketsJSON;
	}

	// Get a SINGULAR ticket by ID
	public JSONObject getTicketByID(String ticketID) throws JSONException {
		// connect to API and get the individual ticket
		System.out.println("Fetching your Ticket " + ticketID + "...");
		JSONObject ticketsJSON = new JSONObject();
		boolean multi = false;

		// get tickets JSON
		ticketsJSON = connectToAPI(multi, ticketID);

		ticketsJSON = formatSingleJSON(ticketsJSON);

		// return the formatted tickets
		return ticketsJSON;
	}

	// connect to the API and handle API issues
	public JSONObject connectToAPI(boolean multi, String id) {

		// connection information
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

		// connecting
		URL url;
		try {
			// initialize url and connection object
			url = new URL(stringURL);
			URLConnection urlConnection = url.openConnection();

			// encode authentication details
			basicAuth = basicAuthentication(email_address, password);

			// set authentication details
			urlConnection.setRequestProperty("Authorization", basicAuth);

			// connect to the URL with authorization
			urlConnection.connect();

			// get the data as an InputStream
			InputStream inputStream = urlConnection.getInputStream();

			sc = new Scanner(inputStream);
			sc = sc.useDelimiter("\\A");
			String result = sc.hasNext() ? sc.next() : "";
			sc.close();

			// cast to a JSON object
			ticketsJSON = new JSONObject(result);

		} catch (MalformedURLException e) {
			System.out.println("ERROR: Failed connection. Malformed URL.");
			return null;

		} catch (Exception e) {
			System.out.println("ERROR: There was an error connecting to the API.");
			return null;
		}

		// return JSON
		return ticketsJSON;
	}

	// encode the user details with basic authentication
	public String basicAuthentication(String email_address, String password) {
		String basicAuth = "";

		// authenticating administration details
		String userAuthentication = email_address + ":" + password;
		basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userAuthentication.getBytes());

		return basicAuth;
	}

	// format the JSON
	public JSONObject formatMultiJSON(JSONObject ticketsJSON) throws JSONException {
		// format the tickets into a display-able format
		JSONArray ticketsArr = new JSONArray();
		ticketsArr = ticketsJSON.getJSONArray("tickets");
		int fetchedID = -1;
		String formattedID = "";

		// DateFormat format = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH':'mm':'ss'Z'",
		// Locale.ENGLISH);
		Date date = new Date();
		String dateStr = "";

		// go over each JSON array object and get data
		for (int i = 0; i < ticketsArr.length(); i++) {
			// format the date of the ticket for display
			try {
				date = dateFormat.parse(ticketsArr.getJSONObject(i).getString("updated_at"));
				dateStr = date.toString();
			} catch (ParseException e) {
				System.out.println(
						"ERROR: There was an issue regarding the last updated date on one of the tickets. Skipping Ticket...");
				continue;
			}

			// format requester_id
			fetchedID = ticketsArr.getJSONObject(i).getInt("requester_id");
			formattedID = formatRequesterID(fetchedID);

			// set formatted data in JSON Object
			ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("requester_id", formattedID);
			ticketsJSON.getJSONArray("tickets").getJSONObject(i).put("updated_at", dateStr);

		}
		return ticketsJSON;
	}

	public JSONObject formatSingleJSON(JSONObject ticketsJSON) throws JSONException {
		// format the tickets into a display-able format
		JSONObject ticket = new JSONObject();
		ticket = ticketsJSON.getJSONObject("ticket");

		int fetchedID = -1;
		String formattedrequester = "";
		String formattedAssignee = "";

		// DateFormat format = new SimpleDateFormat("YYYY-MM-DD'T'mm:ss:SS'Z'",
		// Locale.ENGLISH);
		int submitter_id;
		int assignee_id;
		String subject = "";
		String status = "";

		submitter_id = ticket.getInt("submitter_id");
		assignee_id = ticket.getInt("assignee_id");
		subject = ticket.getString("subject");
		status = ticket.getString("status");

		// format requester_id
		formattedrequester = formatRequesterID(submitter_id);
		formattedAssignee = formatRequesterID(assignee_id);

		// set formatted data in JSON Object
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