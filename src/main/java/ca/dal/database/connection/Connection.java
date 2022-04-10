package ca.dal.database.connection;

public class Connection {

    private final String userId;
    private boolean autoCommit;
    private String databaseName;

    public Connection(String userId) {
        this.userId = userId;
        this.autoCommit = true;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getUserId() {
        return userId;
    }
}
