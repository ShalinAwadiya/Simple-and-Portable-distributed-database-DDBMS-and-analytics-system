package ca.dal.database;

import ca.dal.database.security.Authentication;
import ca.dal.database.storage.StorageManager;

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

    private static Authentication authentication = new Authentication();

    public static void main(String[] args) {
        StorageManager.init();
        authentication.init();
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
