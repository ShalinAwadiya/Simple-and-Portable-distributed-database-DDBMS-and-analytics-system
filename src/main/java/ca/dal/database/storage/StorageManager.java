package ca.dal.database.storage;

import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;
import ca.dal.database.utils.FileUtils;

import java.util.List;
import java.util.Map;

import static ca.dal.database.constant.ApplicationConstants.DOT;
import static ca.dal.database.utils.FileUtils.*;
import static ca.dal.database.utils.PrintUtils.println;
import static ca.dal.database.utils.PrintUtils.success;
import static ca.dal.database.utils.StringUtils.builder;

/**
 * @author Harsh Shah
 */
public class StorageManager {


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
        String databaseMetaFile = builder(databaseName, DATABASE_METADATA);
        FileUtils.createFile(ROOT, databaseName, databaseMetaFile);

        DatabaseMetadataModel model = new DatabaseMetadataModel(databaseName);

        // Write Table Metadata
        write(model.toMetaString(), ROOT, databaseName, databaseMetaFile);

        success(String.format("Database %s created successfully", databaseName));
    }

    /**
     * @param databaseName
     * @param metadataModel
     */
    public void updateDatabaseMetadata(String databaseName, TableMetadataModel metadataModel) {

        DatabaseMetadataModel metadata = getDatabaseMetadata(databaseName);

        metadata.addTableHeaderMetadataModel(metadataModel);

        write(metadata.toListString(), ROOT, databaseName, builder(databaseName, DATABASE_METADATA));

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
        String tableFile = builder(tableName,TABLE_FILE_EXTENSION);
        FileUtils.createFile(ROOT, databaseName, tableName, tableFile);

        // Create Table Metadata
        String tableMetaFile = builder(tableName, TABLE_METADATA);
        FileUtils.createFile(ROOT, databaseName, tableName, tableMetaFile);

        // Write Table Metadata
        int result = write(metadata.toStringList(), ROOT, databaseName, tableName, tableMetaFile);
        updateDatabaseMetadata(databaseName, metadata);

        success(String.format("Table %s created successfully", tableName));
    }

    /**
     * @param databaseName
     * @param metadataModel
     */
    public void updateTableMetadata(String databaseName, TableMetadataModel metadataModel) {
        String tableName = metadataModel.getTableName();

        write(metadataModel.toStringList(), ROOT, databaseName, tableName,
                builder(tableName, DATABASE_METADATA));
    }

    /**
     * @param databaseName
     * @param tableName
     * @param row
     */
    public void insertRow(String databaseName, String tableName, RowModel row) {

        TableMetadataModel metadata = getTableMetadata(databaseName, tableName);

        long nextIndex = metadata.getNoOfRows() + 1;
        RowModel newRow = new RowModel(row, nextIndex);

        int result = append(newRow.toString(), ROOT, databaseName, tableName,
                builder(tableName,TABLE_FILE_EXTENSION));
        updateTableMetadata(databaseName, new TableMetadataModel(metadata, nextIndex));

        success("1 record inserted successfully");
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

        int result = writeWhere(value, newValue, columnPosition, step,
                ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));

        success(String.format("%d record(s) updated successfully", databaseName));
    }

    /**
     * @param databaseName
     * @return
     */
    public DatabaseMetadataModel getDatabaseMetadata(String databaseName) {

        List<String> lines = read(ROOT, databaseName,
                builder(databaseName, DATABASE_METADATA));

        return DatabaseMetadataModel.parse(lines);
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     */
    public TableMetadataModel getTableMetadata(String databaseName, String tableName) {

        List<String> lines = read(ROOT, databaseName, tableName,
                builder(tableName, TABLE_METADATA));

        return TableMetadataModel.parse(lines);
    }

    public void fetchRows(String databaseName, String tableName,
                          List<String> columns, Map<String, Object> condition) {

        String columnName = null;
        String columnValue = null;

        if(!condition.isEmpty()) {
            Map.Entry<String, Object> entry = condition.entrySet().iterator().next();
            columnName = entry.getKey();
            columnValue = (String) entry.getValue();
        }

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int columnIndex = -1;
        int step = tableMetadata.getColumnsMetadata().size();

        if(null != columnName) {
            for(int i = 0; i < tableMetadata.getColumnsMetadata().size(); i++){
                ColumnMetadataModel itr = tableMetadata.getColumnsMetadata().get(i);

                if(columnName.equals(itr.getName())){
                    columnIndex = i;
                    break;
                }
            }
        }

        FileUtils.read(ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));


        System.out.println();
    }
}
