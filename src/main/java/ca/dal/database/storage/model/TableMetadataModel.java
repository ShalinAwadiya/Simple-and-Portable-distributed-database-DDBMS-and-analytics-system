package ca.dal.database.storage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;

public class TableMetadataModel {

    private String tableName;

    private Integer noOfColumns;

    private Integer noOfRows;

    private List<ColumnMetadataModel> columnsMetadata;

    public TableMetadataModel(String tableName, List<ColumnMetadataModel> columnsMetadata) {
        this.tableName = tableName;
        this.columnsMetadata = columnsMetadata;
        this.noOfColumns = columnsMetadata.size();
        this.noOfRows = 0;
    }

    public String getTableName() {
        return tableName;
    }

    public Integer getNoOfColumns() {
        return noOfColumns;
    }

    public Integer getNoOfRows() {
        return noOfRows;
    }

    public List<ColumnMetadataModel> getColumnsMetadata() {
        return columnsMetadata;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setColumnsMetadata(List<ColumnMetadataModel> columnsMetadata) {
        this.columnsMetadata = columnsMetadata;
        this.noOfColumns = columnsMetadata.size();
    }

    public List<String> toList(){
        List<String> list = new ArrayList<>();

        String meta = String.format("[META,%s,%d,%d]", tableName, noOfColumns, noOfRows);
        list.add(meta);

        for(ColumnMetadataModel columnMetadata: columnsMetadata){
            list.add(columnMetadata.toString());
        }

        return list;
    }

    @Override
    public String toString() {
        return toList().stream().collect(Collectors.joining(LINE_FEED));
    }
}
