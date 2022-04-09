package ca.dal.database.query.executor;

import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutor {

    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());
    private final StorageManager storageManager = new StorageManager();

    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                storageManager.createDatabase(queryModel.getDatabaseName());
                break;
            case USE_DATABASE:
                // set in connection
                break;
            case CREATE_TABLE:
                storageManager.createTable("user", new TableMetadataModel(queryModel.getTableName(), queryModel.getColumnDefinition()));
                break;
            case INSERT_ROW:
                storageManager.insertRow("user", queryModel.getTableName(), new RowModel(queryModel.getValues()));
                break;
            case SELECT_ROW:
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
}
