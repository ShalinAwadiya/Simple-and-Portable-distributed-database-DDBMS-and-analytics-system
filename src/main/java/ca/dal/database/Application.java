package ca.dal.database;

import ca.dal.database.datamodel.DataModel;
import ca.dal.database.extractor.DataExtract;
import ca.dal.database.iam.Authentication;
import ca.dal.database.storage.StorageManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;

/**
 * @author Harsh Shah
 */
public class Application {

    static {
        setup();
    }

    private static Authentication authentication = new Authentication();

    public static void main(String[] args) throws IOException {
        /*StorageManager.init();
        authentication.init();*/
       // DataExtract extract=new DataExtract();
        //extract.exportDB("databases");
        DataModel model=new DataModel();
        model.createERD("nishit");
    }

    public static void setup() {
        try {
            InputStream loggerProps = ClassLoader.getSystemResourceAsStream("logger.properties");
            LogManager.getLogManager().readConfiguration(loggerProps);
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
        }
    }
}
