package ca.dal.database.connection;

public class Connection {

    private boolean autoCommit;
    private String databaseName;
    private String userId;

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public String getUserId() {
        return userId;
    }

    public Connection(String userId) {
        this.userId = userId;
    }
}
