package ca.dal.database.storage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;
import static ca.dal.database.utils.StringUtils.valueOf;

public class RowModel {

    private RowMetadataModel metadata;

    private List<Object> values;

    public RowModel(long index, List<Object> values) {
        this.metadata = new RowMetadataModel(index);
        this.values = values;
    }

    public RowModel(long index, String identifier, List<Object> values) {
        this.metadata = new RowMetadataModel(index, identifier);
        this.values = values;
    }

    public RowMetadataModel getMetadata() {
        return metadata;
    }

    public List<Object> getValues() {
        return values;
    }

    public List<String> toList(){
        List<String> list = new ArrayList<>();

        list.add(metadata.toString());
        for(Object value : values){
            list.add(valueOf(value));
        }

        return list;
    }

    @Override
    public String toString() {
        return LINE_FEED + toList().stream().collect(Collectors.joining(LINE_FEED));
    }
}
