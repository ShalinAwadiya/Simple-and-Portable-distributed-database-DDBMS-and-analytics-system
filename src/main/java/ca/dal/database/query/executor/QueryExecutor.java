package ca.dal.database.query.executor;

import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.logging.Level;
import java.util.logging.Logger;

public class  QueryExecutor {

    private static final Logger logger = Logger.getLogger(QueryExecutor.class.getName());
    private final StorageManager storageManager = new StorageManager();

    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                createDatabase(queryModel);
                break;
            case USE_DATABASE:
                // set in connection
                break;
            case CREATE_TABLE:
                storageManager.createTable("user", new TableMetadataModel(queryModel.getTableName(), queryModel.getColumnDefinition()));
                break;
            case INSERT_ROW:
                insertRow(queryModel);
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

    private void insertRow(QueryModel queryModel) {
        storageManager.insertRow(getDatabaseName(), queryModel.getTableName(), new RowModel(queryModel.getValues()));
    }

    private void createTable(QueryModel queryModel) {
        List<ColumnMetadataModel> columnsMetadata = new ArrayList<>();
        for(Map.Entry<String, String> entry: queryModel.getColumnDefinition().entrySet()){
            ColumnMetadataModel columnMeta = new ColumnMetadataModel(entry.getKey(), entry.getValue());
            columnsMetadata.add(columnMeta);
        }
        storageManager.createTable(getDatabaseName(), new TableMetadataModel(queryModel.getTableName(), columnsMetadata));
    }

    private void createDatabase(QueryModel queryModel) {
        storageManager.createDatabase(queryModel.getDatabaseName());
    }
}
