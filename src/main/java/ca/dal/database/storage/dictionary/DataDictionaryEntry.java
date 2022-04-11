package ca.dal.database.storage.dictionary;

import ca.dal.database.config.ApplicationConfiguration;

import java.util.Objects;

import static ca.dal.database.constant.ApplicationConstants.COMMA;
import static ca.dal.database.utils.StringUtils.isEmpty;

public class DataDictionaryEntry {

    private boolean lock;

    private String instanceName;

    private String databaseName;

    private String tableName;

    public DataDictionaryEntry(String databaseName) {
        this.instanceName = ApplicationConfiguration.getInstance().getIdentifier();
        this.databaseName = databaseName;
    }

    public DataDictionaryEntry(String databaseName, String tableName) {
        this.instanceName = ApplicationConfiguration.getInstance().getIdentifier();
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public DataDictionaryEntry(String instanceName, String databaseName, String tableName, boolean lock) {
        this.lock = lock;
        this.instanceName = instanceName;
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public static DataDictionaryEntry parse(String line){
        line = line.substring(1, line.length()-1);
        String[] parts = line.split(COMMA);
        return new DataDictionaryEntry(parts[0], parts[1], parts[2], parts[3].equalsIgnoreCase("LOCK"));
    }


    @Override
    public String toString() {
        return String.format("(%s,%s,%s,%s)", instanceName, databaseName,
                isEmpty(tableName) ? "" : tableName, lock ? "LOCK" : "UNLOCK");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataDictionaryEntry that = (DataDictionaryEntry) o;
        return Objects.equals(databaseName, that.databaseName)
                && Objects.equals(tableName, that.tableName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lock, instanceName, databaseName, tableName);
    }
}
