import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import pojo.Pair;
import utils.FileUtil;

import java.io.IOException;

class FileUtilTest {

    private static final String USERS_FILE_NAME = "data/users.json";
    private static final String TICKETS_FILE_NAME = "data/tickets.json";

    @Test
    @DisplayName("Read contents of a file as a string")
    void testReadFile() throws IOException {
        String expectedString = "{\n" +
                "  \"fruit\": \"Apple\",\n" +
                "  \"size\": \"Large\",\n" +
                "  \"color\": \"Red\"\n" +
                "}";
        assertEquals(expectedString, FileUtil.readFile("data/sample.json"));
    }

    @Test
    @DisplayName("Test conversion of json to pojo")
    void testConvertJsonToPojo() {
        Pair pair = FileUtil.convertJsonToPojo(USERS_FILE_NAME, TICKETS_FILE_NAME);
        assertAll("Querying Pair object",
                () -> assertNotNull(pair),
                () -> assertEquals(75, pair.getUsers().size()),
                () -> assertEquals(200, pair.getTickets().size()));
    }
}
