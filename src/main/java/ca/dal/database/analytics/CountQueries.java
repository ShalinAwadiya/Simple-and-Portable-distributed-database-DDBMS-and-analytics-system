package ca.dal.database.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CountQueries {

    String delimeter = "<!!>";
    private final List<String> databases = new ArrayList<>();
    private static final String path = "datastore";

    public CountQueries() {
        File file = new File(path);
        String[] directories = file.list();
        for (int i = 0; i < directories.length; i++) {
            if (!(directories[i].equalsIgnoreCase("system") //
                    || directories[i].equalsIgnoreCase("datastore.meta"))) {
                databases.add(directories[i]);
            }
        }
//        System.out.println("Databases:" + databases);
    }

//    public static void main(String[] args) {
//
//        CountQueries countQueries = new CountQueries();
//        System.out.println(countQueries.databases);
//    }

    public void getQueryCount() {
        BufferedReader br = null;
        try {
            File file = new File("DatabaseLogs/QueryLogs.txt");
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            HashMap<String, HashMap<String, Integer>> databaseLogs = new HashMap<>();

            while (line != null) {
//                System.out.println(line);

                String[] logProperties = line.split(delimeter);
                for (int i = 0; i < databases.size(); i++) {
//                    System.out.println("Database: " + databases.get(i));
                    if (logProperties[7].contains(databases.get(i))) {
                        String value = logProperties[7];
                        value = value.substring(1, value.length() - 1);
                        String[] keyValuePairs = value.split(",");
                        HashMap<String, String> map = new HashMap<>();


                        for (String pair : keyValuePairs) {
                            String[] entry = pair.split("=");
                            map.put(entry[0].trim(), entry[1].trim());
                        }
                        String username = map.get("username");
//                        System.out.println("Username:" + username);

                        if (databaseLogs.containsKey(databases.get(i))) {
                            HashMap<String, Integer> temp = databaseLogs.get(databases.get(i));
                            if (temp.containsKey(username)) {
                                int counter = temp.get(username);
                                counter = counter + 1;
                                temp.put(username, counter);
                            } else {
                                temp.put(username, 1);
                            }
                            databaseLogs.put(databases.get(i), temp);
                        } else {
                            HashMap<String, Integer> temp = new HashMap<>();
                            temp.put(username, 1);

                            databaseLogs.put(databases.get(i), temp);
                        }
                    }
                }

                line = br.readLine();
            }
            System.out.println(databaseLogs);
            for (Map.Entry<String, HashMap<String, Integer>> entry1 : databaseLogs.entrySet()) {
                String key1 = entry1.getKey();
                for (Map.Entry<String, Integer> entry2 : entry1.getValue().entrySet()) {
                    String key2 = entry2.getKey();
                    int value2 = entry2.getValue();
                    System.out.println("User " + key2 + " submitted " + value2 + " query for " + key1);
                }
            }
            System.out.println("Query analysis performed successfully");
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
