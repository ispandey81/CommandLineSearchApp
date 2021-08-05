import pojo.User;

import java.util.List;
import java.util.Scanner;

public class Cli {
    private static final String searchOptions = "Welcome to Zendesk Search\nType 'quit' to exit at any time, Press 'Enter' to continue\n\n\t\tSelect search options:\n\t\t* Press 1 to search Zendesk\n\t\t* Press 2 to view a list of searchable fields\n\t\t* Type 'quit' to exit\n\n";
    private static final String searchableFields = "-------------------------------------\nSearch Users with\n_id\nname\ncreated_at\nverified\n-------------------------------------\nSearch Tickets with\n_id\ncreated_at\ntype\nsubject\nassignee_id\ntags\n";
    private static final String usersFileName = "data/users.json";
    private static final String ticketsFileName = "data/tickets.json";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Util.printFormattedText(searchOptions);
            String userInput = scanner.nextLine();
            while (!Util.quitToExit(userInput)) {
                userInput = scanner.nextLine();
                if (userInput.equals("1") || userInput.equals("2") || userInput.equals("")) {
                    if (!Util.quitToExit(userInput) && userInput.equals("1")) {
                        System.out.println("Select 1) Users or 2) Tickets");
                        userInput = scanner.nextLine();
                        if (!Util.quitToExit(userInput) && userInput.equals("1")) {
                            // We are in user search
                            Util.search(scanner, userInput, usersFileName);
                        } else if(!Util.quitToExit(userInput) && userInput.equals("2")) {
                            Util.search(scanner, userInput, ticketsFileName);
                        } else if (userInput.equals("")) {
                            //DO NOTHING
                        } else if(Util.quitToExit(userInput)) {
                            break;
                        } else {
                            System.out.println("Invalid search option");
                            Util.printFormattedText(searchOptions);
                        }
                    } else if (userInput.equals("2")) {
                        Util.printFormattedText(searchableFields);
                    } else if (userInput.equals("")) {
                        //DO NOTHING
                    }
                } else if(Util.quitToExit(userInput)) {
                    break;
                } else {
                    System.out.println("Invalid search option");
                    Util.printFormattedText(searchOptions);
                }

            }
        } catch (Exception e) {
            Util.printFormattedText(searchOptions);
            e.printStackTrace();
        }

    }
}
