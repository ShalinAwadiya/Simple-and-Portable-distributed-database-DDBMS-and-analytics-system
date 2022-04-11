package ca.dal.database.storage.model.row;

import ca.dal.database.utils.UUIDUtils;

import static ca.dal.database.constant.ApplicationConstants.BLANK_SPACE;

public class RowMetadataModel {

    private Long index;

    private final String identifier;

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

    public void setIndex(Long index) {
        this.index = index;
    }

    public static RowMetadataModel parseHeader(String line) {
        String[] parts = line.substring(1, line.length() - 1).split(BLANK_SPACE);

        if (parts.length != 2) {
            return null;
        }

        return new RowMetadataModel(Long.parseLong(parts[0].substring(1)), parts[1]);
    }

    @Override
    public String toString() {
        return String.format("[#%d %s]", index, identifier);
    }
}
