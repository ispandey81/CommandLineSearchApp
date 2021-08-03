import java.util.Scanner;

public class Cli {

    private static final String searchOptions = "Welcome to Zendesk Search\nType 'quit to exit at any time, Press 'Enter' to continue\n\n\t\tSelect search options:\n\t\t* Press 1 to search Zendesk\n\t\t* Press 2 to view a list of searchable fields\n\t\t* Type 'quit' to exit\n\n";

    private static void printHelp() {
        System.out.format("%s", searchOptions);
    }
    public static void main(String[] args) {
        printHelp();
        try {
            Scanner scanner = new Scanner(System.in);
            String userInput = scanner.next();
            if (userInput.equals("quit")) {
                System.out.println("Exiting the program");
                System.exit(-1);
            } else if (userInput.equals("1")) {
                System.out.println("Select 1) Users or 2) Tickets");
                String searchUsersOrTickets= scanner.next();
                if (searchUsersOrTickets.equals("1")) {
                    System.out.println("Enter search term");
                    String searchTerm = scanner.next();
                    System.out.println("Enter search value");
                    String searchValue = scanner.next();
                    System.out.printf("Searching users for %s with a value of %s ", searchTerm, searchValue);
                }
                System.out.println("in the users block");
            } else if (userInput.equals("2")) {
                System.out.println("in the tickets block");
            } else {
                System.out.println("Invalid search option");
                printHelp();
            }

        } catch (Exception e) {
            printHelp();
            e.printStackTrace();
        }

    }
}
