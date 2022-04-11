package ca.dal.database.transaction;

import ca.dal.database.connection.Connection;
import ca.dal.database.query.model.QueryType;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.transaction.model.TableDatastoreModel;
import ca.dal.database.transaction.model.TransactionModel;

import java.util.List;
import java.util.stream.Collectors;

public class TransactionManager {

    private Connection connection = null;

    private TransactionModel transaction = null;

    public TransactionManager(Connection connection) {
        this.connection = connection;
        this.transaction = new TransactionModel();
    }

    public TransactionModel start() {
        return new TransactionModel();
    }

    /**
     * @param databaseName
     * @param tableName
     * @param row
     * @param rawQuery
     */
    public void perform(String databaseName, String tableName, RowModel row, String rawQuery) {
        transaction.addQuery(rawQuery);
        transaction.addInDatastore(databaseName, tableName, row);
    }

    /**
     * @param type
     * @param databaseName
     * @param tableName
     * @param rows
     * @param rawQuery
     */
    public void perform(QueryType type, String databaseName, String tableName, List<RowModel> rows, String rawQuery) {
        transaction.addQuery(rawQuery);

        if (QueryType.DELETE_ROW.equals(type)) {
            transaction.deleteInDatastore(databaseName, tableName, rows.stream().map(row -> row.getMetadata().getIdentifier()).collect(Collectors.toList()));
        } else {
            transaction.updateInDatastore(databaseName, tableName, rows);
        }
    }

    /**
     * @param databaseName
     * @param tableName
     * @return
     */
    public TableDatastoreModel fetchDatastore(String databaseName, String tableName) {
        return transaction.fetchDatastore(databaseName, tableName);
    }

    /**
     * @param databaseName
     * @param tableName
     * @param rawQuery
     * @return
     */
    public TableDatastoreModel fetchDatastore(String databaseName, String tableName, String rawQuery) {
        transaction.addQuery(rawQuery);
        return transaction.fetchDatastore(databaseName, tableName);
    }

    public void rollback() {
        this.transaction = new TransactionModel();
    }

    public List<String> getTablesInvolved() {
        return this.transaction.getTablesInvolved();
    }
}
