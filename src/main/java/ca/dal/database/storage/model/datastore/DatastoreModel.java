package ca.dal.database.storage.model.datastore;

import ca.dal.database.storage.model.database.DatabaseMetadataHeaderModel;

import java.util.ArrayList;
import java.util.List;

public class DatastoreModel {

    private Integer noOfDatabase;

    private List<DatabaseMetadataHeaderModel> databaseMetadataHeaderModels;

    public DatastoreModel(Integer noOfDatabase) {
        this.noOfDatabase = noOfDatabase;
        this.databaseMetadataHeaderModels = new ArrayList<>();
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

    public String toMetaString() {
        return String.format("[HEADER,%d]", getNoOfDatabase());
    }

    public List<String> toListString() {

        List<String> list = new ArrayList<>();
        list.add(toMetaString());

        for (DatabaseMetadataHeaderModel databaseMetadataHeaderModel : databaseMetadataHeaderModels) {
            list.add(databaseMetadataHeaderModel.toString());
        }

        return list;
    }

    public void parse(List<String> list){



    }
}
