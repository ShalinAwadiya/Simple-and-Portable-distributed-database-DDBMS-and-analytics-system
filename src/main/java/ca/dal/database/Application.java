package ca.dal.database;

import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.LogManager;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }


    public static void main(String[] args) {

        StorageManager manager = new StorageManager();
//        manager.createDatabase("system");
//        manager.createTable("system", new TableMetadataModel("user",
//                List.of(new ColumnMetadataModel("username", "string"),
//                        new ColumnMetadataModel("password", "string"))
//                ));
//
//        manager.createTable("system",  new TableMetadataModel("people",
//                List.of(new ColumnMetadataModel("username", "string"),
//                        new ColumnMetadataModel("password", "string"))
//        ));
//
//        manager.getTableMetadata("system", "user");

        manager.insertRow("system", "user", new RowModel(List.of("harsh", "password")));
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
