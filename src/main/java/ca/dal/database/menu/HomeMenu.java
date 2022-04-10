package ca.dal.database.menu;

import ca.dal.database.connection.Connection;
import ca.dal.database.datamodel.DataModel;
import ca.dal.database.extractor.DataExtract;
import ca.dal.database.query.executor.QueryExecutor;

import java.util.Scanner;

import static ca.dal.database.query.QueryParser.evaluateQuery;
import static ca.dal.database.utils.PrintUtils.*;

public class HomeMenu {

    private Connection connection;

    public HomeMenu(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() {
        return connection;
    }

    AnalyticsMenu analyticsMenu = new AnalyticsMenu(connection);

    public void show() {

        while (true) {

            printWithMargin("Select option from the Menu");
            System.out.println("1. Write Queries");
            System.out.println("2. Export");
            System.out.println("3. Data Model");
            System.out.println("4. Analytics");
            System.out.println("5. Exit");
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
                    printWithMargin("Welcome to query executor mode", "To exit this mode enter \"quit\"");
                    int result = runQuery();
                    if (result == -1) {
                        show();
                    }
                    break;
                case 2:
                    exportDatabase();
                    show();
                    break;
                case 3:
                    exportDataModel();
                    show();
                    break;
                case 4:
                    analyticsMenu.show();
                    break;
                case 5:
                    printWithMargin("Good Bye!");
                    return;
                default:
                    error("Incorrect option chosen, Please try Again");
                    continue;
            }

            switch (userChoice) {
                case 1:
                    printWithMargin("Welcome to query executor mode", "To exit this mode enter \"quit\"");
                    runQuery();
                    break;
                case 2:
                    exportDatabase();
                    break;
                case 3:
                    exportDataModel();
                    break;
                case 4:
                    break;
                case 5:
                    printWithMargin("Good Bye!");
                    return;
                default:
                    error("Incorrect option chosen, Please try Again");

            }
        }
    }

    /**
     * @author Harsh Shah
     */
    private void exportDatabase() {
        println("Enter Database Name: ");
        Scanner sc = new Scanner(System.in);
        String database = sc.nextLine();

        DataModel model = new DataModel();
        int result = model.createERD(database);

        if (result == 0) {
            success("Entity-Relationship Model of %s is created!", database);
        }
    }

    /**
     * @author Harsh Shah
     */
    private void exportDataModel() {
        DataExtract extract = new DataExtract();
        int result = extract.exportDB("datastore");
        if (result == 0) {
            success("Data Model exported successfully!");
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
