import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.SearchException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import pojo.User;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Util {

    public static final String[] searchOptionsRequiringConversion = {"created_at", "verified", "assignee_id"};

    public static void printHelp(String helpText) {
        System.out.format("%s", helpText);
    }

    public static String readFile(String fileName) throws IOException
    {
        String result = null;
        ClassLoader classLoader = Util.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {

            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static List<User> findUser(String searchTerm, String searchValue, String fileToSearchFrom) throws SearchException {
        if (Objects.isNull(searchTerm) || Objects.isNull(searchValue) || Objects.isNull(fileToSearchFrom)) {
            throw new SearchException("searchTerm or searchValue or fileToSearchFrom is null");
        }
        List<User> foundUsers = new ArrayList<User>();
        try {
            String fileContent = readFile(fileToSearchFrom);
            ObjectMapper objectMapper = new ObjectMapper();
            Set<User> users = objectMapper.readValue(fileContent, new TypeReference<HashSet<User>>() {
            });
            Object convertedObject = convertSearchValue(searchTerm, searchValue);
            Predicate<User> condition = new Predicate<User>()
            {
                @Override
                public boolean test(User user) {
                    boolean testPassed = false;
                    try {
                        Method method = PropertyUtils.getReadMethod(new PropertyDescriptor(searchTerm, User.class));
                        if (method.invoke(user).equals(convertedObject)) {
                            testPassed = true;
                        }
                    } catch (IntrospectionException e) {
                        e.printStackTrace();
                        System.exit(0);
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                        System.exit(0);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        System.exit(0);
                    }
                    return testPassed;
                }
            };
            foundUsers = users.stream().filter(condition).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundUsers;
    }

    private static Object convertSearchValue(String searchTerm, String searchValue) throws SearchException {
        if(Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue is null");
        } else {
            try {
                boolean validSearchTerm = Arrays.stream(searchOptionsRequiringConversion).anyMatch(s -> s.equals(searchTerm));
                if (validSearchTerm) {
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
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return searchValue;
    }
}
