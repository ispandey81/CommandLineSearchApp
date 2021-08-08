package utils;

import exceptions.SearchException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Objects;

public class CommonUtil {

    private CommonUtil() { throw new IllegalStateException("Utility class"); }

    private static final String[] SEARCH_OPERATIONS_REQUIRING_CONVERSION = {"created_at", "verified", "assignee_id"};

    private static final String TERMINATE_PROGRAM_KEYWORD = "quit";

    public static final String USER_SEARCH = "users";

    public static final String TICKET_SEARCH = "tickets";

    public static boolean quitToExit(String inputToCheck) {
        return inputToCheck.equals(TERMINATE_PROGRAM_KEYWORD);
    }

    public static void printFormattedText(String text) {
        System.out.printf(text);
    }

    /**
     * @param searchTerm
     * @param searchValue
     * @return a search value object converted based on the specified search term
     * @throws SearchException
     */
    public static Object convertSearchValue(String searchTerm, String searchValue) throws SearchException {
        if(Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue is null");
        } else {
            try {
                boolean validSearchTerm = Arrays.stream(SEARCH_OPERATIONS_REQUIRING_CONVERSION).anyMatch(s -> s.equals(searchTerm));
                if (validSearchTerm && !Objects.equals(searchValue, "null")) {
                    switch (searchTerm) {
                        case "verified":
                            return Boolean.parseBoolean(searchValue);
                        case "created_at":
                            // Assuming the date provided is in ISO 8601 Time zone format otherwise
                            // an exception will be thrown
                            return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX").parse(searchValue);
                        case "assignee_id":
                            return Integer.parseInt(searchValue);
                        default:
                            return searchValue;
                    }
                } else if(Objects.equals(searchValue, "null")) {
                    return null;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return searchValue;
    }
}
