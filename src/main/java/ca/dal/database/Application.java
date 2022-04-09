package ca.dal.database;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.QueryParser;
import ca.dal.database.query.executor.QueryExecutor;
import ca.dal.database.security.Authentication;
import ca.dal.database.storage.StorageManager;

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

    private static Authentication authentication = new Authentication();

    public static void main(String[] args) {

        StorageManager.init();

        //authentication.init();

        Connection connection = new Connection("shah");
        connection.setDatabaseName("dalhousie");

        QueryExecutor executor = new QueryExecutor(connection);
        executor.execute(QueryParser.evaluateQuery("delete from students where id=1002;"));
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
