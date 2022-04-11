package ca.dal.database.extractor;

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
import java.util.List;

import static ca.dal.database.utils.PrintUtils.error;

public class DataExtract {

//    public static void main(String[] args){
//        DataExtract de = new DataExtract();
//        de.exportDB("D2_DB");
//    }

    public int exportDB(String database) {
        try {
            StorageManager storageManager = new StorageManager(new Connection("SYSTEM"));
            DatabaseMetadataModel databaseMetadataModel = storageManager.getDatabaseMetadata(database);
            List<TableMetadataHeaderModel> tableNames = databaseMetadataModel.getTableHeaderMetadataModels();

            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/sql_dump/export.sql"));
            for (TableMetadataHeaderModel tableNameModel : tableNames) {

                String tableName = tableNameModel.getTableName();
                String drop = "drop table if exists " + tableName + ";";
                bufferedWriter.write(drop);
                bufferedWriter.write("\n");
                String create = "create table " + tableName + "(";
                TableMetadataModel tableMetadataModel = storageManager.getTableMetadata(database, tableName);
                List<ColumnMetadataModel> tableColumnMetaData = tableMetadataModel.getColumnsMetadata();
                String primaryKeyLocal = "";
                String foreignKeyLocal = "";
                for (ColumnMetadataModel metadataModel : tableColumnMetaData) {
                    String primaryKey = "";
                    String foreignKey = "";
                    String columnName = metadataModel.getName();
                    String columnType = metadataModel.getType();
                    ForeignKeyConstraintModel foreignKeyModel;
                    if (columnName != null && columnType != null) {
                        create += columnName + "(" + columnType + "),";
                    }
                    if (metadataModel.isPrimaryKey()) {
                        primaryKey = columnName;
                        primaryKeyLocal = "primary key(" + primaryKey + "),";
                    }
                    if (metadataModel.isForeignKey()) {
                        foreignKey = columnName;
                        foreignKeyModel = metadataModel.getForeignConstraint();
                        foreignKeyLocal = "foreign key(" + foreignKey + ") references " + foreignKeyModel.getTableName() + "(" + foreignKeyModel.getColumnName() + ")";
                    }

                }
                bufferedWriter.write(create + primaryKeyLocal + foreignKeyLocal + ";");
                bufferedWriter.write("\n");
                List<RowModel> rowModelList = storageManager.fetchAllRowsWithType(database, tableName);
                String insert = "insert into table " + tableName + " values ";

                for (RowModel rowModel : rowModelList) {
                    insert += "(" + rowModel.getValues() + "),";
                }

                String insertWithCharRemove = insert.replace("[", "").replace("]", "");
                bufferedWriter.write(insertWithCharRemove.substring(0, insertWithCharRemove.length() - 1) + ";");
                bufferedWriter.write("\n\n");
            }
            bufferedWriter.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            error("Something went wrong, Please try again");
            return -1;
        }

    }
}