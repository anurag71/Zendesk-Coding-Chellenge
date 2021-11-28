package Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import Model.connection;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;


/*
 * JUnit 4 is used. 
 * Contains the test cases to validate the code.
 */
public class TestCases {

	@Test
	public void testFetchFalseTicketID() {
		String id = "-152353abfsb";
		connection conn = new connection();

		try {
			conn.connectToAPI(false, id);
		} catch (Exception e) {
			assertNotNull(e);
			assertEquals(e.getClass(), Exception.class);
		}
	}

	@Test
	public void testFetchTicketByID() throws JSONException {
		String id = "10";
		connection conn = new connection();
		JSONObject ticket = new JSONObject();

		ticket = conn.getTicketByID(id);
		assertEquals(10, ticket.get("id"));
	}

	@Test
	public void testTicketContent() throws JSONException {
		String id = "1";
		connection conn = new connection();
		JSONObject ticket = new JSONObject();

		ticket = conn.getTicketByID(id);
		assertEquals(1524050750582L, ticket.get("submitter_id"));
	}

	@Test
	public void testFormatSubmitterIDOutput() {
		connection conn = new connection();
		int requester_id = -24724;
		String str = "";

		str = conn.formatRequesterID(requester_id);

		assertEquals(str, "24724");

	}
}
