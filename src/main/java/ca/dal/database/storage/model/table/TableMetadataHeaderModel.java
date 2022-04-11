package ca.dal.database.storage.model.table;

public class TableMetadataHeaderModel {

    private String tableName;

    private Integer noOfColumns;

    public TableMetadataHeaderModel(String tableName, Integer noOfColumns) {
        this.tableName = tableName;
        this.noOfColumns = noOfColumns;
    }

    public String getTableName() {
        return tableName;
    }

    public Integer getNoOfColumns() {
        return noOfColumns;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setNoOfColumns(Integer noOfColumns) {
        this.noOfColumns = noOfColumns;
    }

    @Override
    public String toString() {
        return String.format("(%s,%d)", tableName, noOfColumns);
    }

    public static TableMetadataHeaderModel parse(String header) {
        String[] parts = header.substring(1, header.length() - 1).split(",");
        return new TableMetadataHeaderModel(parts[0], Integer.parseInt(parts[1]));
    }
}
