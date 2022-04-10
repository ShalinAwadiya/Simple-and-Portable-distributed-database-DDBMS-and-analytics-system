package ca.dal.database.transaction.model;

import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.utils.UUIDUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Harsh Shah
 */
public class TransactionModel {

    private String id;

    private List<String> queries;

    private Map<String, Map<String, TableDatastoreModel>> datastore;

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


    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    public TableDatastoreModel fetchDatastore(String databaseName, String tableName) {
        return getFromBuffer(databaseName, tableName);
    }

    /**
     * @param tableName
     * @param rowModel
     * @author Harsh Shah
     */
    public void addInDatastore(String databaseName, String tableName, RowModel rowModel) {
        TableDatastoreModel model = getFromBuffer(databaseName, tableName);
        model.addRow(rowModel);
        putFromBuffer(databaseName, tableName, model);
    }



    /**
     * @param tableName
     * @param rowModels
     * @author Harsh Shah
     */
    public void updateInDatastore(String databaseName, String tableName, List<RowModel> rowModels) {
        TableDatastoreModel model = getFromBuffer(databaseName, tableName);

        for(RowModel rowModel: rowModels){
            model.updateRow(rowModel);
        }

        putFromBuffer(databaseName, tableName, model);
    }

    /**
     * @param tableName
     * @param rowIdentifiers
     * @author Harsh Shah
     */
    public void deleteInDatastore(String databaseName, String tableName, List<String> rowIdentifiers) {
        TableDatastoreModel model = getFromBuffer(databaseName, tableName);

        for(String rowIdentifier: rowIdentifiers) {
            model.deleteRow(rowIdentifier);
        }
        putFromBuffer(databaseName, tableName, model);
    }


    /**
     * @param databaseName
     * @param tableName
     * @return
     * @author Harsh Shah
     */
    private TableDatastoreModel getFromBuffer(String databaseName, String tableName){
        if(!datastore.containsKey(databaseName)){
            datastore.put(databaseName, new HashMap<>());
        }

        if(!datastore.get(databaseName).containsKey(tableName)){
            datastore.get(databaseName).put(tableName, new TableDatastoreModel(tableName));
        }

        return datastore.get(databaseName).get(tableName);
    }

    /**
     * @param databaseName
     * @param tableName
     * @param model
     * @author Harsh Shah
     */
    private void putFromBuffer(String databaseName, String tableName, TableDatastoreModel model) {
        datastore.get(databaseName).put(tableName, model);
    }

    public List<String> getTablesInvolved() {
        return datastore.entrySet()
                .iterator().next()
                .getValue().entrySet()
                .stream().map(itr -> itr.getKey()).collect(Collectors.toList());
    }
}
