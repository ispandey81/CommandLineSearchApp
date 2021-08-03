import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Util {

    private static final String searchOptions = "Welcome to Zendesk Search\nType 'quit to exit at any time, Press 'Enter' to continue\n\n\t\tSelect search options:\n\t\t* Press 1 to search Zendesk\n\t\t* Press 2 to view a list of searchable fields\n\t\t* Type 'quit' to exit\n\n";

    public static void printHelp() {
        System.out.format("%s", searchOptions);
    }

    public static void readFile() throws IOException
    {
        String fileName = "data/users.json";
        ClassLoader classLoader = Util.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {

            String result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
            System.out.println(result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
