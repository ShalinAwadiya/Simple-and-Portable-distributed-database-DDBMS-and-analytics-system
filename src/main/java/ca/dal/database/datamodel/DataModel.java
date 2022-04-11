package ca.dal.database.datamodel;

import ca.dal.database.connection.Connection;
import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.column.ForeignKeyConstraintModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.row.RowModel;
import ca.dal.database.storage.model.table.TableMetadataHeaderModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ca.dal.database.utils.PrintUtils.error;

public class DataModel {

    /**
     * @param database
     */

    public int createERD(String database) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(
                    new FileWriter("src/main/resources/erd/" + database + "_ERD.erd"));
            bufferedWriter.write("\t\t\t\t\t\t\t************Reverse Engineering Model************\n");
            StorageManager storageManager = new StorageManager(new Connection("SYSTEM"));
            DatabaseMetadataModel databaseMetadataModel = storageManager.getDatabaseMetadata(database);
            List<TableMetadataHeaderModel> tableNames = databaseMetadataModel.getTableHeaderMetadataModels();

            for (TableMetadataHeaderModel tableNameModel : tableNames) {
                String tableName = tableNameModel.getTableName();
                TableMetadataModel tableMetadataModel = storageManager.getTableMetadata(database, tableName);
                List<ColumnMetadataModel> tableColumnMetaData = tableMetadataModel.getColumnsMetadata();
                bufferedWriter.append("\n");
                bufferedWriter.write("+---------------------+" + "\n");
                bufferedWriter.write(tableName + "\n");
                bufferedWriter.write("+---------------------+" + "\n");
                int count = 0;
                for (ColumnMetadataModel metadataModel : tableColumnMetaData) {
                    String primaryKey = "";
                    String foreignKey = "";
                    String columnName = metadataModel.getName();
                    String columnType = metadataModel.getType();
                    ForeignKeyConstraintModel foreignKeyModel;
                    bufferedWriter.write(columnName + "\n");
                    if (columnName != null && columnType != null) {
                    }
                    if (metadataModel.isPrimaryKey()) {
                        primaryKey = columnName;
                    }
                    if (metadataModel.isForeignKey()) {
                        foreignKey = columnName;
                    }
                    if (primaryKey != "") {
                        bufferedWriter.write("PK | " + primaryKey + "\n");
                    }
                    if (foreignKey != "") {
                        bufferedWriter.write("FK | " + foreignKey + "\n");
                        bufferedWriter.write(
                                "** N->" + checkCardinality(storageManager.fetchAllRows(database, tableName), count)
                                        + " relationship with below **\n");
                        foreignKeyModel = metadataModel.getForeignConstraint();
                        bufferedWriter.write("Foreign key table: " + foreignKeyModel.getTableName() + "\n");
                        bufferedWriter.write("Foreign key reference: " + foreignKeyModel.getColumnName() + "\n");
                    }
                    count++;
                }
            }
            bufferedWriter.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            error("Something went wrong, Please try again");
            return -1;
        }
    }

    private String checkCardinality(List<RowModel> rowModelList, int postition) {
        List<String> list = new ArrayList<>();
        for (RowModel rowModel : rowModelList) {
            String value = (String) rowModel.getValues().get(postition);
            if (!list.isEmpty() && list.contains(value)) {
                return "N";
            } else {
                list.add(value);
            }
        }
        return "1";
    }
}