import pojo.User;

import java.util.List;
import java.util.Scanner;

public class Cli {
    private static final String searchOptions = "Welcome to Zendesk Search\nType 'quit to exit at any time, Press 'Enter' to continue\n\n\t\tSelect search options:\n\t\t* Press 1 to search Zendesk\n\t\t* Press 2 to view a list of searchable fields\n\t\t* Type 'quit' to exit\n\n";
    private static final String usersFileName = "data/users.json";
    private static final String ticketsFileName = "data/tickets.json";

    public static void main(String[] args) {
        Util.printHelp(searchOptions);
        try {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.nextLine();
            if (userInput.equals("quit")) {
                System.out.println("Exiting the program");
                System.exit(-1);
            } else if (userInput.equals("1")) {
                System.out.println("Select 1) Users or 2) Tickets");
                String searchUsersOrTickets= scanner.nextLine();
                if (searchUsersOrTickets.equals("1")) {
                    System.out.println("Enter search term");
                    String searchTerm = scanner.nextLine();
                    System.out.println("Enter search value");
                    String searchValue = scanner.nextLine();
                    System.out.printf("Searching users for %s with a value of %s \n", searchTerm, searchValue);
                    List<User> userList = Util.findUser(searchTerm, searchValue, usersFileName);
                    if (userList.isEmpty()) {
                        System.out.println("No results found");
                    } else {
                        userList.stream().forEach(user -> System.out.format("%s", user.toString()));
                    }
                }
            } else if (userInput.equals("2")) {
                System.out.println("in the tickets block");
            } else {
                System.out.println("Invalid search option");
                Util.printHelp(searchOptions);
            }

        } catch (Exception e) {
            Util.printHelp(searchOptions);
            e.printStackTrace();
        }

    }
}
