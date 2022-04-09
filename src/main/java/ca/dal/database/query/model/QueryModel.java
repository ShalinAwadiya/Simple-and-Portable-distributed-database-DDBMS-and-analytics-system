package ca.dal.database.query.model;

import ca.dal.database.storage.model.column.ColumnMetadataModel;

import java.util.List;
import java.util.Map;

/**
 * @author Nishit Mistry
 */
public class QueryModel {
    private String rawQuery;
    private QueryType type;
    private String tableName;
    private Map<String, Object> condition;
    private List<String> columns;
    private List<Object> values;
    private String databaseName;
    private List<ColumnMetadataModel> columnDefinition;

    private QueryModel() {
    }

    public List<ColumnMetadataModel> getColumnDefinition() {
        return columnDefinition;
    }

    public void setColumnDefinition(List<ColumnMetadataModel> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public String getRawQuery() {
        return rawQuery;
    }

    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    public QueryType getType() {
        return type;
    }

    public void setType(QueryType type) {
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Object> getCondition() {
        return condition;
    }

    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public static QueryModel createDBQuery(String databaseName, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setDatabaseName(databaseName);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_DATABASE);
        return model;
    }

    public static QueryModel useDBQuery(String databaseName, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setDatabaseName(databaseName);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.USE_DATABASE);
        return model;
    }

    public static QueryModel createTableQuery(String tableName, List<ColumnMetadataModel> columnDefinition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumnDefinition(columnDefinition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_TABLE);
        return model;
    }

    public static QueryModel insertQuery(String tableName, List<String> columns, List<Object> values, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setValues(values);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.INSERT_ROW);
        return model;
    }

    public static QueryModel selectQuery(String tableName, List<String> columns, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.SELECT_ROW);
        return model;
    }

    public static QueryModel updateQuery(String tableName, List<String> columns, List<Object> values, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setValues(values);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.UPDATE_ROW);
        return model;
    }

    public static QueryModel deleteQuery(String tableName, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.DELETE_ROW);
        return model;
    }

    public static QueryModel startTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.START_TRANSACTION);
        return model;
    }

    public static QueryModel endTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.END_TRANSACTION);
        return model;
    }

    public static QueryModel commitQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.COMMIT);
        return model;
    }

    public static QueryModel rollbackQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.ROLLBACK);
        return model;
    }
}
