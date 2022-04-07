package ca.dal.database.transaction.model;

import java.util.*;

/**
 * @author Harsh Shah
 */
public class TableDatastoreModel {

    private String tableName;

    private List<List<Object>> rowsAdded;

    private List<List<Object>> rowsUpdated;

    private Set<String> deletedRowsIdentifiers;

    private Map<String, Integer> updatedRowsIdentifiers;

    public TableDatastoreModel(String tableName) {
        this.tableName = tableName;
        this.rowsAdded = new ArrayList<>();
        this.rowsUpdated = new ArrayList<>();
        this.deletedRowsIdentifiers = new HashSet<>();
        this.updatedRowsIdentifiers = new HashMap<>();
    }

    /**
     * @param values
     * @author Harsh Shah
     */
    public void addRow(List<Object> values){
        this.rowsAdded.add(values);
    }

    /**
     * @param identifier
     * @param values
     * @author Harsh Shah
     */
    public void updateRow(String identifier, List<Object> values){
        Integer index = this.updatedRowsIdentifiers.getOrDefault(identifier, -1);

        if(index == -1){
            this.rowsUpdated.add(values);
        }

        this.rowsUpdated.set(index, values);
    }

    /**
     * @param identifier
     * @author Harsh Shah
     */
    public void deleteRow(String identifier){
        this.deletedRowsIdentifiers.add(identifier);
    }

    /**
     * @return
     * @author Harsh Shah
     */
    public boolean isEmpty(){
        return rowsAdded.isEmpty() && updatedRowsIdentifiers.isEmpty() && deletedRowsIdentifiers.isEmpty();
    }

    /**
     * @return
     * @author Harsh Shah
     */
    public boolean isNotEmpty(){
        return !isEmpty();
    }
}
