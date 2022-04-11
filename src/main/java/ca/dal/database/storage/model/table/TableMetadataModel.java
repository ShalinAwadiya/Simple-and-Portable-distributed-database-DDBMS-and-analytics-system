package ca.dal.database.storage.model.table;

import ca.dal.database.storage.model.column.ColumnMetadataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;
import static java.lang.Long.parseLong;

public class TableMetadataModel extends TableMetadataHeaderModel {

    private List<ColumnMetadataModel> columnsMetadata;

    private final Long noOfRows;

    public TableMetadataModel(String tableName, List<ColumnMetadataModel> columnsMetadata) {
        super(tableName, columnsMetadata.size());
        this.columnsMetadata = columnsMetadata;
        this.noOfRows = 0L;
    }

    public TableMetadataModel(String tableName, Long noOfRows, List<ColumnMetadataModel> columnsMetadata) {
        super(tableName, columnsMetadata.size());
        this.columnsMetadata = columnsMetadata;
        this.noOfRows = noOfRows;
    }

    public TableMetadataModel(TableMetadataModel metadataModel, Long noOfRows) {
        super(metadataModel.getTableName(), metadataModel.getColumnsMetadata().size());
        this.columnsMetadata = metadataModel.getColumnsMetadata();
        this.noOfRows = noOfRows;
    }

    public List<ColumnMetadataModel> getColumnsMetadata() {
        return columnsMetadata;
    }

    public void setColumnsMetadata(List<ColumnMetadataModel> columnsMetadata) {
        this.columnsMetadata = columnsMetadata;
        this.setNoOfColumns(columnsMetadata.size());
    }

    public Long getNoOfRows() {
        return noOfRows;
    }

    public String toMetaString() {
        return String.format("[HEADER,%s,%d,%d]", getTableName(), getNoOfColumns(), this.noOfRows);
    }

    public List<String> toStringList() {
        List<String> list = new ArrayList<>();

        list.add(toMetaString());

        for (ColumnMetadataModel columnMetadata : columnsMetadata) {
            list.add(columnMetadata.toString());
        }

        return list;
    }

    public String toHeaderString() {
        return super.toString();
    }

    @Override
    public String toString() {
        return toStringList().stream().collect(Collectors.joining(LINE_FEED));
    }

    public static TableMetadataModel parse(List<String> lines) {

        String header = lines.get(0);
        String[] parts = header.substring(1, header.length() - 1).split(",");
        String tableName = parts[1];
        long noOfRows = parseLong(parts[3]);

        List<ColumnMetadataModel> columnMetadataModels = new ArrayList<>();

        for (int index = 1; index < lines.size(); index++) {
            ColumnMetadataModel columnMetadataModel = ColumnMetadataModel.parse(lines.get(index));
            columnMetadataModels.add(columnMetadataModel);
        }

        return new TableMetadataModel(tableName, noOfRows, columnMetadataModels);
    }

}
