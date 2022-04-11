package ca.dal.database.storage.model.row;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;
import static ca.dal.database.utils.StringUtils.valueOf;

public class RowModel {

    private final RowMetadataModel metadata;

    private List<Object> values;

    public RowModel(List<Object> values) {
        this.metadata = new RowMetadataModel(-1L);
        this.values = values;
    }

    public RowModel(long index, List<Object> values) {
        this.metadata = new RowMetadataModel(index);
        this.values = values;
    }

    public RowModel(RowModel row, long index) {
        this.metadata = new RowMetadataModel(index, row.getMetadata().getIdentifier());
        this.values = row.getValues();
    }

    public RowModel(RowMetadataModel rowMetadataModel, List<Object> values) {
        this.metadata = rowMetadataModel;
        this.values = values;
    }

    public RowMetadataModel getMetadata() {
        return metadata;
    }

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    public static RowModel parse(List<String> lines) {
        if (lines == null || lines.isEmpty()) {
            return null;
        }

        List<Object> values = new ArrayList<>();
        for (int i = 1; i < lines.size(); i++) {
            values.add(lines.get(i));
        }

        return new RowModel(RowMetadataModel.parseHeader(lines.get(0)), values);
    }

    public List<String> toList() {
        List<String> list = new ArrayList<>();

        list.add(metadata.toString());
        for (Object value : values) {
            list.add(valueOf(value));
        }

        return list;
    }

    @Override
    public String toString() {
        return toList().stream().collect(Collectors.joining(LINE_FEED));
    }
}
