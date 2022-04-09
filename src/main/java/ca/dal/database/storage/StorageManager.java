package ca.dal.database.storage;

import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.database.DatabaseMetadataHeaderModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.datastore.DatastoreModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataHeaderModel;
import ca.dal.database.storage.model.table.TableMetadataModel;
import ca.dal.database.utils.FileUtils;

import java.util.*;

import static ca.dal.database.constant.ApplicationConstants.DOT;
import static ca.dal.database.utils.FileUtils.*;
import static ca.dal.database.utils.ListUtils.project;
import static ca.dal.database.utils.PrintUtils.*;
import static ca.dal.database.utils.StringUtils.builder;
import static ca.dal.database.utils.StringUtils.isEmpty;

/**
 * @author Harsh Shah
 */
public class StorageManager {


    private static final String ROOT = "datastore";
    private static final String DATASTORE_METADATA = DOT + "meta";
    private static final String DATABASE_METADATA = DOT + "meta";
    private static final String TABLE_FILE_EXTENSION = DOT + "rows";
    private static final String TABLE_METADATA = DOT + "meta";

    public static void init(){
        // Create datastore
        FileUtils.createDirectory(ROOT);

        // Create datastore metadata
        FileUtils.createFile(ROOT, builder(ROOT, DATABASE_METADATA));

        DatastoreModel model = new DatastoreModel(0);

        // Write Table Metadata
        write(model.toListString(), ROOT, builder(ROOT, DATABASE_METADATA));
    }

    /**
     * @author Harsh Shah
     */
    public static void init(){
        // Create datastore
        FileUtils.createDirectory(ROOT);

        // Create datastore metadata
        FileUtils.createFile(ROOT, builder(ROOT, DATASTORE_METADATA));

        // Write Table Metadata
        write(new DatastoreModel(0).toListString(), ROOT, builder(ROOT, DATABASE_METADATA));
    }

    public void updateDatastoreMetadata(DatabaseMetadataModel metadataModel) {
        DatastoreModel metadata = getDatastoreMetadata();
        metadata.addDatabaseMetadataHeaderModels(metadataModel);
        write(metadata.toListString(), ROOT, builder(ROOT, DATASTORE_METADATA));
    }

    /**
     * @return
     * @author Harsh Shah
     */
    private DatastoreModel getDatastoreMetadata() {
        List<String> lines = read(ROOT, builder(ROOT, DATASTORE_METADATA));
        return DatastoreModel.parse(lines);
    }

    /**
     * @param databaseName
     * @return
     * @author Harsh Shah
     */
    public boolean isDatabaseExists(String databaseName){
        DatastoreModel datastoreMetadata = getDatastoreMetadata();

        Optional<DatabaseMetadataHeaderModel> exists = datastoreMetadata.getDatabaseMetadataHeaderModels()
                .stream().filter(itr -> itr.getDatabaseName().equals(databaseName)).findFirst();

        return exists.isPresent();

    }

    /**
     * @param databaseName
     * @author Harsh Shah
     */
    public void createDatabase(String databaseName) {

        DatastoreModel datastoreMetadata = getDatastoreMetadata();

        Optional<DatabaseMetadataHeaderModel> exists = datastoreMetadata.getDatabaseMetadataHeaderModels().stream().filter(itr ->
                itr.getDatabaseName().equals(databaseName)).findFirst();

        if(exists.isPresent()){
            error("%s database already exists", databaseName);
            return;
        }

        // Create Database
        FileUtils.createDirectory(ROOT, databaseName);

        // Create Database Metadata
        String databaseMetaFile = builder(databaseName, DATABASE_METADATA);
        FileUtils.createFile(ROOT, databaseName, databaseMetaFile);

        DatabaseMetadataModel model = new DatabaseMetadataModel(databaseName);

        // Write Table Metadata
        write(model.toMetaString(), ROOT, databaseName, databaseMetaFile);
        updateDatastoreMetadata(model);
        success(String.format("Database %s created successfully", databaseName));
    }

    /**
     * @param databaseName
     * @param metadataModel
     * @author Harsh Shah
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

        DatabaseMetadataModel databaseMetadata = getDatabaseMetadata(databaseName);
        Optional<TableMetadataHeaderModel> exists = databaseMetadata.getTableHeaderMetadataModels().stream()
                .filter(itr -> itr.getTableName().equals(tableName)).findFirst();

        if(exists.isPresent()){
            error("%s table already exists", tableName);
            return;
        }

        // Create Table Space
        FileUtils.createDirectory(ROOT, databaseName, tableName);

        // Create Table
        String tableFile = builder(tableName, TABLE_FILE_EXTENSION);
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
     * @author Harsh Shah
     */
    public void updateTableMetadata(String databaseName, TableMetadataModel metadataModel) {
        String tableName = metadataModel.getTableName();

        write(metadataModel.toStringList(), ROOT, databaseName, tableName,
                builder(tableName, DATABASE_METADATA));
    }

