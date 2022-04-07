package ca.dal.database.query.model;

import java.util.List;
import java.util.Map;

public class QueryModel {
    private String rawQuery;
    private QueryType type;
    private String tableName;
    private Map<String, Object> condition;
    private List<String> columns;
    private List<Object> values;
    private String databaseName;
    private Map<String, String> columnDefinition;

    private QueryModel() {
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

    public Map<String, String> getColumnDefinition() {
        return columnDefinition;
    }

    public void setColumnDefinition(Map<String, String> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    public static void createDBQuery(String tableName, List<String> columns, Map<String, String> columnDefinition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setColumnDefinition(columnDefinition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_DATABASE);
    }

    public static void useDBQuery(String databaseName, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setDatabaseName(databaseName);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.USE_DATABASE);
    }

    public static void createTableQuery(String tableName, List<String> columns, Map<String, String> columnDefinition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setColumnDefinition(columnDefinition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_TABLE);
    }

    public static void selectQuery(String tableName, List<String> columns, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.SELECT_ROW);
    }

    public static void insertQuery(String tableName, List<String> columns, List<Object> values, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setValues(values);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.INSERT_ROW);
    }

    public static void updateQuery(String tableName, List<String> columns, List<Object> values, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setValues(values);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.UPDATE_ROW);
    }

    public static void deleteQuery(String tableName, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.DELETE_ROW);
    }

    public static void startTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.START_TRANSACTION);
    }

    public static void endTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.END_TRANSACTION);
    }

    public static void commitQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.COMMIT);
    }

    public static void rollbackQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.ROLLBACK);
    }
}
