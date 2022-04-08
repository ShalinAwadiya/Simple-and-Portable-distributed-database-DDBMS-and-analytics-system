package ca.dal.database.storage;

import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;
import ca.dal.database.utils.FileUtils;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.dal.database.constant.ApplicationConstants.DOT;
import static ca.dal.database.utils.FileUtils.*;
import static ca.dal.database.utils.StringUtils.valueOf;

/**
 * @author Harsh Shah
 */
public class StorageManager {

    private static final Logger logger = Logger.getLogger(StorageManager.class.getName());


    private static final String ROOT = "datastore";
    private static final String DATABASE_METADATA = DOT + "meta";
    private static final String TABLE_FILE_EXTENSION = DOT + "rows";
    private static final String TABLE_METADATA = DOT + "meta";

    /**
     * @param databaseName
     * @author Harsh Shah
     */
    public void createDatabase(String databaseName) {

        // Create Database
        FileUtils.createDirectory(ROOT, databaseName);

        // Create Database Metadata
        StringBuilder databaseMetaBuilder = new StringBuilder(databaseName);
        databaseMetaBuilder.append(DATABASE_METADATA);
        FileUtils.createFile(ROOT, databaseName, databaseMetaBuilder.toString());

        DatabaseMetadataModel model = new DatabaseMetadataModel(databaseName);

        // Write Table Metadata
        int result = write(model.toMetaString(), ROOT, databaseName, databaseMetaBuilder.toString());

        logger.log(Level.INFO, valueOf(result));
    }

    /**
     * @param databaseName
     * @param metadataModel
     */
    public void updateDatabaseMetadata(String databaseName, TableMetadataModel metadataModel) {

        DatabaseMetadataModel metadata = getDatabaseMetadata(databaseName);

        metadata.addTableHeaderMetadataModel(metadataModel);

        StringBuilder databaseMetaBuilder = new StringBuilder(databaseName);
        databaseMetaBuilder.append(DATABASE_METADATA);
        write(metadata.toListString(), ROOT, databaseName, databaseMetaBuilder.toString());

    }


    /**
     * @param databaseName
     * @param metadata
     * @author Harsh Shah
     */
    public void createTable(String databaseName, TableMetadataModel metadata) {

        String tableName = metadata.getTableName();

        // Create Table Space
        FileUtils.createDirectory(ROOT, databaseName, tableName);

        // Create Table
        StringBuilder tableBuilder = new StringBuilder(tableName);
        tableBuilder.append(TABLE_FILE_EXTENSION);
        FileUtils.createFile(ROOT, databaseName, tableName, tableBuilder.toString());

        // Create Table Metadata
        StringBuilder tableMetaBuilder = new StringBuilder(tableName);
        tableMetaBuilder.append(TABLE_METADATA);
        FileUtils.createFile(ROOT, databaseName, tableName, tableMetaBuilder.toString());

        // Write Table Metadata
        int result = write(metadata.toStringList(), ROOT, databaseName, tableName, tableMetaBuilder.toString());
        updateDatabaseMetadata(databaseName, metadata);
        logger.log(Level.INFO, valueOf(result));
    }

    /**
     * @param databaseName
     * @param metadataModel
     */
    public void updateTableMetadata(String databaseName, TableMetadataModel metadataModel) {
        String tableName = metadataModel.getTableName();

        StringBuilder tableMetaBuilder = new StringBuilder(tableName);
        tableMetaBuilder.append(DATABASE_METADATA);
        write(metadataModel.toStringList(), ROOT, databaseName, tableName, tableMetaBuilder.toString());
    }

    /**
     * @param databaseName
     * @param tableName
     * @param row
     */
    public void insertRow(String databaseName, String tableName, RowModel row) {

        StringBuilder tableBuilder = new StringBuilder(tableName);
        tableBuilder.append(TABLE_FILE_EXTENSION);

        TableMetadataModel metadata = getTableMetadata(databaseName, tableName);

        long nextIndex = metadata.getNoOfRows() + 1;
        RowModel newRow = new RowModel(row, nextIndex);

        int result = append(newRow.toString(), ROOT, databaseName, tableName, tableBuilder.toString());
        updateTableMetadata(databaseName, new TableMetadataModel(metadata, nextIndex));

        logger.log(Level.INFO, valueOf(result));
    }

    /**
     * @param databaseName
     * @param tableName
     * @param column
     * @param value
     * @param newValue
     */
    public void updateRow(String databaseName, String tableName, String column, String value, String newValue) {
        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        List<ColumnMetadataModel> columnsMetadata = tableMetadata.getColumnsMetadata();
        int step = columnsMetadata.size()+1;
        int columnPosition = -1;

        for(int i = 0; i < columnsMetadata.size(); i++){
            if(columnsMetadata.get(i).getName().equals(column)){
                columnPosition = i+1;
            }
        }

        StringBuilder tableBuilder = new StringBuilder(tableName);
        tableBuilder.append(TABLE_FILE_EXTENSION);
        int result = writeWhere(value, newValue, columnPosition, step,
                ROOT, databaseName, tableName, tableBuilder.toString());

        logger.log(Level.INFO, valueOf(result));
    }

    /**
     * @param databaseName
     * @return
     */
    public DatabaseMetadataModel getDatabaseMetadata(String databaseName) {

        StringBuilder databaseMetaBuilder = new StringBuilder(databaseName);
        databaseMetaBuilder.append(DATABASE_METADATA);

        List<String> lines = read(ROOT, databaseName, databaseMetaBuilder.toString());

        return DatabaseMetadataModel.parse(lines);
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     */
    public TableMetadataModel getTableMetadata(String databaseName, String tableName) {

        StringBuilder tableMetaBuilder = new StringBuilder(tableName);
        tableMetaBuilder.append(TABLE_METADATA);

        List<String> lines = read(ROOT, databaseName, tableName, tableMetaBuilder.toString());

        return TableMetadataModel.parse(lines);
    }

}
