package ca.dal.database.config;

import ca.dal.database.config.model.InstanceModel;
import ca.dal.database.storage.dictionary.DataDictionaryEntry;

public class ApplicationConfiguration {

    private static InstanceModel instance;

    public static void init(InstanceModel argInstance) {
        instance = argInstance;
    }

    public static InstanceModel getInstance() {
        return instance;
    }

    public static boolean isCurrentInstance(DataDictionaryEntry entry) {
        return instance.getIdentifier().equals(entry.getInstanceName());
    }

    public static String getCurrentInstance(){
        return instance.getIdentifier();
    }
}
