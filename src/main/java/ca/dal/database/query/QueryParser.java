package ca.dal.database.query;

import ca.dal.database.analytics.CountQueries;
import ca.dal.database.analytics.CountUpdates;
import ca.dal.database.connection.Connection;
import ca.dal.database.logger.QueryLog;
import ca.dal.database.query.model.QueryModel;
import ca.dal.database.query.model.QueryType;
import ca.dal.database.storage.model.column.ColumnMetadataModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ca.dal.database.utils.MapUtils.of;
import static ca.dal.database.utils.PrintUtils.error;
import static ca.dal.database.utils.StringUtils.replace;
import static ca.dal.database.utils.StringUtils.splitAndTrim;
import static java.util.Arrays.asList;

public class QueryParser {

    private static final QueryLog queryLog = new QueryLog();

    /**
     * @param connection
     * @param query
     * @return queryModel or null
     */
    public static QueryModel evaluateQuery(Connection connection, String query) {

        if (query.charAt(query.length() - 1) == ';') {
            String newQuery = query.substring(0, query.length() - 1);

            String[] token = newQuery.split(" ");
            List<String> columns = new ArrayList<>();
            List<Object> values = new ArrayList<>();
            Map<String, Object> conditionNew = new LinkedHashMap<>();

            QueryModel queryModel = null;
            if (token.length == 0) {
                error("EMPTY QUERY");
            } else {
                switch (token[0].toUpperCase()) {
                    case "CREATE":
                        if (token[1].equalsIgnoreCase("DATABASE")) {
                            queryModel = createDBQuery(token, newQuery);
                        } else if (token[1].equalsIgnoreCase("TABLE")) {
                            queryModel = createTableQuery(token, newQuery);
                        } else {
                            error("Enter Valid Create Query");
                        }
                        break;
                    case "USE":
                        queryModel = useDBQuery(token, newQuery);
                        break;
                    case "INSERT":
                        queryModel = insertQuery(token, newQuery);
                        break;
                    case "SELECT":
                        queryModel = selectQuery(newQuery, columns, conditionNew);
                        break;
                    case "UPDATE":
                        queryModel = updateQuery(token, newQuery, columns, values, conditionNew);
                        break;
                    case "DELETE":
                        queryModel = deleteQuery(token, newQuery);
                        break;
                    case "START":
                        if (token[1].equalsIgnoreCase("TRANSACTION")) {
                            queryModel = startTransactionQuery(newQuery);
                        } else {
                            error("Enter Valid Transaction Query");
                        }
                        break;
                    case "END":
                        queryModel = endTransactionQuery(newQuery);
                        break;
                    case "COMMIT":
                        queryModel = commitQuery(newQuery);
                        break;
                    case "ROLLBACK":
                        queryModel = rollbackQuery(newQuery);
                        break;
                    case "COUNT":
                        if (token[1].equalsIgnoreCase("QUERIES")) {
                            CountQueries countQueries = new CountQueries();
                            countQueries.getQueryCount();
                            queryModel = QueryModel.createCount(QueryType.COUNT_QUERIES);
                        } else if (token[1].equalsIgnoreCase("UPDATE")) {
                            String countDatabaseName = token[2];
                            CountUpdates countUpdates = new CountUpdates();
                            countUpdates.countUpdates(countDatabaseName);
                            queryModel = QueryModel.countUpdates(QueryType.COUNT_UPDATE);
                        } else {
                            error("Enter Valid Count Query");
                        }
                        break;
                    default:
                        error("Invalid Query, Try again");
                }
            }

            if (queryModel != null) {
                queryLog.writeLog("Information Log", "Query - " + queryModel.getType().toString(), "Query executed by a user.", of("database", connection.getDatabaseName(), "query", null, "table", queryModel.getTableName(), "username", connection.getUserId()));

            } else {
                queryLog.writeLog("Error Log", "Query - null", "Invalid Query executed by a user.", of("database", connection.getDatabaseName(), "query", null, "table", "null", "username", connection.getUserId()));
            }
            return queryModel;
        } else {
            error("Semicolon (;) missing");
        }
        return null;
    }

    /**
     * @param token
     * @param newQuery
     * @return useDBQuery or null
     */
    public static QueryModel useDBQuery(String[] token, String newQuery) {
        if (token.length == 2) {
            String databaseName = token[1];
            return QueryModel.useDBQuery(databaseName, newQuery);
        } else {
            error("Enter Valid Use Database Query");
        }
        return null;
    }

    /**
     * @param token
     * @param newQuery
     * @return createDBQuery or null
     */
    public static QueryModel createDBQuery(String[] token, String newQuery) {
        if (token.length == 3) {
            String databaseName = token[2];
            return QueryModel.createDBQuery(databaseName, newQuery);
        } else {
            error("Enter Valid Create Database Query");
        }
        return null;
    }

