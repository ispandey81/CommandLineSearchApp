import exceptions.SearchException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pojo.Ticket;
import pojo.User;
import utils.SearchUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SearchUtilTest {

    private static final String USERS_FILE_NAME = "data/users.json";
    private static final String TICKETS_FILE_NAME = "data/tickets.json";

    @Test
    @DisplayName("Test findTickets function")
    void testFindTickets() throws SearchException, ParseException {
        Map<Ticket, String> foundTickets = SearchUtil.findTickets("assignee_id", "71", USERS_FILE_NAME, TICKETS_FILE_NAME);
        Ticket expectedTicket = new Ticket("8ea53283-5b36-4328-9a78-f261ee90f44b",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2016-03-07T03:00:54-11:00"),
                "task", "A Catastrophe in Sierra Leone", 71, List.of("Washington","Wyoming","Ohio","Pennsylvania"));
        assertAll("Querying the found tickets",
                () -> assertEquals(1, foundTickets.size()),
                () -> assertTrue(foundTickets.containsValue("Prince Hinton")),
                () -> assertTrue(foundTickets.containsKey(expectedTicket)));
    }

    @Test
    @DisplayName("Test findTickets function for missing value search")
    void testFindTicketsForMissingValueSearch() throws SearchException, ParseException {
        // Checking missing assignee_id
        Map<Ticket, String> foundTicketsMissingAssignee = SearchUtil.findTickets("assignee_id", "null", USERS_FILE_NAME, TICKETS_FILE_NAME);
        // Checking missing type
        Map<Ticket, String> foundTicketsMissingType = SearchUtil.findTickets("type", "null", USERS_FILE_NAME, TICKETS_FILE_NAME);
        Ticket expectedTicketForMissingAssignee = new Ticket("c68cb7d7-b517-4d0b-a826-9605423e78c2",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2016-03-09T01:39:48-11:00"),
                "task", "A Problem in Western Sahara", null, List.of("Massachusetts","New York","Minnesota","New Jersey"));
        Ticket expectedTicketForMissingType = new Ticket("49a3526c-2bc4-45b0-a6dd-6a55e5a4bd9f",
                new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2016-02-20T02:55:51-11:00"),
                "task", "A Drama in India", 41, List.of("California","Palau","Kentucky","North Carolina"));
        assertAll("Querying the found tickets for missing values",
                () -> assertEquals(4, foundTicketsMissingAssignee.size()),
                () -> assertTrue(foundTicketsMissingAssignee.containsValue("")),
                () -> assertTrue(foundTicketsMissingAssignee.containsKey(expectedTicketForMissingAssignee)),
                () -> assertEquals(2, foundTicketsMissingType.size()),
                () -> assertTrue(foundTicketsMissingType.containsKey(expectedTicketForMissingType)),
                () -> assertTrue(foundTicketsMissingType.containsValue("Alvarez Black")));
    }

    @Test
    @DisplayName("Test findUsers function")
    void testFindUsers() throws SearchException, ParseException {
        Map<User, List<String>> foundUsers= SearchUtil.findUsers("_id", "71", USERS_FILE_NAME, TICKETS_FILE_NAME);
        User expectedUser = new User("71", "Prince Hinton", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2016-04-18T11:05:43-10:00"), false);
        assertAll("Querying the found users",
                () -> assertEquals(1, foundUsers.size()),
                () -> assertTrue(foundUsers.containsValue(List.of("A Catastrophe in Sierra Leone"))),
                () -> assertTrue(foundUsers.containsKey(expectedUser)));
    }

    @Test
    @DisplayName("Test findUsers function for missing value search")
    void testFindUsersForMissingValueSearch() throws SearchException, ParseException {
        // Checking missing verified property
        Map<User, List<String>> foundUsers= SearchUtil.findUsers("verified", "null", USERS_FILE_NAME, TICKETS_FILE_NAME);
        User expectedUser = new User("54", "Spence Tate", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse("2016-01-03T02:38:58-11:00"), null);
        assertAll("Querying the found users for missing values",
                () -> assertEquals(2, foundUsers.size()),
                () -> assertTrue(foundUsers.containsValue(List.of("A Problem in South Africa","A Drama in Israel","A Drama in Albania"))),
                () -> assertTrue(foundUsers.containsKey(expectedUser)));
    }
}
