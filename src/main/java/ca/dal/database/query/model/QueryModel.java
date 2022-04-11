package ca.dal.database.query.model;

import ca.dal.database.storage.model.column.ColumnMetadataModel;

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
    private List<ColumnMetadataModel> columnDefinition;

    private QueryModel() {
    }

    /**
     * @param type
     * @return model
     */
    public static QueryModel createCount(QueryType type) {
        QueryModel model = new QueryModel();
        model.setType(type);
        return model;
    }

    /**
     * @param type
     * @return model
     */
    public static QueryModel countUpdates(QueryType type) {
        QueryModel model = new QueryModel();
        model.setType(type);
        return model;
    }

    /**
     * @return columnDefinition
     */
    public List<ColumnMetadataModel> getColumnDefinition() {
        return columnDefinition;
    }

    /**
     * @param columnDefinition
     */
    public void setColumnDefinition(List<ColumnMetadataModel> columnDefinition) {
        this.columnDefinition = columnDefinition;
    }

    /**
     * @return rawQuery
     */
    public String getRawQuery() {
        return rawQuery;
    }

    /**
     * @param rawQuery
     */
    public void setRawQuery(String rawQuery) {
        this.rawQuery = rawQuery;
    }

    /**
     * @return type
     */
    public QueryType getType() {
        return type;
    }

    /**
     * @param type
     */
    public void setType(QueryType type) {
        this.type = type;
    }

    /**
     * @return tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return condition
     */
    public Map<String, Object> getCondition() {
        return condition;
    }

    /**
     * @param condition
     */
    public void setCondition(Map<String, Object> condition) {
        this.condition = condition;
    }

    /**
     * @return columns
     */
    public List<String> getColumns() {
        return columns;
    }

    /**
     * @param columns
     */
    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    /**
     * @return values
     */
    public List<Object> getValues() {
        return values;
    }

    /**
     * @param values
     */
    public void setValues(List<Object> values) {
        this.values = values;
    }

    /**
     * @return databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param databaseName
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @param databaseName
     * @param rawQuery
     * @return model
     */
    public static QueryModel createDBQuery(String databaseName, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setDatabaseName(databaseName);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_DATABASE);
        return model;
    }

    /**
     * @param databaseName
     * @param rawQuery
     * @return model
     */
    public static QueryModel useDBQuery(String databaseName, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setDatabaseName(databaseName);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.USE_DATABASE);
        return model;
    }

    /**
     * @param tableName
     * @param columnDefinition
     * @param rawQuery
     * @return model
     */
    public static QueryModel createTableQuery(String tableName, List<ColumnMetadataModel> columnDefinition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumnDefinition(columnDefinition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.CREATE_TABLE);
        return model;
    }

    /**
     * @param tableName
     * @param columns
     * @param values
     * @param rawQuery
     * @return model
     */
    public static QueryModel insertQuery(String tableName, List<String> columns, List<Object> values, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setValues(values);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.INSERT_ROW);
        return model;
    }

    /**
     * @param tableName
     * @param columns
     * @param condition
     * @param rawQuery
     * @return model
     */
    public static QueryModel selectQuery(String tableName, List<String> columns, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setColumns(columns);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.SELECT_ROW);
        return model;
    }

    /**
     * @param tableName
     * @param columns
     * @param values
     * @param condition
     * @param rawQuery
     * @return model
     */
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

    /**
     * @param tableName
     * @param condition
     * @param rawQuery
     * @return model
     */
    public static QueryModel deleteQuery(String tableName, Map<String, Object> condition, String rawQuery) {
        QueryModel model = new QueryModel();
        model.setTableName(tableName);
        model.setCondition(condition);
        model.setRawQuery(rawQuery);
        model.setType(QueryType.DELETE_ROW);
        return model;
    }

    /**
     * @param rawQuery
     * @return model
     */
    public static QueryModel startTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.START_TRANSACTION);
        return model;
    }

    /**
     * @param rawQuery
     * @return model
     */
    public static QueryModel endTransactionQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.END_TRANSACTION);
        return model;
    }

    /**
     * @param rawQuery
     * @return model
     */
    public static QueryModel commitQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.COMMIT);
        return model;
    }

    /**
     * @param rawQuery
     * @return model
     */
    public static QueryModel rollbackQuery(String rawQuery) {
        QueryModel model = new QueryModel();
        model.setRawQuery(rawQuery);
        model.setType(QueryType.ROLLBACK);
        return model;
    }

}