    /**
     * @param databaseName
     * @param tableName
     * @param columns
     * @param condition
     * @author Harsh Shah
     */
    public void fetchRows(String databaseName, String tableName,
                          List<String> columns, Map<String, Object> condition) {

        String columnName = null;
        String columnValue = null;

        if (!condition.isEmpty()) {
            Map.Entry<String, Object> entry = condition.entrySet().iterator().next();
            columnName = entry.getKey();
            columnValue = (String) entry.getValue();
        }

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int columnIndex = -1;
        List<Integer> projectedIndexes = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        HashSet<String> columnSet = new HashSet<>(columns);

        for (int i = 0; i < tableMetadata.getColumnsMetadata().size(); i++) {
            ColumnMetadataModel itr = tableMetadata.getColumnsMetadata().get(i);

            if (null != columnName && columnName.equals(itr.getName())) {
                columnIndex = i;
            }

            if (columnSet.contains(itr.getName())) {
                projectedIndexes.add(i);
                headers.add(itr.getName());
            }
        }

        List<RowModel> rows = fetchAllRows(databaseName, tableName);
        List<List<Object>> matrix = new ArrayList<>();

        for (RowModel row : rows) {
            List<Object> values = row.getValues();

            if (columnIndex == -1) {
                matrix.add(project(projectedIndexes, values));
            } else if (columnValue.equals(values.get(columnIndex))) {
                matrix.add(project(projectedIndexes, values));
            }
        }

        printMatrix(headers, matrix);
    }

    /**
     * @param databaseName
     * @param tableName
     * @param row
     * @author Harsh Shah
     */
    public void insertRow(String databaseName, String tableName, RowModel row) {

        TableMetadataModel metadata = getTableMetadata(databaseName, tableName);

        long nextIndex = metadata.getNoOfRows() + 1;
        RowModel newRow = new RowModel(row, nextIndex);

        int result = append(newRow.toString(), ROOT, databaseName, tableName,
                builder(tableName, TABLE_FILE_EXTENSION));
        updateTableMetadata(databaseName, new TableMetadataModel(metadata, nextIndex));

        success("1 record inserted successfully");
    }

    /**
     * @param databaseName
     * @param tableName
     * @param column
     * @param newValue
     * @author Harsh Shah
     */
    public void updateRow(String databaseName, String tableName, String column,
                          String newValue, Map<String, Object> condition) {

        String columnName = null;
        String columnValue = null;

        if (!condition.isEmpty()) {
            Map.Entry<String, Object> entry = condition.entrySet().iterator().next();
            columnName = entry.getKey();
            columnValue = (String) entry.getValue();
        }

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int columnIndex = -1;
        int replaceColumnIndex = -1;

        for (int i = 0; i < tableMetadata.getColumnsMetadata().size(); i++) {
            ColumnMetadataModel itr = tableMetadata.getColumnsMetadata().get(i);
            if (null != columnName && columnName.equals(itr.getName())) {
                columnIndex = i;
            }

            if (column.equals(itr.getName())) {
                replaceColumnIndex = i;
            }
        }

        List<RowModel> rows = fetchAllRows(databaseName, tableName);

        int noOfRowUpdated = 0;
        for (RowModel row : rows) {
            List<Object> values = row.getValues();
            if (isEmpty(columnValue)) {
                values.set(replaceColumnIndex, newValue);
                noOfRowUpdated++;
            } else if (columnValue.equals(values.get(columnIndex))) {
                values.set(replaceColumnIndex, newValue);
                noOfRowUpdated++;
            }
        }

        updateAllRows(databaseName, tableName, rows);
        success(String.format("%d record(s) updated successfully", noOfRowUpdated));
    }


    /**
     * @param databaseName
     * @param tableName
     * @param condition
     * @author Harsh Shah
     */
    public void deleteRow(String databaseName, String tableName, Map<String, Object> condition) {

        String columnName = null;
        String columnValue = null;

        if (!condition.isEmpty()) {
            Map.Entry<String, Object> entry = condition.entrySet().iterator().next();
            columnName = entry.getKey();
            columnValue = (String) entry.getValue();
        }

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int columnIndex = -1;
        for (int i = 0; i < tableMetadata.getColumnsMetadata().size(); i++) {
            ColumnMetadataModel itr = tableMetadata.getColumnsMetadata().get(i);
            if (null != columnName && columnName.equals(itr.getName())) {
                columnIndex = i;
            }
        }

        List<RowModel> rows = fetchAllRows(databaseName, tableName);

        int noOfRowDeleted = 0;
        List<RowModel> remainingRows = new ArrayList<>();
        for (RowModel row : rows) {
            List<Object> values = row.getValues();
            if (isEmpty(columnValue)) {
                noOfRowDeleted++;
            } else if (columnValue.equals(values.get(columnIndex))) {
                noOfRowDeleted++;
            } else {
                remainingRows.add(row);
            }
        }

        updateAllRows(databaseName, tableName, remainingRows);
        updateTableMetadata(databaseName, new TableMetadataModel(tableMetadata, (long) remainingRows.size()));
        success(String.format("%d record(s) deleted successfully", noOfRowDeleted));

    }

    /**
     * @param databaseName
     * @return
     * @author Harsh Shah
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
     * @author Harsh Shah
     */
    public TableMetadataModel getTableMetadata(String databaseName, String tableName) {

        List<String> lines = read(ROOT, databaseName, tableName,
                builder(tableName, TABLE_METADATA));

        return TableMetadataModel.parse(lines);
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    private List<RowModel> fetchAllRows(String databaseName, String tableName) {

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int step = tableMetadata.getColumnsMetadata().size() + 1;

        List<String> lines = read(ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));

        List<RowModel> matrix = new ArrayList<>();
        List<String> row = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            if (i != 0 && i % step == 0) {
                matrix.add(RowModel.parse(row));
                row = new ArrayList<>();
            }
            row.add(lines.get(i));
        }
        matrix.add(RowModel.parse(row));

        return matrix;
    }

    /**
     * @param databaseName
     * @param tableName
     * @param rows
     * @author Harsh Shah
     */
    private void updateAllRows(String databaseName, String tableName, List<RowModel> rows) {
        List<String> output = new ArrayList<>();
        rows.stream().forEach(row -> output.addAll(row.toList()));
        write(output, ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));
    }

}
