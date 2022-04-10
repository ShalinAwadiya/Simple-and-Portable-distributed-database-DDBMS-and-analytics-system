package ca.dal.database.datamodel;

import ca.dal.database.storage.StorageManager;
import ca.dal.database.storage.model.column.ColumnMetadataModel;
import ca.dal.database.storage.model.column.ForeignKeyConstraintModel;
import ca.dal.database.storage.model.database.DatabaseMetadataModel;
import ca.dal.database.storage.model.table.TableMetadataHeaderModel;
import ca.dal.database.storage.model.table.TableMetadataModel;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static ca.dal.database.utils.PrintUtils.error;

/**
 * @author Meghdoot Ojha
 */
public class DataModel {
    /**
     * @param database
     * @author Meghdoot Ojha
     */

    public int createERD(String database){
        try{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/erd/" + database + "_ERD.erd"));
bufferedWriter.write("\t\t\t\t\t\t\t************Reverse Engineering Model************\n");
        StorageManager storageManager = new StorageManager();
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
            for (ColumnMetadataModel metadataModel : tableColumnMetaData) {
                String erd = tableName + "(";
                String primaryKey = "";
                String foreignKey = "";
                String columnName = metadataModel.getName();
                String columnType = metadataModel.getType();
                ForeignKeyConstraintModel foreignKeyModel;
                bufferedWriter.write(columnName + "\n");
                if (columnName != null && columnType != null) {
                    erd += columnName + "(" + columnType + "),";
                }
                if (metadataModel.isPrimaryKey()) {
                    primaryKey = columnName;
                    erd += "Primary key: " + primaryKey + ",";
                }
                if (metadataModel.isForeignKey()) {
                    foreignKey = columnName;
                    foreignKeyModel = metadataModel.getForeignConstraint();
                    erd += "Foreign Key: " + foreignKey + " n--1 " + foreignKeyModel.getTableName() + "(" + foreignKeyModel.getColumnName() + ")";
                }
                if (primaryKey != "") {
                    bufferedWriter.write("PK | " + primaryKey + "\n");
                }
                if (foreignKey != "") {
                    bufferedWriter.write("FK | " + foreignKey + "\n");
                    bufferedWriter.write("**N->1 relationship with below**\n");
                    foreignKeyModel = metadataModel.getForeignConstraint();
                    bufferedWriter.write("Foreign key table: "+foreignKeyModel.getTableName()+"\n");
                    bufferedWriter.write("Foreign key reference: "+foreignKeyModel.getColumnName()+"\n");
                }
            }
        }
        bufferedWriter.close();
        return 0;
    }
        catch (IOException e) {
            e.printStackTrace();
            error("Something went wrong, Please try again");
            return -1;
        }
}}