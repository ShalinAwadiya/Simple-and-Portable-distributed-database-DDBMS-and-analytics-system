package ca.dal.database.query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
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
                case "USE":
                case "INSERT":
                case "SELECT":
                    if(Objects.equals(token[1], "*")){

                    }

                    break;
                case "UPDATE":
                case "DELETE":
                default:
                    logger.log(Level.INFO, "INVALID QUERY");
            }
        }
    }
}
