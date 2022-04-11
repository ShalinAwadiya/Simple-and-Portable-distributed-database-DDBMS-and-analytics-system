package ca.dal.database.storage.model.database;

public class DatabaseMetadataHeaderModel {

    private String databaseName;

    private Integer noOfTables;

    public DatabaseMetadataHeaderModel(String databaseName, Integer noOfTables) {
        this.databaseName = databaseName;
        this.noOfTables = noOfTables;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Integer getNoOfTables() {
        return noOfTables;
    }

    public void setNoOfTables(Integer noOfTables) {
        this.noOfTables = noOfTables;
    }

    @Override
    public String toString() {
        return String.format("(%s,%d)", databaseName, noOfTables);
    }


    public static DatabaseMetadataHeaderModel parse(String header) {
        String[] parts = header.substring(1, header.length() - 1).split(",");
        return new DatabaseMetadataHeaderModel(parts[0], Integer.parseInt(parts[1]));
    }
}
