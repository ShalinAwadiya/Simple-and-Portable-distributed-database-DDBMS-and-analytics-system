package ca.dal.database.query;

import ca.dal.database.query.model.QueryModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueryParser {

    private static final Logger logger = Logger.getLogger(QueryParser.class.getName());

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String query = br.readLine();
        evaluateQuery(query);
    }

    public static void evaluateQuery(String query) {
        String[] token = query.split(" ");

        if (token.length == 0) {
            logger.log(Level.INFO, "EMPTY QUERY");
        } else {
            switch (token[0].toUpperCase()) {
                case "CREATE":
                    if (token[1].equalsIgnoreCase("DATABASE")) {
                        createDBQuery(token, query);
                    } else {
                        createTableQuery(token, query);
                    }
                    break;
                case "USE":
                    parseUseDBQuery(token, query);
                    break;
                case "INSERT":
                    insertQuery(token, query);
                    break;
                case "SELECT":
                    break;
                case "UPDATE":
                case "DELETE":
                default:
                    logger.log(Level.INFO, "INVALID QUERY");
            }
        }
    }

    public static void parseUseDBQuery(String[] token, String query) {
        String databaseName = token[1].toUpperCase();
        QueryModel.useDBQuery(databaseName, query);
    }

    public static void createDBQuery(String[] token, String query) {
        String databaseName = token[2].toUpperCase();
        QueryModel.createDBQuery(databaseName, query);
    }

    public static void createTableQuery(String[] token, String query) {
        String tableName = token[2].toUpperCase();
        String queryManipulation = query.substring(query.indexOf("(") + 1, query.length() - 1).trim();
        String[] queryToken = queryManipulation.split(",");
        Map<String, String> columnDefinition = new HashMap<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");
            columnDefinition.put(queryFinalToken[0], queryFinalToken[1]);
        }
        QueryModel.createTableQuery(tableName, columnDefinition, query);
    }

    public static void insertQuery(String[] token, String query) {
        String tableName = token[2].toUpperCase();
        String queryManipulation = query.substring(query.indexOf("(") + 1, query.indexOf(")")).trim();
        String[] queryToken = queryManipulation.split(",");
        List<String> columns = new ArrayList<>();
        for (int i = 0; i < queryToken.length; i++) {
            String[] queryFinalToken = queryToken[i].trim().split(" ");
            columns.add(queryFinalToken[0]);
        }
        String queryManipulationValues = query.substring(query.indexOf("(") + 1, query.indexOf(")")).trim();
        String[] queryTokenValues = queryManipulationValues.split(",");
        List<Object> values = new ArrayList<>();
        for (int i = 0; i < queryTokenValues.length; i++) {
            String[] queryFinalTokenValue = queryTokenValues[i].trim().split(" ");
            values.add(queryFinalTokenValue[0]);
        }
        QueryModel.insertQuery(tableName, columns, values, query);
    }
}
