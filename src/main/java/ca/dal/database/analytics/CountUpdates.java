package ca.dal.database.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountUpdates {

    String delimeter = "<!!>";
    private final List<String> tables = new ArrayList<>();

//    public static void main(String[] args) {
//
//        CountUpdates countUpdates = new CountUpdates();
//        countUpdates.tables.add("table1");
//        countUpdates.tables.add("table2");
//        countUpdates.tables.add("table3");
//        countUpdates.countUpdates("database1");
//    }

    public void countUpdates(String databaseName) {
        BufferedReader br = null;

        String path = "datastore/" + databaseName;
        File tableFiles = new File(path);
        String[] directories = tableFiles.list();
        for (int i = 0; i < directories.length; i++) {
            if (!(directories[i].equalsIgnoreCase(databaseName + ".meta"))) {
                tables.add(directories[i]);
            }
        }
//        System.out.println("Tables:" + tables);
        try {
            File file = new File("DatabaseLogs/QueryLogs.txt");
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            HashMap<String, Integer> tableUpdates = new HashMap<>();

            while (line != null) {

                String[] logProperties = line.split(delimeter);
                for (int i = 0; i < tables.size(); i++) {
                    if (logProperties[5].toLowerCase().contains("update") && logProperties[7].contains(tables.get(i))) {
                        if (tableUpdates.containsKey(tables.get(i))) {
                            int counter = tableUpdates.get(tables.get(i));
                            counter = counter + 1;
                            tableUpdates.put(tables.get(i), counter);
                        } else {
                            tableUpdates.put(tables.get(i), 1);
                        }
                    }
                }
                line = br.readLine();
            }
//            System.out.println(tableUpdates);
            for (Map.Entry<String, Integer> entry : tableUpdates.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println("Total: " + value + " Update operation are performed on " + key);
            }
            System.out.println("Update analysis performed successfully");
        } catch (IOException e) {
            System.out.println("Exception:" + e.getMessage());
        } finally {
            try {
                if (br != null) br.close();
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedReader" + ex);
            }
        }
    }
}



