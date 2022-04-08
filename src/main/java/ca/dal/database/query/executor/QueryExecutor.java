package ca.dal.database.query.executor;

import ca.dal.database.query.QueryParser;
import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.column.ColumnModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryExecutor {

    private static final Logger logger = Logger.getLogger(QueryParser.class.getName());
    private StorageManager storageManager = new StorageManager();

    public void execute(QueryModel queryModel) {
        switch (queryModel.getType()) {
            case CREATE_DATABASE:
                storageManager.createDatabase(queryModel.getDatabaseName());
                break;
            case USE_DATABASE:
                // set in connection
                break;
            case CREATE_TABLE:
                List<ColumnMetadataModel> columnsMetadata = new ArrayList<>();
                for (Map.Entry<String, String> entry : queryModel.getColumnDefinition().entrySet()) {
                    ColumnMetadataModel columnMeta = new ColumnMetadataModel(entry.getKey(), entry.getValue());
                    columnsMetadata.add(columnMeta);
                }
                storageManager.createTable("user", new TableMetadataModel(queryModel.getTableName(), columnsMetadata));

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
