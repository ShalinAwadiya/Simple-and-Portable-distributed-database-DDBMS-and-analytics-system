package ca.dal.database.query;

import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.model.column.ColumnMetadataModel;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Nishit Mistry
 */
public class QueryParser {

    private static final Logger logger = Logger.getLogger(QueryParser.class.getName());

    public static QueryModel evaluateQuery(String query) {
        String newQuery = query.substring(0, query.length() - 1);
        String[] token = newQuery.split(" ");
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Map<String, Object> conditionNew = new LinkedHashMap<>();

        if (token.length == 0) {
            logger.log(Level.INFO, "EMPTY QUERY");
        } else {
            switch (token[0].toUpperCase()) {
                case "CREATE":
                    if (token[1].equalsIgnoreCase("DATABASE")) {
                        return createDBQuery(token, newQuery);
                    } else if (token[1].equalsIgnoreCase("TABLE")) {
                        return createTableQuery(token, newQuery);
                    } else {
                        logger.log(Level.INFO, "Enter Valid Create Query");
                    }
                    break;
                case "USE":
                    return useDBQuery(token, newQuery);
                case "INSERT":
                    return insertQuery(token, newQuery);
                case "SELECT":
                    return selectQuery(newQuery, columns, conditionNew);
                case "UPDATE":
                    return updateQuery(token, newQuery, columns, values, conditionNew);
                case "DELETE":
                    return deleteQuery(token, newQuery);
                default:
                    logger.log(Level.INFO, "INVALID QUERY");
            }
        }
        return null;
    }

    public static QueryModel useDBQuery(String[] token, String newQuery) {
        if (token.length == 2) {
            String databaseName = token[1];
            return QueryModel.useDBQuery(databaseName, newQuery);
        } else {
            logger.log(Level.INFO, "Enter Valid Use Database Query");
        }
        return null;
    }

    public static QueryModel createDBQuery(String[] token, String newQuery) {
        if (token.length == 3) {
            String databaseName = token[2];
            return QueryModel.createDBQuery(databaseName, newQuery);
        } else {
            logger.log(Level.INFO, "Enter Valid Create Database Query");
        }
        return null;
    }

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

                    columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1],
                            newQueryToken[0], subQueryToken));
                }
            } else {
                columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1]));
            }
        }
        return QueryModel.createTableQuery(tableName, columnDefinition, newQuery);
    }

    public static QueryModel insertQuery(String[] token, String newQuery) {
        String tableName = token[2];
        String queryManipulation = newQuery.substring(newQuery.indexOf("(") + 1, newQuery.indexOf(")")).trim();
        String[] queryToken = queryManipulation.split(",");
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");
            columns.add(queryFinalToken[0]);
        }
        String queryManipulationValues = newQuery.substring(newQuery.indexOf("(", newQuery.indexOf(")") + 1) + 1).trim();
        String[] queryTokenValues = queryManipulationValues.split(",");
        List<Object> values = new ArrayList<>();
        String queryTokenNew = queryTokenValues[1].substring(queryTokenValues[1].indexOf("\"") + 1,
                queryTokenValues[1].length() - 2);

        values.add(queryTokenValues[0]);
        values.add(queryTokenNew);

        return QueryModel.insertQuery(tableName, columns, values, newQuery);
    }

    public static QueryModel deleteQuery(String[] token, String newQuery) {
        if (token.length == 5) {
            String tableName = token[2];
            Map<String, Object> conditionNew = new LinkedHashMap<>();
            queryManipulation(newQuery, conditionNew);
            return QueryModel.deleteQuery(tableName, conditionNew, newQuery);
        } else {
            logger.log(Level.INFO, "Enter Valid Delete Query");
        }
        return null;
    }

    public static QueryModel updateQuery(String[] token, String newQuery, List<String> columns, List<Object> values,
                                         Map<String, Object> conditionNew) {
        String tableName = token[1];
        String queryManipulation = newQuery.substring(newQuery.indexOf("set"), newQuery.length() - 1).trim();
        String[] queryToken = queryManipulation.split(" ");
        String[] columnLogic = queryToken[1].split("=");
        columns.add(columnLogic[0]);
        String queryTokenNew = columnLogic[1].substring(columnLogic[1].indexOf("\"") + 1, columnLogic[1].length() - 1);

        values.add(queryTokenNew);
        String[] conditionLogic = queryToken[3].split("=");

        String conditionLogicNew = conditionLogic[1].substring(conditionLogic[1].indexOf("\"") + 1,
                conditionLogic[1].length());
        conditionNew.put(conditionLogic[0], conditionLogicNew);

        return QueryModel.updateQuery(tableName, columns, values, conditionNew, newQuery);
    }

    public static QueryModel selectQuery(String newQuery, List<String> columns, Map<String, Object> conditionNew) {
        String substring = newQuery.substring(newQuery.indexOf("from"), newQuery.length() - 1);
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

    private static void queryManipulation(String newQuery, Map<String, Object> conditionNew) {
        if (!newQuery.contains("where")) {
            return;
        }

        String queryManipulation = newQuery.substring(newQuery.indexOf("where"), newQuery.length() - 1).trim();
        String[] queryTokenNew = queryManipulation.split(" ");
        String[] conditionLogic = queryTokenNew[1].split("=");

        String conditionLogicNew = conditionLogic[1].substring(conditionLogic[1].indexOf("\"") + 1,
                conditionLogic[1].length());

        conditionNew.put(conditionLogic[0], conditionLogicNew);
    }
}
