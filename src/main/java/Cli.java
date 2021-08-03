import java.util.Scanner;

public class Cli {
    public static void main(String[] args) {
        Util.printHelp();
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
                    Util.readFile();
                }
                System.out.println("in the users block");
            } else if (userInput.equals("2")) {
                System.out.println("in the tickets block");
            } else {
                System.out.println("Invalid search option");
                Util.printHelp();
            }

        } catch (Exception e) {
            Util.printHelp();
            e.printStackTrace();
        }

    }
}
