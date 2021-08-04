import pojo.User;

import java.util.List;
import java.util.Scanner;

public class Cli {
    private static final String searchOptions = "Welcome to Zendesk Search\nType 'quit' to exit at any time, Press 'Enter' to continue\n\n\t\tSelect search options:\n\t\t* Press 1 to search Zendesk\n\t\t* Press 2 to view a list of searchable fields\n\t\t* Type 'quit' to exit\n\n";
    private static final String usersFileName = "data/users.json";
    private static final String ticketsFileName = "data/tickets.json";
    private static final String terminateProgramKeyword = "quit";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Util.printHelp(searchOptions);
            String userInput = scanner.nextLine();
            while (!userInput.equals(terminateProgramKeyword)) {
                userInput = scanner.nextLine();
                if (userInput.equals("1") || userInput.equals("2") || userInput.equals("")) {
                    if (userInput.equals("1") && !userInput.equals(terminateProgramKeyword)) {
                        System.out.println("Select 1) Users or 2) Tickets");
                        userInput = scanner.nextLine();
                        if (userInput.equals("1") && !userInput.equals(terminateProgramKeyword)) {
                            // We are in user search
                            System.out.println("Enter search term");
                            userInput = scanner.nextLine();
                            if (userInput.equals(terminateProgramKeyword)) {
                                break;
                            }
                            String searchTerm = userInput;
                            System.out.println("Enter search value");
                            userInput = scanner.nextLine();
                            if (userInput.equals(terminateProgramKeyword)) {
                                break;
                            }
                            String searchValue = userInput;
                            System.out.printf("Searching users for %s with a value of %s \n", searchTerm, searchValue);
                            List<User> userList = Util.findUser(searchTerm, searchValue, usersFileName);
                            if (userList.isEmpty()) {
                                System.out.println("No results found");
                            } else {
                                userList.stream().forEach(user -> System.out.format("%s", user.toString()));
                            }
                        } else if(userInput.equals("2") && !userInput.equals(terminateProgramKeyword)) {
                            System.out.println("in tickets search");
                        }
                    }
                    if (userInput.equals("2")) {
                        System.out.println("View searchable fields");
                    }
                    if (userInput.equals("")) {
                        //DO NOTHING
                    }
                } else if(userInput.equals(terminateProgramKeyword)) {
                    break;
                } else {
                    System.out.println("Invalid search option");
                    Util.printHelp(searchOptions);
                }

            }
        } catch (Exception e) {
            Util.printHelp(searchOptions);
            e.printStackTrace();
        }

    }
}
