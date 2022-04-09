package ca.dal.database;

import ca.dal.database.datamodel.DataModel;
import ca.dal.database.extractor.DataExtract;
import ca.dal.database.query.executor.QueryExecutor;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.logging.LogManager;

import static ca.dal.database.query.QueryParser.evaluateQuery;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }

    private static final QueryExecutor queryExecutor = new QueryExecutor();

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        DataModel model=new DataModel();
        model.createERD("USER");
        DataExtract dataExtract=new DataExtract();
        dataExtract.exportDB("databases");

        do {
            String query = scanner.nextLine();

            if (query.equalsIgnoreCase("q")) {
                break;
            }

            queryExecutor.execute(evaluateQuery(query));
        } while (true);

        scanner.close();
    }

    public static void setup() {
        try {
            InputStream loggerProps = ClassLoader.getSystemResourceAsStream("logger.properties");
            LogManager.getLogManager().readConfiguration(loggerProps);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
