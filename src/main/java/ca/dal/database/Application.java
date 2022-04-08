package ca.dal.database;

import ca.dal.database.query.QueryParser;
import ca.dal.database.query.model.QueryModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.logging.LogManager;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }


    public static void main(String[] args) {
        String query = "delete from nishit where customerid=1;";
        QueryModel model = QueryParser.evaluateQuery(query);
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
