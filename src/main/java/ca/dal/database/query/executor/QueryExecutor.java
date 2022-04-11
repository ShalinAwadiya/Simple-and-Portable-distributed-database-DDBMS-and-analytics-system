package ca.dal.database.query.executor;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import static ca.dal.database.utils.PrintUtils.error;
import static ca.dal.database.utils.PrintUtils.success;
import static ca.dal.database.utils.StringUtils.isEmpty;

public class QueryExecutor {

    private final StorageManager storageManager;
    private Connection connection = null;

    /**
     * @param connection
     */
    public QueryExecutor(Connection connection) {
        storageManager = new StorageManager(connection);
        this.connection = connection;
    }

    /**
     * @return getDatabaseName
     */
    private String getDatabaseName() {
        return this.connection.getDatabaseName();
    }

    /**
     * @param databaseName
     */
    private void setDatabaseName(String databaseName) {
        this.connection.setDatabaseName(databaseName);
    }

    /**
     * @param queryModel
     */
    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                if (storageManager.isDatabaseExists(queryModel.getDatabaseName())) {
                    error("%s database already exist", queryModel.getDatabaseName());
                    break;
                }

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
                success("Transaction started successfully!");
                break;

            case COMMIT:

                if (connection.isAutoCommit()) {
                    error("No transaction is running");
                }

                commit(getDatabaseName());
                connection.setAutoCommit(true);
                success("Transaction committed successfully!");
                break;

            case ROLLBACK:

                if (connection.isAutoCommit()) {
                    error("No transaction is running");
                }

                connection.setAutoCommit(true);
                rollback();
                success("Transaction rollback successfully!");
                break;

            case COUNT_QUERIES:
                break;

            case COUNT_UPDATE:
                break;

            default:
                error("Invalid Query Option, Please try again!");
                break;
        }
    }

    /**
     * @param databaseName
     */
    private void commit(String databaseName) {
        storageManager.commit(databaseName);
    }

    private void rollback() {
        storageManager.rollback();
    }

    /**
     * @param queryModel
     */
    private void useDatabase(QueryModel queryModel) {
        setDatabaseName(queryModel.getDatabaseName());
        success(String.format("%s database selected successfully", queryModel.getDatabaseName()));
    }

    /**
     * @param databaseName
     * @param queryModel
     */
    private void deleteRows(String databaseName, QueryModel queryModel) {
        storageManager.deleteRow(queryModel.getRawQuery(), databaseName, queryModel.getTableName(), queryModel.getCondition());
    }

    /**
     * @param databaseName
     * @param queryModel
     */
    private void updateRows(String databaseName, QueryModel queryModel) {
        storageManager.updateRow(queryModel.getRawQuery(), databaseName, queryModel.getTableName(), queryModel.getColumns().get(0), (String) queryModel.getValues().get(0), queryModel.getCondition());
    }

    /**
     * @param databaseName
     * @param queryModel
     */
    private void fetchRows(String databaseName, QueryModel queryModel) {
        storageManager.fetchRows(databaseName, queryModel.getTableName(), queryModel.getColumns(), queryModel.getCondition());
    }

    /**
     * @param queryModel
     */
    private void insertRow(QueryModel queryModel) {
        storageManager.insertRow(queryModel.getRawQuery(), getDatabaseName(), queryModel.getTableName(), new RowModel(queryModel.getValues()));
    }

    /**
     * @param queryModel
     */
    private void createTable(QueryModel queryModel) {
        storageManager.createTable(getDatabaseName(), new TableMetadataModel(queryModel.getTableName(), queryModel.getColumnDefinition()));
    }

    /**
     * @param queryModel
     */
    private void createDatabase(QueryModel queryModel) {
        storageManager.createDatabase(queryModel.getDatabaseName());
    }
}
