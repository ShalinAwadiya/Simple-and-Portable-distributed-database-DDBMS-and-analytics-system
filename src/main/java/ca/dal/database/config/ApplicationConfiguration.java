package ca.dal.database.config;

import ca.dal.database.config.model.InstanceModel;

public class ApplicationConfiguration {

    private static InstanceModel instance;

    public static void init(InstanceModel argInstance) {
        instance = argInstance;
    }

    public static InstanceModel getInstance() {
        return instance;
    }
}
