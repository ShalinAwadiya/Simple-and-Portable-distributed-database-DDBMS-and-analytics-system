package ca.dal.database.query.model;

/**
 * @author Nishit Mistry
 */
public enum QueryType {
    CREATE_DATABASE, USE_DATABASE,
    CREATE_TABLE,
    INSERT_ROW, SELECT_ROW, UPDATE_ROW, DELETE_ROW,
    START_TRANSACTION, END_TRANSACTION, COMMIT, ROLLBACK
}