    /**
     * @param token
     * @param newQuery
     * @return createTableQuery or null
     */
    public static QueryModel createTableQuery(String[] token, String newQuery) {
        String tableName = token[2];
        String queryManipulation = newQuery.substring(newQuery.indexOf("(") + 1, newQuery.length() - 1).trim();
        String[] queryToken = queryManipulation.split(",");
        List<ColumnMetadataModel> columnDefinition = new ArrayList<>();

        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");

            if (queryFinalToken.length >= 3) {
                if (queryFinalToken[2].equalsIgnoreCase("primary")) {
                    columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1], true));
                } else if (queryFinalToken[2].equalsIgnoreCase("foreign")) {
                    String[] newQueryToken = queryFinalToken[5].split("\\(");
                    String subQueryToken = newQueryToken[1].substring(0, newQueryToken[1].indexOf(")"));

                    columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1], newQueryToken[0], subQueryToken));
                }
            } else {
                columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1]));
            }
        }
        return QueryModel.createTableQuery(tableName, columnDefinition, newQuery);
    }

    /**
     * @param token
     * @param newQuery
     * @return insertQuery or null
     */
    public static QueryModel insertQuery(String[] token, String newQuery) {
        String tableName = token[2];
        String queryManipulation = newQuery.substring(newQuery.indexOf("(") + 1, newQuery.indexOf(")")).trim();
        String[] queryToken = queryManipulation.split(",");
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");
            columns.add(queryFinalToken[0]);
        }
        String queryManipulationValues = newQuery.substring(newQuery.indexOf("(", newQuery.indexOf(")") + 1) + 1, newQuery.length() - 1).trim();

        String[] queryTokenValues = splitAndTrim(queryManipulationValues, ",");
        queryTokenValues = replace(queryTokenValues, "(\"|')", "");

        return QueryModel.insertQuery(tableName, columns, asList(queryTokenValues), newQuery);
    }

    /**
     * @param token
     * @param newQuery
     * @return deleteQuery or null
     */
    public static QueryModel deleteQuery(String[] token, String newQuery) {
        if (token.length == 5) {
            String tableName = token[2];
            Map<String, Object> conditionNew = new LinkedHashMap<>();
            queryManipulation(newQuery, conditionNew);
            return QueryModel.deleteQuery(tableName, conditionNew, newQuery);
        } else {
            error("Enter Valid Delete Query");
        }
        return null;
    }

    /**
     * @param token
     * @param newQuery
     * @param columns
     * @param values
     * @param conditionNew
     * @return updateQuery or null
     */
    public static QueryModel updateQuery(String[] token, String newQuery, List<String> columns, List<Object> values, Map<String, Object> conditionNew) {

        String tableName = token[1];

        if (!token[2].equalsIgnoreCase("set")) {
            error("Invalid update query");
        }

        String queryManipulation = newQuery.substring(newQuery.indexOf(token[2])).trim();
        String[] queryToken = splitAndTrim(queryManipulation, " ");
        String[] columnLogic = replace(splitAndTrim(queryToken[1], "="), "(\"|')", "");
        columns.add(columnLogic[0]);
        values.add(columnLogic[1]);

        String[] conditionLogic = replace(splitAndTrim(queryToken[3], "="), "(\"|')", "");
        conditionNew.put(conditionLogic[0], conditionLogic[1]);

        return QueryModel.updateQuery(tableName, columns, values, conditionNew, newQuery);
    }

    /**
     * @param newQuery
     * @param columns
     * @param conditionNew
     * @return selectQuery or null
     */
    public static QueryModel selectQuery(String newQuery, List<String> columns, Map<String, Object> conditionNew) {
        String substring = newQuery.substring(newQuery.indexOf("from"));
        String[] queryToken = substring.split(" ");
        String tableName = queryToken[1];
        String columnsSelect = newQuery.substring(newQuery.indexOf("select") + 7, newQuery.indexOf("from") - 1);

        String[] selectTokenSplit = columnsSelect.split(",");

        for (int i = 0; i < selectTokenSplit.length; i++) {
            columns.add(selectTokenSplit[i]);
        }
        queryManipulation(newQuery, conditionNew);

        return QueryModel.selectQuery(tableName, columns, conditionNew, newQuery);
    }

    /**
     * @param newQuery
     * @return startTransaction or null
     */
    public static QueryModel startTransactionQuery(String newQuery) {
        return QueryModel.startTransactionQuery(newQuery);
    }

    /**
     * @param newQuery
     * @return endTransaction or null
     */
    public static QueryModel endTransactionQuery(String newQuery) {
        return QueryModel.endTransactionQuery(newQuery);
    }

    /**
     * @param newQuery
     * @return commitQuery or null
     */
    public static QueryModel commitQuery(String newQuery) {
        return QueryModel.commitQuery(newQuery);
    }

    /**
     * @param newQuery
     * @return rollbackQuery or null
     */
    public static QueryModel rollbackQuery(String newQuery) {
        return QueryModel.rollbackQuery(newQuery);
    }

    /**
     * @param newQuery
     * @param conditionNew
     */
    private static void queryManipulation(String newQuery, Map<String, Object> conditionNew) {
        if (!newQuery.contains("where")) {
            return;
        }

        String queryManipulation = newQuery.substring(newQuery.indexOf("where")).trim();
        String[] queryTokenNew = splitAndTrim(queryManipulation, " ");

        String[] conditionLogic = replace(splitAndTrim(queryTokenNew[1], "="), "(\"|')", "");
        conditionNew.put(conditionLogic[0], conditionLogic[1]);

    }
}
