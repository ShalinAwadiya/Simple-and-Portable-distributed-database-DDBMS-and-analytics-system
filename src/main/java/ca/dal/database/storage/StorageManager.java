package ca.dal.database.storage;

import ca.dal.database.storage.model.RowModel;
import ca.dal.database.storage.model.TableMetadataModel;
import ca.dal.database.transaction.TransactionManager;
import ca.dal.database.utils.FileUtils;

import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.dal.database.constant.ApplicationConstants.DOT;
import static ca.dal.database.utils.FileUtils.append;
import static ca.dal.database.utils.FileUtils.write;
import static ca.dal.database.utils.StringUtils.valueOf;

/**
 * @author Harsh Shah
 */
public class StorageManager {

    private static final Logger logger = Logger.getLogger(StorageManager.class.getName());


    private static final String ROOT = "datastore";
    private static final String TABLE_FILE_EXTENSION = DOT + "rows";
    private static final String TABLE_METADATA = DOT + "meta";

    /**
     * @param databaseName
     * @author Harsh Shah
     */
    public void createDatabase(String databaseName){
        FileUtils.createDirectory(ROOT, databaseName);
    }


    /**
     * @param databaseName
     * @param tableName
     * @param metadata
     * @author Harsh Shah
     */
    public void createTable(String databaseName, String tableName, TableMetadataModel metadata){
        FileUtils.createDirectory(ROOT, databaseName, tableName);

        StringBuilder tableBuilder = new StringBuilder(tableName);
        tableBuilder.append(TABLE_FILE_EXTENSION);
        FileUtils.createFile(ROOT, databaseName, tableName, tableBuilder.toString());

        StringBuilder tableMetaBuilder = new StringBuilder(tableName);
        tableMetaBuilder.append(TABLE_METADATA);
        FileUtils.createFile(ROOT, databaseName, tableName, tableMetaBuilder.toString());

        int result = write(metadata.toList(), ROOT, databaseName, tableName, tableMetaBuilder.toString());

        logger.log(Level.INFO, valueOf(result));
    }

    public void insertRow(String databaseName, String tableName, RowModel row){

        StringBuilder tableBuilder = new StringBuilder(tableName);
        tableBuilder.append(TABLE_FILE_EXTENSION);

        int result = append(row.toString(), ROOT, databaseName, tableName, tableBuilder.toString());

    }

}
