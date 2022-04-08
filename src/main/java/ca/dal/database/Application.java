package ca.dal.database;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.executor.QueryExecutor;
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

    private static final QueryExecutor queryExecutor = new QueryExecutor();


    public static void main(String[] args) {
        QueryExecutor queryExecutor = new QueryExecutor(new Connection("harsh"));


        StorageManager manager = new StorageManager();
        manager.updateRow("user","people", "name", "Harsh", "Me-Harsh");

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
