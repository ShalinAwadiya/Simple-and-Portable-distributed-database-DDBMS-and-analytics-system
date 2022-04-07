package ca.dal.database;

import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.ColumnMetadataModel;
import ca.dal.database.storage.model.RowModel;
import ca.dal.database.storage.model.TableMetadataModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.logging.LogManager;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }


    public static void main(String[] args) {}

    public static void setup() {
        try {
            InputStream loggerProps = ClassLoader.getSystemResourceAsStream("logger.properties");
            LogManager.getLogManager().readConfiguration(loggerProps);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
