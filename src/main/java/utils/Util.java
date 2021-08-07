package utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.SearchException;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.IOUtils;
import pojo.Ticket;
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

    private Util() { throw new IllegalStateException("Utility class"); }

    private static final String[] SEARCH_OPERATIONS_REQUIRING_CONVERSION = {"created_at", "verified", "assignee_id"};

    private static final String TERMINATE_PROGRAM_KEYWORD = "quit";

    public static void printFormattedText(String text) {
        System.out.printf(text);
    }

    public static String readFile(String fileName) throws IOException
    {
        String result = null;
        ClassLoader classLoader = Util.class.getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream(fileName)) {

            result = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static boolean quitToExit(String inputToCheck) {
        return inputToCheck.equals(TERMINATE_PROGRAM_KEYWORD);
    }

    public static void search(Scanner scanner, String userInput, String usersFileName, String ticketsFileName, String searchingUsersOrTickets) {
        try {
            System.out.println("Enter search term");
            userInput = scanner.nextLine();
            if (Util.quitToExit(userInput)) {
                System.exit(0);
            }
            String searchTerm = userInput;
            System.out.println("Enter search value");
            userInput = scanner.nextLine();
            if (Util.quitToExit(userInput)) {
                System.exit(0);
            }
            String searchValue = userInput;
            System.out.printf("Searching %s for '%s' with a value of '%s' %n", searchingUsersOrTickets, searchTerm, searchValue);
            Map<User, List<String>> responseList = findUsers(searchTerm, searchValue, usersFileName, ticketsFileName, searchingUsersOrTickets);
            Map<Ticket, String> ticketsResponseList = findTickets(searchTerm, searchValue, usersFileName, ticketsFileName, searchingUsersOrTickets);
            if (responseList.isEmpty()) {
                System.out.println("No results found");
            } else {
                System.out.format("%d result(s) found%n", responseList.size());
                for (Map.Entry<User,List<String>> entry : responseList.entrySet()) {
                    System.out.printf(entry.getKey() + "tickets = " + entry.getValue() + "%n");
                }
            }
            if (ticketsResponseList.isEmpty()) {
                System.out.println("No results found");
            } else {
                System.out.format("%d result(s) found%n", ticketsResponseList.size());
                for (Map.Entry<Ticket, String> entry : ticketsResponseList.entrySet()) {
                    System.out.printf(entry.getKey() + "assignee_name = " + entry.getValue() + "%n");
                }
            }
        } catch (SearchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Map<Ticket, String> findTickets(String searchTerm, String searchValue, String usersFileName, String ticketsFileName, String searchingUsersOrTickets) throws SearchException {
        if (Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue or fileToSearchFrom is null");
        }
        Map<Ticket, String> searchTicketsResponse = new HashMap<>();
        List<Ticket> foundTickets;
        Set<User> users;
        Set<Ticket> tickets;
        try {
            String usersFileContent = readFile(usersFileName);
            String ticketsFileContent = readFile(ticketsFileName);
            ObjectMapper objectMapper = new ObjectMapper();

            Object convertedObject = convertSearchValue(searchTerm, searchValue);
            Predicate<Ticket> ticketPredicate = ticket -> {
                boolean testPassed = false;
                try {
                    Method method = PropertyUtils.getReadMethod(new PropertyDescriptor(searchTerm, Ticket.class));
                    if (Objects.equals(method.invoke(ticket), convertedObject) || (method.invoke(ticket) instanceof Collection && new ArrayList<>((Collection<?>)method.invoke(ticket)).contains(convertedObject) )) {
                        testPassed = true;
                    }
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return testPassed;
            };
            users = objectMapper.readValue(usersFileContent, new TypeReference<HashSet<User>>() {
            });
            tickets = objectMapper.readValue(ticketsFileContent, new TypeReference<HashSet<Ticket>>() {
            });
            if (searchingUsersOrTickets.equals("tickets")) {
                foundTickets = tickets.stream().filter(ticketPredicate).collect(Collectors.toList());
                for (Ticket ticket: foundTickets) {
                    for (User user: users) {
                        if (Integer.parseInt(user.get_id()) == ticket.getAssignee_id()) {
                                searchTicketsResponse.put(ticket, user.getName());
                        }
                    }
                    // if the relationship was not found add blank assignee for this ticket
                    if (!searchTicketsResponse.containsKey(ticket)) {
                        searchTicketsResponse.put(ticket, "");
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchTicketsResponse;
    }


    public static Map<User, List<String>> findUsers(String searchTerm, String searchValue, String usersFileName, String ticketsFileName, String searchingUsersOrTickets) throws SearchException {
        if (Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue or fileToSearchFrom is null");
        }
        Map<User, List<String>> searchUsersResponse = new HashMap<>();
        List<User> foundUsers;
        Set<User> users;
        Set<Ticket> tickets;
        try {
            String usersFileContent = readFile(usersFileName);
            String ticketsFileContent = readFile(ticketsFileName);
            ObjectMapper objectMapper = new ObjectMapper();

            Object convertedObject = convertSearchValue(searchTerm, searchValue);
            Predicate<User> userPredicate = user -> {
                boolean testPassed = false;
                try {
                    Method method = PropertyUtils.getReadMethod(new PropertyDescriptor(searchTerm, User.class));
                    if (method.invoke(user) != null && method.invoke(user).equals(convertedObject)) {
                        testPassed = true;
                    }
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return testPassed;
            };
            Predicate<Ticket> ticketPredicate = ticket -> {
                boolean testPassed = false;
                try {
                    Method method = PropertyUtils.getReadMethod(new PropertyDescriptor(searchTerm, Ticket.class));
                    if (Objects.equals(method.invoke(ticket), convertedObject) || (method.invoke(ticket) instanceof Collection && new ArrayList<>((Collection<?>)method.invoke(ticket)).contains(convertedObject) )) {
                        testPassed = true;
                    }
                } catch (IntrospectionException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return testPassed;
            };
            users = objectMapper.readValue(usersFileContent, new TypeReference<HashSet<User>>() {
            });
            tickets = objectMapper.readValue(ticketsFileContent, new TypeReference<HashSet<Ticket>>() {
            });
            if (searchingUsersOrTickets.equals("users")) {
                foundUsers = users.stream().filter(userPredicate).collect(Collectors.toList());
                for (User user: foundUsers) {
                    for (Ticket ticket: tickets) {
                        if (Integer.parseInt(user.get_id()) == ticket.getAssignee_id()) {
                            if (searchUsersResponse.containsKey(user)) {
                                searchUsersResponse.get(user).add(ticket.getSubject());
                            } else {
                                List<String> ticketSubjects = new ArrayList<>();
                                ticketSubjects.add(ticket.getSubject());
                                searchUsersResponse.put(user, ticketSubjects);
                            }
                        }
                    }
                    // if the relationship was not found add an empty list of tickets for this user
                    if (!searchUsersResponse.containsKey(user)) {
                        searchUsersResponse.put(user, Collections.emptyList());
                    }
                }
            }
//            else if (searchingUsersOrTickets.equals("tickets")) {
//                foundItems = tickets.stream().filter(ticketPredicate).collect(Collectors.toList());
//                List<Ticket> xyz =foundItems.stream().map(Ticket.class::cast).collect(Collectors.toList());
//                String assignedUser = users.stream()
//                        .filter(user -> xyz.stream().anyMatch(ticket -> Integer.parseInt(user.get_id()) == ticket.getAssignee_id()))
//                        .map(user -> user.getName())
//                        .reduce("", (partialString, element) -> partialString + element);
//                System.out.println(assignedUser);
//
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchUsersResponse;
    }

    private static Object convertSearchValue(String searchTerm, String searchValue) throws SearchException {
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
