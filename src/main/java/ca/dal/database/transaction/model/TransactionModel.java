package ca.dal.database.transaction.model;

import ca.dal.database.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Shah
 */
public class TransactionModel {

    private String id;

    private List<String> queries;

    private Map<String, TableDatastoreModel> datastore;

    public TransactionModel() {
        this.id = UUIDUtils.generate();
        this.queries = new ArrayList<>();
        this.datastore = new HashMap<>();
    }

    /**
     * @param query
     * @author Harsh Shah
     */
    public void addQuery(String  query) {
        this.queries.add(query);
    }


    public TableDatastoreModel fetchDatastore(String tableName) {
        return datastore.get(tableName);
    }

    /**
     * @param tableName
     * @param values
     * @author Harsh Shah
     */
    public void addInDatastore(String tableName, List<Object> values) {
        TableDatastoreModel model = datastore.getOrDefault(tableName, new TableDatastoreModel(tableName));
        model.addRow(values);
        datastore.put(tableName, model);
    }

    /**
     * @param tableName
     * @param rowIdentifier
     * @param values
     * @author Harsh Shah
     */
    public void updateInDatastore(String tableName, String rowIdentifier, List<Object> values) {
        TableDatastoreModel model = datastore.getOrDefault(tableName, new TableDatastoreModel(tableName));
        model.updateRow(rowIdentifier, values);
        datastore.put(tableName, model);
    }

    /**
     * @param tableName
     * @param rowIdentifier
     * @author Harsh Shah
     */
    public void deleteInDatastore(String tableName, String rowIdentifier) {
        TableDatastoreModel model = datastore.getOrDefault(tableName, new TableDatastoreModel(tableName));
        model.deleteRow(rowIdentifier);
        datastore.put(tableName, model);
    }
}
