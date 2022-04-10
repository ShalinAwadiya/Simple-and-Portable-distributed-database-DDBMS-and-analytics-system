package ca.dal.database;

import ca.dal.database.config.ApplicationConfiguration;
import ca.dal.database.config.model.InstanceModel;
import ca.dal.database.iam.Authentication;
import ca.dal.database.storage.StorageManager;

import java.nio.file.Path;

import static ca.dal.database.utils.PathUtils.absolute;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }

    private static Authentication authentication = new Authentication();

    public static void main(String[] args) {
        StorageManager.init();
        authentication.init();
    }

    public static void setup() {
//        ApplicationConfiguration.init(new InstanceModel("InstanceOne",
//                absolute("config","db-instance-private-key"),
//                "harshshah1295", "34.130.217.34",
//                22, absolute("datastore", "shared")));
    }
}
