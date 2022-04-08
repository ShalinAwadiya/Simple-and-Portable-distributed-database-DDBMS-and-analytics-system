package ca.dal.database;

import ca.dal.database.datamodel.DataModel;
import ca.dal.database.query.QueryParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }
    public static void main(String[] args) throws IOException {
        String query = "delete from nishit where customerid=1;";
        QueryParser.evaluateQuery(query);
        DataModel model=new DataModel();
        model.createERD("USER");
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
