package ca.dal.database.storage.model.database;

import ca.dal.database.storage.model.table.TableMetadataHeaderModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseMetadataModel extends DatabaseMetadataHeaderModel {

    private List<TableMetadataHeaderModel> tableMetadataHeaderModels;

    public DatabaseMetadataModel(String databaseName) {
        super(databaseName, 0);
        this.setTableHeaderMetadataModels(new ArrayList<>());
    }

    public DatabaseMetadataModel(String databaseName, List<TableMetadataHeaderModel> tableMetadataHeaderModels) {
        super(databaseName, tableMetadataHeaderModels.size());
        this.setTableHeaderMetadataModels(tableMetadataHeaderModels);
    }

    public List<TableMetadataHeaderModel> getTableHeaderMetadataModels() {
        if(tableMetadataHeaderModels == null){
            tableMetadataHeaderModels = new ArrayList<>();
        }

        return tableMetadataHeaderModels;
    }


    public void addTableHeaderMetadataModel(TableMetadataHeaderModel headerModel) {
        if (null == this.tableMetadataHeaderModels) {
            this.tableMetadataHeaderModels = new ArrayList<>();
        }
        this.tableMetadataHeaderModels.add(headerModel);
        setNoOfTables(this.tableMetadataHeaderModels.size());
    }

    private void setTableHeaderMetadataModels(List<TableMetadataHeaderModel> tableMetadataHeaderModels) {
        this.tableMetadataHeaderModels = tableMetadataHeaderModels;
        setNoOfTables(this.tableMetadataHeaderModels.size());
    }

    public String toHeaderString() {
        return super.toString();
    }

    public String toMetaString() {
        return String.format("[HEADER,%s,%d]", getDatabaseName(), getNoOfTables());
    }

    public List<String> toListString() {

        List<String> list = new ArrayList<>();

        list.add(toMetaString());

        for (TableMetadataHeaderModel tableMetadataHeaderModel : tableMetadataHeaderModels) {

            if (tableMetadataHeaderModel instanceof TableMetadataModel) {
                list.add(((TableMetadataModel) tableMetadataHeaderModel).toHeaderString());
            } else {
                list.add(tableMetadataHeaderModel.toString());
            }
        }

        return list;
    }

    public static DatabaseMetadataModel parse(List<String> lines) {

        String header = lines.get(0);
        String[] parts = header.substring(1, header.length() - 1).split(",");
        String databaseName = parts[1];

        List<TableMetadataHeaderModel> tableHeaders = new ArrayList<>();

        for (int index = 1; index < lines.size(); index++) {
            TableMetadataHeaderModel tableHeader = TableMetadataHeaderModel.parse(lines.get(index));
            tableHeaders.add(tableHeader);
        }

        return new DatabaseMetadataModel(databaseName, tableHeaders);
    }
}
