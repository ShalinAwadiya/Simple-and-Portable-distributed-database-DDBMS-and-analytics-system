package ca.dal.database.transaction;

import ca.dal.database.transaction.model.TransactionModel;

import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.Collections.emptyList;

/**
 * @author Harsh Shah
 */
public class TransactionManager {

    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());


    public TransactionModel start() {
        logger.log(Level.INFO, "Start Transaction...");
        return new TransactionModel();
    }

    public void perform(TransactionModel transaction, String query) {

        transaction.addQuery(query);

        // get table name from the query
        String tableName = query;

        switch (query) {
            case "select":

                // fetch from the database + local datastore
                // merge the data with local buffer
                // get newly added data
                // check if there is any update on data
                // check if there is any deletion on data

                break;
            case "insert":

                // insert values in local datastore
                transaction.addInDatastore(tableName, emptyList());
                break;
            case "update":

                // fetch the value from the database + local datastore

                // update the data in local datastore
                transaction.updateInDatastore(tableName, "rowIdentifier", emptyList());
                break;
            case "delete":

                // fetch the value from the database + local datastore

                // delete the data in local datastore
                transaction.deleteInDatastore(tableName, "rowIdentifier");
                break;

            default:
        }
    }


}
