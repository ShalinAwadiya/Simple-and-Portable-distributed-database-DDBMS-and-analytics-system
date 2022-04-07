package ca.dal.database.storage.model;

import static ca.dal.database.utils.StringUtils.isEmpty;

public class ColumnMetadataModel {

    private String name;

    private String type;

    private String constraints;

    public ColumnMetadataModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ColumnMetadataModel(String name, String type, String constraints) {
        this.name = name;
        this.type = type;
        this.constraints = constraints;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    @Override
    public String toString() {

        if(isEmpty(constraints)){
            return "("  + name + "," + type + ")";
        }

        return "("  + name + "," + type + "," +constraints + ")";
    }
}
