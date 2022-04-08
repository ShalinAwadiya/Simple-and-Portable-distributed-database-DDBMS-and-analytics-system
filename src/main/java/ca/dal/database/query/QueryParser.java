package ca.dal.database.query;

import ca.dal.database.query.model.QueryModel;
import ca.dal.database.storage.model.column.ColumnMetadataModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryParser {

    private static final Logger logger = Logger.getLogger(QueryParser.class.getName());

    public static QueryModel evaluateQuery(String query) {
        String[] token = query.split(" ");
        List<String> columns = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Map<String, Object> condition = new HashMap<>();

        if (token.length == 0) {
            logger.log(Level.INFO, "EMPTY QUERY");
        } else {
            switch (token[0].toUpperCase()) {
                case "CREATE":
                    if (token[1].equalsIgnoreCase("DATABASE")) {
                        return createDBQuery(token, query);
                    } else if (token[1].equalsIgnoreCase("TABLE")) {
                        return createTableQuery(token, query);
                    } else {
                        logger.log(Level.INFO, "Enter Valid Create Query");
                    }
                    break;
                case "USE":
                    parseUseDBQuery(token, query);
                    break;
                case "INSERT":
                    return insertQuery(token, query);
                case "SELECT":
                    selectQuery(query, columns, condition);
                    break;
                case "UPDATE":
                    updateQuery(token, query, columns, values, condition);
                    break;
                case "DELETE":
                    deleteQuery(token, query);
                    break;
                default:
                    logger.log(Level.INFO, "INVALID QUERY");
            }
        }
        return null;
    }

    public static void parseUseDBQuery(String[] token, String query) {
        if (token.length == 2) {
            String databaseName = token[1].toUpperCase();
            QueryModel.useDBQuery(databaseName, query);
        } else {
            logger.log(Level.INFO, "Enter Valid Use Database Query");
        }

    }

    public static QueryModel createDBQuery(String[] token, String query) {
        if (token.length == 3) {
            String databaseName = token[2].toUpperCase();
            return QueryModel.createDBQuery(databaseName, query);
        } else {
            logger.log(Level.INFO, "Enter Valid Create Database Query");
        }
        return null;
    }

    public static QueryModel createTableQuery(String[] token, String query) {
        // if(token[2].matches("^[a-zA-Z]")){
        // System.out.println("INSIDE REGEX");
        // }
        String tableName = token[2].toUpperCase();
        String queryManipulation = query.substring(query.indexOf("(") + 1, query.length() - 1).trim();
        String[] queryToken = queryManipulation.split(",");
        List<ColumnMetadataModel> columnDefinition = new ArrayList<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");

            //add foreign key primary key logic
            columnDefinition.add(new ColumnMetadataModel(queryFinalToken[0], queryFinalToken[1], true));
        }
        return QueryModel.createTableQuery(tableName, columnDefinition, query);
    }

    public static QueryModel insertQuery(String[] token, String query) {
        String tableName = token[2].toUpperCase();
        String queryManipulation = query.substring(query.indexOf("(") + 1, query.indexOf(")")).trim();
        String[] queryToken = queryManipulation.split(",");
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");
            columns.add(queryFinalToken[0]);
        }
        String queryManipulationValues = query.substring(query.indexOf("(", query.indexOf(")") + 1) + 1, query.length() - 1);
        String[] queryTokenValues = queryManipulationValues.split(",");
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < queryTokenValues.length; i++) {
            String[] queryFinalTokenValue = queryTokenValues[i].trim().split(" ");
            values.add(queryFinalTokenValue[0]);
        }
        return QueryModel.insertQuery(tableName, columns, values, query);
    }

    public static void deleteQuery(String[] token, String query) {
        if (token.length == 5) {
            String tableName = token[2].toUpperCase();
            Map<String, Object> condition = new HashMap<>();
            String queryManipulation = query.substring(query.indexOf("where"), query.length() - 1).trim();
            String[] queryToken = queryManipulation.split(" ");
            String[] conditionLogic = queryToken[1].split("=");
            condition.put(conditionLogic[0], conditionLogic[1]);
            QueryModel.deleteQuery(tableName, condition, query);
        } else {
            logger.log(Level.INFO, "Enter Valid Delete Query");
        }
    }

    public static void updateQuery(String[] token, String query, List<String> columns, List<Object> values, Map<String, Object> condition) {
        String tableName = token[1].toUpperCase();
        String queryManipulation = query.substring(query.indexOf("set"), query.length() - 1).trim();
        String[] queryToken = queryManipulation.split(" ");
        String[] columnLogic = queryToken[1].split("=");
        columns.add(columnLogic[0]);
        values.add(columnLogic[1]);
        String[] conditionLogic = queryToken[3].split("=");
        condition.put(conditionLogic[0], conditionLogic[1]);
        QueryModel.updateQuery(tableName, columns, values, condition, query);
    }

    public static void selectQuery(String query, List<String> columns, Map<String, Object> condition) {
        String substring = query.substring(query.indexOf("from"), query.length() - 1);
        String[] queryToken = substring.split(" ");
        String tableName = queryToken[1].toUpperCase();
        String columnsSelect = query.substring(query.indexOf("select") + 7, query.indexOf("from"));
        String[] selectTokenSplit = columnsSelect.split(" ");
        String colResult = selectTokenSplit[0].substring(0, selectTokenSplit[0].length() - 1);
        String[] colResultArr = colResult.split(" ");
        columns.add(colResultArr[0]);
        columns.add(selectTokenSplit[1]);
        String queryManipulation = query.substring(query.indexOf("where"), query.length() - 1).trim();
        String[] queryTokenNew = queryManipulation.split(" ");
        String[] conditionLogic = queryTokenNew[1].split("=");
        condition.put(conditionLogic[0], conditionLogic[1]);
        QueryModel.selectQuery(tableName, columns, condition, query);
    }
}
