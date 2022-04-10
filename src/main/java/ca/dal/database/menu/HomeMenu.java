package ca.dal.database.menu;

import ca.dal.database.connection.Connection;
import ca.dal.database.datamodel.DataModel;
import ca.dal.database.extractor.DataExtract;
import ca.dal.database.query.executor.QueryExecutor;

import java.io.IOException;
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


    public void show() throws IOException {

        printWithMargin("Select option from the Menu");
        System.out.println("1. Write Queries");
        System.out.println("2. Export");
        System.out.println("3. Data Model");
        System.out.println("4. Analytics");
        System.out.println("5. Exit");
        System.out.print("Enter your choice of operation: ");


        Scanner sc = new Scanner(System.in);
        int userChoice = sc.nextInt();

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
                break;
            case 5:
                printWithMargin("Good Bye!");
                return;
            default:
                error("Incorrect option chosen, Please try Again");
                show();

        }

    }

    /**
     * @author Harsh Shah
     */
    private void exportDatabase() throws IOException {
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
        private void exportDataModel () {
            DataExtract extract = new DataExtract();
            int result = extract.exportDB("datastore");
            if (result == 0) {
                success("Data Model exported successfully!");
            }
        }

        /**
         * @return
         */
        private int runQuery () {
            print("> ");
            Scanner sc = new Scanner(System.in);
            String query = sc.nextLine();

            if ("quit".equals(query)) {
                return -1;
            }

            try {
                QueryExecutor executor = new QueryExecutor(getConnection());
                executor.execute(evaluateQuery(query));
            } catch (Exception e) {
                e.printStackTrace();
                error("Something went wrong, Please try again!");
            }
            runQuery();

            return 0;
        }
    }
