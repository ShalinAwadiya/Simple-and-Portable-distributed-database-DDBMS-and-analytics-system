package ca.dal.database.storage.model.column;

public class ColumnMetadataModel {

    private String name;

    private String type;

    private boolean primaryKey;

    private boolean foreignKey;

    private ForeignKeyConstraintModel foreignConstraint;

    public ColumnMetadataModel(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public ColumnMetadataModel(String name, String type, boolean primaryKey) {
        this.name = name;
        this.type = type;
        this.primaryKey = primaryKey;
    }

    public ColumnMetadataModel(String name, String type, String foreignTableName, String foreignColumnName) {
        this.name = name;
        this.type = type;
        this.primaryKey = false;
        this.foreignKey = true;
        this.foreignConstraint = ForeignKeyConstraintModel.create(foreignTableName, foreignColumnName);
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

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(boolean foreignKey) {
        this.foreignKey = foreignKey;
    }

    public ForeignKeyConstraintModel getForeignConstraint() {
        return foreignConstraint;
    }

    public void setForeignConstraint(ForeignKeyConstraintModel foreignConstraint) {
        this.foreignConstraint = foreignConstraint;
    }

    public static ColumnMetadataModel parse(String header) {
        String[] parts = header.substring(1, header.length() - 1).split(",");

        if (parts.length == 3 && parts[2].equalsIgnoreCase("PRIMARY_KEY")) {
            return new ColumnMetadataModel(parts[0], parts[1], true);
        } else if (parts.length == 5 && parts[2].equalsIgnoreCase("FOREIGN_KEY")) {
            return new ColumnMetadataModel(parts[0], parts[1], parts[3], parts[4]);
        } else {
            return new ColumnMetadataModel(parts[0], parts[1]);
        }
    }

    @Override
    public String toString() {

        if (isPrimaryKey()) {
            return String.format("(%s,%s,PRIMARY_KEY)", name, type);
        } else if (isForeignKey()) {
            return String.format("(%s,%s,FOREIGN_KEY,%s,%s)", name, type, getForeignConstraint().getTableName(), foreignConstraint.getColumnName());
        } else {
            return String.format("(%s,%s)", name, type);
        }
    }
}
