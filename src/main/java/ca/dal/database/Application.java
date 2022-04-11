package ca.dal.database;

import ca.dal.database.config.ApplicationConfiguration;
import ca.dal.database.config.model.InstanceModel;
import ca.dal.database.iam.Authentication;
import ca.dal.database.storage.StorageManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

import static ca.dal.database.utils.PathUtils.absolute;

/**
 * @
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

        Properties properties = new Properties();

        try {
            InputStream configStream = Files.newInputStream(Path.of("config", "config.properties"));
            if (configStream == null) {
                return;
            }
            properties.load(configStream);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ApplicationConfiguration.init(new InstanceModel(
                properties.getProperty("d2_db.instance.identifier"),
                absolute(properties.getProperty("d2_db.instance.private_key")),
                properties.getProperty("d2_db.instance.user"),
                properties.getProperty("d2_db.target.host"),
                Integer.parseInt(properties.getProperty("d2_db.target.port")),
                absolute(properties.getProperty("d2_db.instance.shared_location"))));
    }
}
