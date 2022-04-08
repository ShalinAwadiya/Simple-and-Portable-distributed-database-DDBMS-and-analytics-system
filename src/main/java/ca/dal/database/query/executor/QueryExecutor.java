package ca.dal.database.query.executor;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.dal.database.utils.PrintUtils.success;

public class  QueryExecutor {

    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());
    private final StorageManager storageManager = new StorageManager();
    private Connection connection = null;

    private String getDatabaseName() {
        return this.connection.getDatabaseName();
    }

    private void setDatabaseName(String databaseName) {
        this.connection.setDatabaseName(databaseName);
    }

    public QueryExecutor(Connection connection){
        this.connection = connection;
    }

    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                createDatabase(queryModel);
                break;
            case USE_DATABASE:
                setDatabaseName(queryModel.getDatabaseName());
                success(String.format("%s database selected successfully", queryModel.getDatabaseName()));
                break;
            case CREATE_TABLE:
                createTable(queryModel);
                break;
            case INSERT_ROW:
                insertRow(queryModel);
                break;
            case SELECT_ROW:
                storageManager.fetchRows(getDatabaseName(), queryModel.getTableName(), queryModel.getColumns(), queryModel.getCondition());
                break;
            case UPDATE_ROW:
                break;
            case DELETE_ROW:
                break;
            case START_TRANSACTION:
                break;
            case END_TRANSACTION:
                break;
            case COMMIT:
                break;
            case ROLLBACK:
                break;
            default:
                logger.log(Level.INFO, "Invalid Query Option");
                break;
        }
    }


    private void insertRow(QueryModel queryModel) {
        storageManager.insertRow(getDatabaseName(), queryModel.getTableName(), new RowModel(queryModel.getValues()));
    }

    private void createTable(QueryModel queryModel) {
        storageManager.createTable(getDatabaseName(),
                new TableMetadataModel(queryModel.getTableName(), queryModel.getColumnDefinition()));
    }

    private void createDatabase(QueryModel queryModel) {
        storageManager.createDatabase(queryModel.getDatabaseName());
    }
}
