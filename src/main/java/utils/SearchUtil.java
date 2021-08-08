package utils;

import exceptions.SearchException;
import org.apache.commons.beanutils.PropertyUtils;
import pojo.Pair;
import pojo.Ticket;
import pojo.User;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SearchUtil {

    private SearchUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static void search(Scanner scanner, String userInput, String usersFileName, String ticketsFileName, String searchingUsersOrTickets) {
        try {
            System.out.println("Enter search term");
            userInput = scanner.nextLine();
            if (CommonUtil.quitToExit(userInput)) {
                System.exit(0);
            }
            String searchTerm = userInput;
            System.out.println("Enter search value");
            userInput = scanner.nextLine();
            if (CommonUtil.quitToExit(userInput)) {
                System.exit(0);
            }
            String searchValue = userInput;
            System.out.printf("Searching %s for '%s' with a value of '%s' %n", searchingUsersOrTickets, searchTerm, searchValue);
            
            if (searchingUsersOrTickets.equals("users")) {
                Map<User, List<String>> usersResponseList = findUsers(searchTerm, searchValue, usersFileName, ticketsFileName);
                if (usersResponseList.isEmpty()) {
                    System.out.println("No results found");
                } else {
                    System.out.printf("%d result(s) found%n", usersResponseList.size());
                    for (Map.Entry<User,List<String>> entry : usersResponseList.entrySet()) {
                        System.out.printf(entry.getKey() + "tickets = " + entry.getValue() + "%n");
                    }
                }
            } else if(searchingUsersOrTickets.equals("tickets")) {
                Map<Ticket, String> ticketsResponseList = findTickets(searchTerm, searchValue, usersFileName, ticketsFileName);
                if (ticketsResponseList.isEmpty()) {
                    System.out.println("No results found");
                } else {
                    System.out.printf("%d result(s) found%n", ticketsResponseList.size());
                    for (Map.Entry<Ticket, String> entry : ticketsResponseList.entrySet()) {
                        System.out.printf(entry.getKey() + "assignee_name = " + entry.getValue() + "%n");
                    }
                }
            }
        } catch (SearchException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Map<Ticket, String> findTickets(String searchTerm, String searchValue, String usersFileName, String ticketsFileName) throws SearchException {
        if (Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue or fileToSearchFrom is null");
        }
        Map<Ticket, String> searchTicketsResponse = new HashMap<>();
        List<Ticket> foundTickets;
        try {
            Pair pair = FileUtil.convertJsonToPojo(usersFileName, ticketsFileName);
            Object convertedObject = CommonUtil.convertSearchValue(searchTerm, searchValue);
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
            foundTickets = pair.getTickets().stream().filter(ticketPredicate).collect(Collectors.toList());
            for (Ticket ticket: foundTickets) {
                for (User user: pair.getUsers()) {
                    if (Integer.parseInt(user.get_id()) == ticket.getAssignee_id()) {
                        searchTicketsResponse.put(ticket, user.getName());
                    }
                }
                // if the relationship was not found add blank assignee for this ticket
                if (!searchTicketsResponse.containsKey(ticket)) {
                    searchTicketsResponse.put(ticket, "");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchTicketsResponse;
    }


    public static Map<User, List<String>> findUsers(String searchTerm, String searchValue, String usersFileName, String ticketsFileName) throws SearchException {
        if (Objects.isNull(searchTerm) || Objects.isNull(searchValue)) {
            throw new SearchException("searchTerm or searchValue or fileToSearchFrom is null");
        }
        Map<User, List<String>> searchUsersResponse = new HashMap<>();
        List<User> foundUsers;
        try {
            Pair pair = FileUtil.convertJsonToPojo(usersFileName, ticketsFileName);
            Object convertedObject = CommonUtil.convertSearchValue(searchTerm, searchValue);
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
            foundUsers = pair.getUsers().stream().filter(userPredicate).collect(Collectors.toList());
            for (User user: foundUsers) {
                for (Ticket ticket: pair.getTickets()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return searchUsersResponse;
    }
}
