package ca.dal.database.menu;

import ca.dal.database.connection.Connection;
import ca.dal.database.datamodel.DataModel;
import ca.dal.database.extractor.DataExtract;
import ca.dal.database.logger.GeneralLog;
import ca.dal.database.query.executor.QueryExecutor;
import ca.dal.database.utils.MapUtils;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Scanner;

import static ca.dal.database.query.QueryParser.evaluateQuery;
import static ca.dal.database.utils.PrintUtils.*;
import static ca.dal.database.utils.StringUtils.valueOf;

public class HomeMenu {

    private final Connection connection;
    private final AnalyticsMenu analyticsMenu;
    private GeneralLog generalLog;
    /**
     * @param connection
     */
    public HomeMenu(Connection connection) {
        analyticsMenu = new AnalyticsMenu(connection);
        generalLog = new GeneralLog();
        this.connection = connection;
    }

    /**
     * @return connection
     */
    public Connection getConnection() {
        return connection;
    }

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
                e.printStackTrace();
                error("Incorrect option chosen, Please try Again");
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
                    analyticsMenu.show();
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
     * @
     */
    private void exportDataModel() {
        println("Enter Database Name: ");
        Scanner sc = new Scanner(System.in);
        String database = sc.nextLine();

        DataModel model = new DataModel();
        int result = model.createERD(database);

        if (result == 0) {
            success("Entity-Relationship Model of %s is created!", database);
        }
    }

    private void exportDatabase() {
        println("Enter Database Name: ");
        Scanner sc = new Scanner(System.in);
        String database = sc.nextLine();

        DataExtract extract = new DataExtract();
        int result = extract.exportDB(database);

        if (result == 0) {
            success("Data Dump created successfully!");
        }
    }

    /**
     * @return -1
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

                Instant start = Instant.now();
                executor.execute(evaluateQuery(getConnection(), query));
                long timeElapsed = Duration.between(start, Instant.now()).toMillis();

                generalLog.writeLog("Information", "execution_time", "Time taken by query",
                        MapUtils.of("time", valueOf(timeElapsed)+" ms", "username", connection.getUserId()));

            } catch (Exception e) {
                e.printStackTrace();
                error("Something went wrong, Please try again!");
            }
        }
    }
}
