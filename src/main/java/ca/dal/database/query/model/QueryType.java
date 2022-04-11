package ca.dal.database.query.model;

public enum QueryType {
    CREATE_DATABASE("CREATE_DATABASE"), USE_DATABASE("USE_DATABASE"),
    CREATE_TABLE("CREATE_TABLE"),
    INSERT_ROW("INSERT_ROW"), SELECT_ROW("SELECT_ROW"), UPDATE_ROW("UPDATE_ROW"), DELETE_ROW("DELETE_ROW"),
    START_TRANSACTION("START_TRANSACTION"), END_TRANSACTION("END_TRANSACTION"),
    COMMIT("COMMIT"), ROLLBACK("ROLLBACK"),
    COUNT_QUERIES("COUNT_QUERIES"), COUNT_UPDATE("COUNT_UPDATE");

    private final String value;

    /**
     * @param value
     */
    QueryType(String value) {
        this.value = value;
    }

    /**
     * @return value
     */
    public String getValue() {
        return value;
    }
}
