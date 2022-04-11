package ca.dal.database.storage;

import ca.dal.database.config.ApplicationConfiguration;
import ca.dal.database.connection.Connection;
import ca.dal.database.query.model.QueryType;
import ca.dal.database.storage.dictionary.DataDictionaryEntry;
import ca.dal.database.storage.dictionary.GlobalDataDictionary;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.database.DatabaseMetadataHeaderModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.datastore.DatastoreModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataHeaderModel;
import ca.dal.database.storage.model.table.TableMetadataModel;
import ca.dal.database.transaction.TransactionManager;
import ca.dal.database.transaction.model.TableDatastoreModel;
import ca.dal.database.utils.FileUtils;
import ca.dal.database.utils.SSHUtils;
import ca.dal.database.utils.UUIDUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

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


    public static final String ROOT = "datastore";
    public static final String SHARED = "shared";
    public static final String GLOBAL = "global";
    public static final String DATASTORE_METADATA = DOT + "meta";
    private static final String DATABASE_METADATA = DOT + "meta";
    private static final String TABLE_FILE_EXTENSION = DOT + "rows";
    private static final String TABLE_METADATA = DOT + "meta";


    public static GlobalDataDictionary globalDataDictionary = null;
    private Connection connection = null;
    private TransactionManager transactionManager = null;

    public StorageManager(Connection connection) {
        transactionManager = new TransactionManager(connection);
        this.connection = connection;
    }

    private boolean isTransaction() {
        return !this.connection.isAutoCommit();
    }


    /**
     * @author Harsh Shah
     */
    public static void init() {
        // Create datastore
        FileUtils.createDirectory(ROOT);

        if (Files.notExists(Path.of(ROOT, builder(ROOT, DATASTORE_METADATA)))) {

            // Create datastore metadata
            FileUtils.createFile(ROOT, builder(ROOT, DATASTORE_METADATA));

            // Write Table Metadata
            write(new DatastoreModel(0).toListString(), ROOT, builder(ROOT, DATASTORE_METADATA));
        }

        if (Files.notExists(Path.of(ROOT, SHARED, builder(GLOBAL, DATASTORE_METADATA)))) {

            // Create shared folder
            FileUtils.createDirectory(ROOT, SHARED);

            // Create global metadata
            FileUtils.createFile(ROOT, SHARED, builder(GLOBAL, DATASTORE_METADATA));

            globalDataDictionary = new GlobalDataDictionary(new ArrayList<>());

        } else {
            syncGlobalMetadata();
        }
    }

    public static void syncGlobalMetadata() {

        List<String> remoteLines = SSHUtils.operation(SSHUtils.PEEK, builder("datastore/shared/", GLOBAL, DATASTORE_METADATA));
        List<String> localLines = readLocalCopyOfGlobalMetadata();

        Set<DataDictionaryEntry> entries = new HashSet<>();
        for (String line : remoteLines) {
            entries.add(DataDictionaryEntry.parse(line));
        }

        for (String line : localLines) {
            DataDictionaryEntry entry = DataDictionaryEntry.parse(line);
            if (!entries.contains(entry)) {
                entries.add(entry);
            }
        }

        globalDataDictionary = new GlobalDataDictionary(new ArrayList<>(entries));
        writeLocalCopyOfGlobalMetadata();
        SSHUtils.operation(SSHUtils.PUSH, builder("datastore/shared/", GLOBAL, DATASTORE_METADATA));
    }

    private static void writeLocalCopyOfGlobalMetadata() {
        write(globalDataDictionary.toListString(), ROOT, SHARED, builder(GLOBAL, DATASTORE_METADATA));
    }

    private static List<String> readLocalCopyOfGlobalMetadata() {
        return FileUtils.read(ROOT, SHARED, builder(GLOBAL, DATASTORE_METADATA));
    }

    /**
     * @param metadataModel
     * @author Harsh Shah
     */
    public void updateDatastoreMetadata(DatabaseMetadataModel metadataModel) {
        DatastoreModel metadata = getDatastoreMetadata();
        metadata.addDatabaseMetadataHeaderModels(metadataModel);
        write(metadata.toListString(), ROOT, builder(ROOT, DATASTORE_METADATA));
        append(new DataDictionaryEntry(metadataModel.getDatabaseName()).toString(),
                ROOT, SHARED, builder(GLOBAL, DATASTORE_METADATA));

        syncGlobalMetadata();
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
    public boolean isDatabaseExists(String databaseName) {
        DatastoreModel datastoreMetadata = getDatastoreMetadata();

        Optional<DatabaseMetadataHeaderModel> exists = datastoreMetadata.getDatabaseMetadataHeaderModels()
                .stream().filter(itr -> itr.getDatabaseName().equals(databaseName)).findFirst();

        if (exists.isPresent()) {
            return true;
        }

        // Sync Metadata
        syncGlobalMetadata();

        return globalDataDictionary.getEntries().stream().filter(itr -> itr.getDatabaseName().equals(databaseName)).findFirst().isPresent();
    }

    /**
     * @param databaseName
     * @author Harsh Shah
     */
    public void createDatabase(String databaseName) {

        DatastoreModel datastoreMetadata = getDatastoreMetadata();

        Optional<DatabaseMetadataHeaderModel> exists = datastoreMetadata.getDatabaseMetadataHeaderModels().stream().filter(itr ->
                itr.getDatabaseName().equals(databaseName)).findFirst();

        if (exists.isPresent()) {
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

        Set<String> instances = globalDataDictionary.getInstancesInvolved(databaseName);

        if (instances.size() == 2) {
            syncDatabaseMetadata(databaseName);
        }

        syncGlobalMetadata();

        DataDictionaryEntry entry = new DataDictionaryEntry(databaseName);
        boolean isFound = false;

        for (DataDictionaryEntry dictionaryEntry : globalDataDictionary.getEntries()) {
            if (dictionaryEntry.equals(entry) && isEmpty(dictionaryEntry.getTableName())) {
                dictionaryEntry.setTableName(metadataModel.getTableName());
                isFound = true;
                break;
            }
        }

        if (!isFound) {
            globalDataDictionary.addEntry(new DataDictionaryEntry(databaseName, metadataModel.getTableName()));
        }

        writeLocalCopyOfGlobalMetadata();

        syncGlobalMetadata();
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

        if (exists.isPresent()) {
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

        DataDictionaryEntry entry = globalDataDictionary.getInstanceInvolved(databaseName, tableName).get();

        if(ApplicationConfiguration.isCurrentInstance(entry)) {

            write(metadataModel.toStringList(), ROOT, databaseName, tableName,
                    builder(tableName, TABLE_METADATA));
        } else {
            updateTableMetadataOnRemote(databaseName, tableName, metadataModel);
        }
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

            if (columnSet.size() == 1 && columnSet.contains("*")) {
                projectedIndexes.add(i);
                headers.add(itr.getName());
            } else if (columnSet.contains(itr.getName())) {
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
    public void insertRow(String rawQuery, String databaseName, String tableName, RowModel row) {

        TableMetadataModel metadata = getTableMetadata(databaseName, tableName);

        long nextIndex = metadata.getNoOfRows() + 1;
        RowModel newRow = new RowModel(row, nextIndex);

        if (isTransaction()) {
            transactionManager.perform(databaseName, tableName, row, rawQuery);
        } else {


            DataDictionaryEntry entry = globalDataDictionary.getInstanceInvolved(databaseName, tableName).get();

            if(ApplicationConfiguration.isCurrentInstance(entry)) {
                append(newRow.toString(), ROOT, databaseName, tableName,
                        builder(tableName, TABLE_FILE_EXTENSION));

            } else {
                appendTableOnRemote(databaseName, tableName, newRow.toList());
            }

            updateTableMetadata(databaseName, new TableMetadataModel(metadata, nextIndex));
        }

        success("1 record inserted successfully");
    }

    /**
     * @param databaseName
     * @param tableName
     * @param column
     * @param newValue
     * @author Harsh Shah
     */
    public void updateRow(String rawQuery, String databaseName, String tableName, String column,
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

        List<RowModel> updatedRows = new ArrayList<>();
        for (RowModel row : rows) {
            List<Object> values = row.getValues();
            if (isEmpty(columnValue)) {
                values.set(replaceColumnIndex, newValue);
                updatedRows.add(row);
                noOfRowUpdated++;
            } else if (columnValue.equals(values.get(columnIndex))) {
                values.set(replaceColumnIndex, newValue);
                updatedRows.add(row);
                noOfRowUpdated++;
            }
        }

        if (isTransaction()) {
            transactionManager.perform(QueryType.UPDATE_ROW, databaseName, tableName,
                    updatedRows, rawQuery);
        } else {
            updateAllRows(databaseName, tableName, rows);
        }
        success(String.format("%d record(s) updated successfully", noOfRowUpdated));
    }

    /**
     * @param databaseName
     * @param tableName
     * @param condition
     * @author Harsh Shah
     */
    public void deleteRow(String rawQuery, String databaseName, String tableName, Map<String, Object> condition) {

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
        List<RowModel> deletedRows = new ArrayList<>();
        List<RowModel> remainingRows = new ArrayList<>();
        for (RowModel row : rows) {
            List<Object> values = row.getValues();
            if (isEmpty(columnValue)) {
                noOfRowDeleted++;
                deletedRows.add(row);
            } else if (columnValue.equals(values.get(columnIndex))) {
                noOfRowDeleted++;
                deletedRows.add(row);
            } else {
                remainingRows.add(row);
            }
        }

        if (isTransaction()) {

            transactionManager.perform(QueryType.DELETE_ROW, databaseName, tableName,
                    deletedRows, rawQuery);
        } else {
            updateAllRows(databaseName, tableName, remainingRows);
            updateTableMetadata(databaseName, new TableMetadataModel(tableMetadata, (long) remainingRows.size()));
        }
        success(String.format("%d record(s) deleted successfully", noOfRowDeleted));

    }

    /**
     * @param databaseName
     * @return
     * @author Harsh Shah
     */
    public DatabaseMetadataModel getDatabaseMetadata(String databaseName) {

        Set<String> instances = globalDataDictionary.getInstancesInvolved(databaseName);

        List<String> remoteLines = null;

        int count = 0;
        if(instances.size() == 2){
            remoteLines = SSHUtils.operation(SSHUtils.PEEK, builder("datastore/",
                    databaseName, "/",
                    builder(databaseName, DATABASE_METADATA)));

            count++;
        }

        if (remoteLines == null) {
            remoteLines = new ArrayList<>();
        }

        List<String> lines = null;

        if(instances.contains(ApplicationConfiguration.getCurrentInstance())){
            lines = read(ROOT, databaseName,
                    builder(databaseName, DATABASE_METADATA));
            count++;

        }


        if (lines == null) {
            lines = new ArrayList<>();
        } else {
            if(count == 2) {
                lines = lines.subList(1, lines.size());
            }
        }

        Set<String> references = new HashSet<>(remoteLines);

        for (String line : lines) {
            if (!references.contains(line)) {
                remoteLines.add(line);
            }
        }

        if (remoteLines.isEmpty()) {
            return null;
        }

        return DatabaseMetadataModel.parse(remoteLines);
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    public TableMetadataModel getTableMetadata(String databaseName, String tableName) {

        Optional<DataDictionaryEntry> entryOptional = globalDataDictionary.getInstanceInvolved(databaseName, tableName);

        DataDictionaryEntry entry = entryOptional.get();

        List<String> lines = null;

        if(ApplicationConfiguration.isCurrentInstance(entry)){


            lines = read(ROOT, databaseName, tableName,
                    builder(tableName, TABLE_METADATA));

        } else {

            lines = SSHUtils.operation(SSHUtils.PEEK, builder("datastore/",
                    databaseName, "/", tableName, "/",
                    builder(tableName, TABLE_METADATA)));
        }

        return TableMetadataModel.parse(lines);
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    public List<RowModel> fetchAllRows(String databaseName, String tableName) {

        List<RowModel> fromStorage = fetchAllRowsFromStorage(databaseName, tableName);

        if (!isTransaction()) {
            return fromStorage;
        }

        TableDatastoreModel fromBuffer = transactionManager.fetchDatastore(databaseName, tableName);

        fromStorage.addAll(fromBuffer.getRowsAdded());

        Map<String, Integer> updatedIdentifiers = fromBuffer.getUpdatedRowsIdentifiers();

        for (RowModel itr : fromStorage) {
            String identifier = itr.getMetadata().getIdentifier();
            if (updatedIdentifiers.containsKey(identifier)) {
                itr.setValues(fromBuffer.getRowsUpdated().get(updatedIdentifiers.get(identifier)).getValues());
            }
        }

        Set<String> deletedIdentifiers = fromBuffer.getDeletedRowsIdentifiers();

        return fromStorage.stream()
                .filter(itr -> !(deletedIdentifiers.contains(itr.getMetadata().getIdentifier())))
                .collect(Collectors.toList());
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    private List<RowModel> fetchAllRowsFromStorage(String databaseName, String tableName) {

        TableMetadataModel tableMetadata = getTableMetadata(databaseName, tableName);

        int step = tableMetadata.getColumnsMetadata().size() + 1;

        List<String> lines = readFileFromStorage(databaseName, tableName);

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
     * @return
     */
    private List<String> readFileFromStorage(String databaseName, String tableName) {

        Optional<DataDictionaryEntry> entryOptional = globalDataDictionary.getInstanceInvolved(databaseName, tableName);

        DataDictionaryEntry entry = entryOptional.get();

        if(ApplicationConfiguration.isCurrentInstance(entry)) {

            return read(ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));

        } else {

            return SSHUtils.operation(SSHUtils.PEEK, builder("datastore/",
                    databaseName, "/", tableName, "/",
                    builder(tableName, TABLE_FILE_EXTENSION)));
        }
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

        DataDictionaryEntry entry = globalDataDictionary.getInstanceInvolved(databaseName, tableName).get();

        if(ApplicationConfiguration.isCurrentInstance(entry)){
            write(output, ROOT, databaseName, tableName, builder(tableName, TABLE_FILE_EXTENSION));
        } else {
            updateTableOnRemote(databaseName,tableName, output);
        }
    }

    /**
     * @author Harsh Shah
     */
    public void rollback() {
        transactionManager.rollback();
    }

    /**
     * @param databaseName
     * @author Harsh Shah
     */
    public void commit(String databaseName) {

        List<String> tables = transactionManager.getTablesInvolved();

        for (String tableName : tables) {
            List<RowModel> rows = fetchAllRows(databaseName, tableName);

            TableMetadataModel metadata = getTableMetadata(databaseName, tableName);

            Long rowNumbers = metadata.getNoOfRows();

            for (RowModel row : rows) {
                if (row.getMetadata().getIndex() == -1) {
                    rowNumbers++;
                    row.getMetadata().setIndex(rowNumbers);
                }
            }

            updateAllRows(databaseName, tableName, rows);
            updateTableMetadata(databaseName, new TableMetadataModel(metadata, (long) rows.size()));
        }
    }

    private void syncDatabaseMetadata(String databaseName) {
        SSHUtils.operation(SSHUtils.PUSH, builder(ROOT, "/", databaseName, "/", builder(databaseName, DATABASE_METADATA)));
    }



    private void appendTableOnRemote(String databaseName, String tableName, List<String> rows) {
        List<String> lines = SSHUtils.operation(SSHUtils.PEEK, builder("datastore/",
                databaseName, "/", tableName, "/",
                builder(tableName, TABLE_FILE_EXTENSION)));

        lines.addAll(rows);

        String tempTableId = UUIDUtils.generate();
        FileUtils.createTempFile("datastore", tempTableId, lines);
        SSHUtils.operation(SSHUtils.PUSH, "datastore/temp/"+tempTableId,
                builder("datastore/", databaseName, "/", tableName, "/", builder(tableName, TABLE_FILE_EXTENSION)));

        FileUtils.removeTempFile("datastore", tempTableId);

    }


    private void updateTableOnRemote(String databaseName, String tableName, List<String> rows) {
        String tempTableId = UUIDUtils.generate();
        FileUtils.createTempFile("datastore", tempTableId, rows);
        SSHUtils.operation(SSHUtils.PUSH, "datastore/temp/"+tempTableId,
                builder("datastore/", databaseName, "/", tableName, "/", builder(tableName, TABLE_FILE_EXTENSION)));

        FileUtils.removeTempFile("datastore", tempTableId);
    }

    private void updateTableMetadataOnRemote(String databaseName, String tableName, TableMetadataModel metadataModel) {
        String tempTableMetaId = UUIDUtils.generate();
        FileUtils.createTempFile("datastore", tempTableMetaId, metadataModel.toStringList());
        SSHUtils.operation(SSHUtils.PUSH, "datastore/temp/"+tempTableMetaId,
                builder("datastore/", databaseName, "/", tableName, "/", builder(tableName, TABLE_METADATA)));

        FileUtils.removeTempFile("datastore", tempTableMetaId);
    }
}
