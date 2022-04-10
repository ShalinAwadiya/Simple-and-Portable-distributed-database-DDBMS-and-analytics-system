package ca.dal.database.menu;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.executor.QueryExecutor;

import java.util.Scanner;

import static ca.dal.database.query.QueryParser.evaluateQuery;
import static ca.dal.database.utils.PrintUtils.*;

public class AnalyticsMenu {

    private Connection connection;

    public AnalyticsMenu(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    public void show() {

        while (true) {

            printWithMargin("Select option from the Menu");
            System.out.println("1. Count Queries");
            System.out.println("2. Count Update");
            System.out.println("3. Exit");
            System.out.print("Enter your choice of operation: ");

            Scanner sc = new Scanner(System.in);
            String userInput = sc.nextLine();

            int userChoice = -1;
            try {
                userChoice = Integer.parseInt(userInput);
            } catch (Exception e) {
                error("Exception Occurred" + e);
            }
            switch (userChoice) {
                case 1:
                    printWithMargin("Welcome to analytics mode", "To exit this mode enter \"quit\"");
                    int result = runQuery();
                    if (result == -1) {
                        show();
                    }
                    break;
                case 2:
                    runQuery();
                    show();
                    break;
                case 3:
                    printWithMargin("Good Bye!");
                    return;
                default:
                    error("Incorrect option chosen, Please try Again");
                    continue;
            }

            switch (userChoice) {
                case 1:
                    printWithMargin("Welcome to analytics mode", "To exit this mode enter \"quit\"");
                    runQuery();
                    break;
                case 2:
                    runQuery();
                    break;
                case 3:
                    printWithMargin("Good Bye!");
                    return;
                default:
                    error("Incorrect option chosen, Please try Again");

            }
        }
    }

    /**
     * @return
     */
    private int runQuery() {
        Scanner sc = new Scanner(System.in);
        QueryExecutor executor = new QueryExecutor(getConnection());
        while (true) {
            print("> ");
            String query = sc.nextLine();

            if ("quit".equals(query)) {
                return -1;
            }

            try {
                executor.execute(evaluateQuery(getConnection(), query));
            } catch (Exception e) {
                e.printStackTrace();
                error("Something went wrong, Please try again!");
            }
        }
    }
}
