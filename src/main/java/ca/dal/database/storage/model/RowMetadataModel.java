package ca.dal.database.storage.model;

import ca.dal.database.utils.UUIDUtils;

public class RowMetadataModel {

    private Long index;

    private String identifier;

    public RowMetadataModel(Long index) {
        this.index = index;
        this.identifier = UUIDUtils.generate();
    }

    public RowMetadataModel(Long index, String identifier) {
        this.index = index;
        this.identifier = identifier;
    }

    public Long getIndex() {
        return index;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String toString() {
        return String.format("[#%d %s]", index, identifier);
    }
}
