package ca.dal.database.storage.model.column;

public class ForeignKeyConstraintModel {

    private String tableName;

    private String columnName;

    public ForeignKeyConstraintModel(String tableName, String columnName) {
        this.tableName = tableName;
        this.columnName = columnName;
    }

    public static ForeignKeyConstraintModel create(String tableName, String columnName) {
        return new ForeignKeyConstraintModel(tableName, columnName);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
