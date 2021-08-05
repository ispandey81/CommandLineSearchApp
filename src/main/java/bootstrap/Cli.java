package bootstrap;

import utils.Util;

import java.util.Scanner;

public class Cli {
    private static final String SEARCH_OPTIONS = "Welcome to Zendesk Search%nType 'quit' to exit at any time, Press 'Enter' to continue%n%n\t\tSelect search options:%n\t\t* Press 1 to search Zendesk%n\t\t* Press 2 to view a list of searchable fields%n\t\t* Type 'quit' to exit%n%n";
    private static final String SEARCHABLE_FIELDS = "-------------------------------------%nSearch Users with%n_id%nname%ncreated_at%nverified%n-------------------------------------%nSearch Tickets with%n_id%ncreated_at%ntype%nsubject%nassignee_id%ntags%n";
    private static final String USERS_FILE_NAME = "data/users.json";
    private static final String TICKETS_FILE_NAME = "data/tickets.json";
    private static final String DISPLAY_AVAILABLE_SELECTIONS = "Select 1) Users or 2) Tickets";
    private static final String INVALID_SEARCH_OPTION = "Invalid search option";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            Util.printFormattedText(SEARCH_OPTIONS);
            String userInput = scanner.nextLine();
            while (!Util.quitToExit(userInput)) {
                userInput = scanner.nextLine();
                if (userInput.equals("1") || userInput.equals("2") || userInput.equals("")) {
                    if (!Util.quitToExit(userInput) && userInput.equals("1")) {
                        System.out.println(DISPLAY_AVAILABLE_SELECTIONS);
                        userInput = scanner.nextLine();
                        if (!Util.quitToExit(userInput) && userInput.equals("1")) {
                            // We are in user search
                            Util.search(scanner, userInput, USERS_FILE_NAME);
                        } else if(!Util.quitToExit(userInput) && userInput.equals("2")) {
                            Util.search(scanner, userInput, TICKETS_FILE_NAME);
                        } else if (userInput.equals("")) {
                            //DO NOTHING
                        } else if(Util.quitToExit(userInput)) {
                            System.exit(0);
                        } else {
                            System.out.println(INVALID_SEARCH_OPTION);
                            Util.printFormattedText(SEARCH_OPTIONS);
                        }
                    } else if (userInput.equals("2")) {
                        Util.printFormattedText(SEARCHABLE_FIELDS);
                    } else if (userInput.equals("")) {
                        //DO NOTHING
                    }
                } else if(Util.quitToExit(userInput)) {
                    System.exit(0);
                } else {
                    System.out.println(INVALID_SEARCH_OPTION);
                    Util.printFormattedText(SEARCH_OPTIONS);
                }

            }
        } catch (Exception e) {
            Util.printFormattedText(SEARCH_OPTIONS);
            e.printStackTrace();
        }

    }
}
