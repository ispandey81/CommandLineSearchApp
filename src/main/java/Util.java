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
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Util {

//    public static String[] userSearchOptions =

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
            Predicate<User> condition = new Predicate<User>()
            {
                @Override
                public boolean test(User user) {
                    boolean testPassed = false;
                    try {
                        Method method = PropertyUtils.getReadMethod(new PropertyDescriptor(searchTerm, User.class));
                        System.out.println(method.getName());
                        if (method.invoke(user).toString().equals(searchValue)) {
                            testPassed = true;
                        }
                    } catch (IntrospectionException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return testPassed;
                }
            };
            foundUsers = users.stream().filter(condition).collect(Collectors.toList());
//            System.out.println(foundUsers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundUsers;
    }
}
