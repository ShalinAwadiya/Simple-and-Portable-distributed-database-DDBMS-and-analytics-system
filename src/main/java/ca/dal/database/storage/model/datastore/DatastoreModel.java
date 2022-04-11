package ca.dal.database.storage.model.datastore;

import ca.dal.database.storage.model.database.DatabaseMetadataHeaderModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;

import java.util.ArrayList;
import java.util.List;

import static ca.dal.database.constant.ApplicationConstants.COMMA;

public class DatastoreModel {

    private Integer noOfDatabase;

    private List<DatabaseMetadataHeaderModel> databaseMetadataHeaderModels;

    public DatastoreModel(Integer noOfDatabase) {
        this.noOfDatabase = noOfDatabase;
        this.databaseMetadataHeaderModels = new ArrayList<>();
    }

    public DatastoreModel(Integer noOfDatabase, List<DatabaseMetadataHeaderModel> databaseMetadataHeaderModels) {
        this.noOfDatabase = noOfDatabase;
        this.databaseMetadataHeaderModels = databaseMetadataHeaderModels;
    }

    public Integer getNoOfDatabase() {
        return noOfDatabase;
    }

    public void setNoOfDatabase(Integer noOfDatabase) {
        this.noOfDatabase = noOfDatabase;
    }

    public List<DatabaseMetadataHeaderModel> getDatabaseMetadataHeaderModels() {
        return databaseMetadataHeaderModels;
    }

    public void setDatabaseMetadataHeaderModels(List<DatabaseMetadataHeaderModel> databaseMetadataHeaderModels) {
        this.databaseMetadataHeaderModels = databaseMetadataHeaderModels;
    }

    public void addDatabaseMetadataHeaderModels(DatabaseMetadataHeaderModel databaseMetadataHeaderModel) {
        if (this.databaseMetadataHeaderModels == null) {
            this.databaseMetadataHeaderModels = new ArrayList<>();
        }
        this.databaseMetadataHeaderModels.add(databaseMetadataHeaderModel);
        this.noOfDatabase = this.databaseMetadataHeaderModels.size();
    }

    public String toMetaString() {
        return String.format("[HEADER,%d]", getNoOfDatabase());
    }

    public List<String> toListString() {

        List<String> list = new ArrayList<>();
        list.add(toMetaString());

        for (DatabaseMetadataHeaderModel databaseMetadataHeaderModel : databaseMetadataHeaderModels) {

            if (databaseMetadataHeaderModel instanceof DatabaseMetadataModel) {
                list.add(((DatabaseMetadataModel) databaseMetadataHeaderModel).toHeaderString());
            } else {
                list.add(databaseMetadataHeaderModel.toString());
            }
        }

        return list;
    }

    public static DatastoreModel parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return null;
        }

        String headers = lines.get(0);
        String[] parts = headers.substring(1, headers.length() - 1).split(COMMA);

        List<DatabaseMetadataHeaderModel> values = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            values.add(DatabaseMetadataHeaderModel.parse(lines.get(i)));
        }

        return new DatastoreModel(Integer.parseInt(parts[1]), values);
    }
}
