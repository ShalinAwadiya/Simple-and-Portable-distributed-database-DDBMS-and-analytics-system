package ca.dal.database.transaction.model;

import ca.dal.database.storage.model.row.RowModel;

import java.util.*;

/**
 * @author Harsh Shah
 */
public class TableDatastoreModel {

    private String tableName;

    private List<RowModel> rowsAdded;

    private List<RowModel> rowsUpdated;

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
     * @param rowModel
     * @author Harsh Shah
     */
    public void addRow(RowModel rowModel){
        this.rowsAdded.add(rowModel);
    }

    /**
     * @param rowModel
     * @author Harsh Shah
     */
    public void updateRow( RowModel rowModel){
        String identifier = rowModel.getMetadata().getIdentifier();

        int index = this.updatedRowsIdentifiers.getOrDefault(identifier, -1);

        if(index == -1){
            this.rowsUpdated.add(rowModel);
            this.updatedRowsIdentifiers.put(identifier, this.rowsUpdated.size() -1);
        } else {
            this.rowsUpdated.set(index, rowModel);
        }
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

    public List<RowModel> getRowsAdded() {
        return rowsAdded;
    }

    public List<RowModel> getRowsUpdated() {
        return rowsUpdated;
    }

    public Set<String> getDeletedRowsIdentifiers() {
        return deletedRowsIdentifiers;
    }

    public Map<String, Integer> getUpdatedRowsIdentifiers() {
        return updatedRowsIdentifiers;
    }

    /**
     * @return
     * @author Harsh Shah
     */
    public boolean isNotEmpty(){
        return !isEmpty();
    }
}
