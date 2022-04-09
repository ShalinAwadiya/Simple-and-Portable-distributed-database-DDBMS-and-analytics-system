package ca.dal.database.query.executor;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.logging.Logger;

import static ca.dal.database.utils.PrintUtils.error;
import static ca.dal.database.utils.PrintUtils.success;
import static ca.dal.database.utils.StringUtils.isEmpty;

/**
 * @author Nishit Mistry
 */
public class QueryExecutor {

    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());

    private final StorageManager storageManager;
    private Connection connection = null;

    private String getDatabaseName() {
        return this.connection.getDatabaseName();
    }

    private void setDatabaseName(String databaseName) {
        this.connection.setDatabaseName(databaseName);
    }

    public QueryExecutor(Connection connection) {
        storageManager = new StorageManager(connection);
        this.connection = connection;
    }

    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                createDatabase(queryModel);
                break;
            case USE_DATABASE:

                if (!storageManager.isDatabaseExists(queryModel.getDatabaseName())) {
                    error("%s database doesn't exist", queryModel.getDatabaseName());
                    break;
                }

                useDatabase(queryModel);
                break;

            case CREATE_TABLE:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }

                createTable(queryModel);
                break;
            case INSERT_ROW:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }

                insertRow(queryModel);
                break;
            case SELECT_ROW:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }

                fetchRows(getDatabaseName(), queryModel);
                break;
            case UPDATE_ROW:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }

                updateRows(getDatabaseName(), queryModel);
                break;
            case DELETE_ROW:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }

                deleteRows(getDatabaseName(), queryModel);
                break;
            case START_TRANSACTION:

                if (isEmpty(getDatabaseName())) {
                    error("Please select database");
                    break;
                }
                connection.setAutoCommit(false);
                break;
            case COMMIT:

                if (!connection.isAutoCommit()) {
                    error("No transaction is running");
                }

                connection.setAutoCommit(true);
                break;
            case ROLLBACK:

                if (!connection.isAutoCommit()) {
                    error("No transaction is running");
                }

                connection.setAutoCommit(false);
                break;
            default:
                error("Invalid Query Option, Please try again!");
                break;
        }
    }

    private void useDatabase(QueryModel queryModel) {
        setDatabaseName(queryModel.getDatabaseName());
        success(String.format("%s database selected successfully", queryModel.getDatabaseName()));
    }

    private void deleteRows(String databaseName, QueryModel queryModel) {
        storageManager.deleteRow(databaseName, queryModel.getTableName(), queryModel.getCondition());
    }

    private void updateRows(String databaseName, QueryModel queryModel) {
        storageManager.updateRow(databaseName, queryModel.getTableName(), queryModel.getColumns().get(0), (String) queryModel.getValues().get(0), queryModel.getCondition());
    }

    private void fetchRows(String databaseName, QueryModel queryModel) {
        storageManager.fetchRows(databaseName, queryModel.getTableName(), queryModel.getColumns(), queryModel.getCondition());
    }

    private void insertRow(QueryModel queryModel) {
        storageManager.insertRow(getDatabaseName(), queryModel.getTableName(), new RowModel(queryModel.getValues()));
    }

    private void createTable(QueryModel queryModel) {
        storageManager.createTable(getDatabaseName(), new TableMetadataModel(queryModel.getTableName(), queryModel.getColumnDefinition()));
    }

    private void createDatabase(QueryModel queryModel) {
        storageManager.createDatabase(queryModel.getDatabaseName());
    }
}
