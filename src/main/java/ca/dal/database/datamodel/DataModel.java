package ca.dal.database.datamodel;

import java.io.*;
import java.util.List;

/**
 * @author Meghdoot Ojha
 */
public class DataModel {
    /**
     * @param database
     * @author Meghdoot Ojha
     */
    public void createERD(String database) throws IOException {
        int count = 0;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("src/main/resources/erd/"+database+"_ERD.erd"));
        File file = new File(database);
        if (file.exists()) {
            List<File> list1 = List.of(file.listFiles());
            for (File fileIterate1 : list1) {
                if (fileIterate1.isDirectory()) {
                    List<File> list2 = List.of(fileIterate1.listFiles());
                    for (File fileIterate2 : list2) {
                        if (fileIterate2.getName().contains("meta")) {
                            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIterate2));
                            String line;
                            String tableName = "";
                            String columnName = "";
                            String primarykey = "";
                            String foreignkey = "";
                            String foreignKeyRef = "";
                            String foreignKeytable = "";
                            String temp;
                            while ((line = bufferedReader.readLine()) != null) {
                                if (count == 0) {
                                    String[] split = line.split(",");
                                    tableName = split[1];
                                    count++;
                                } else if (line.startsWith("(pk")) {
                                    String[] split = line.split(",");
                                    primarykey = split[1].replace(")", "");
                                } else if (line.startsWith("(fk")) {
                                    String[] split = line.split(",");
                                    foreignkey = split[1].replace(")", "");
                                } else if (line.startsWith("(f_ref")) {
                                    String[] split = line.split(",");
                                    foreignKeyRef = split[1].replace(")", "");
                                } else if (line.startsWith("(f_table")) {
                                    String[] split = line.split(",");
                                    foreignKeytable = split[1].replace(")", "");
                                } else {
                                    columnName = columnName + "\n" + line;

                                }
                            }
                            bufferedWriter.write("+---------------------+" + "\n");
                            bufferedWriter.write(tableName + "\n");
                            bufferedWriter.write("+---------------------+");
                            bufferedWriter.write(columnName + "\n");
                            if (primarykey != "") {
                                bufferedWriter.write("PK | " + primarykey + "\n");
                            }
                            if (foreignkey != "") {
                                bufferedWriter.write("FK | " + foreignkey + "\n");
                                bufferedWriter.write("N->1\n");
                            }
                            if (foreignKeytable != "") {
                                bufferedWriter.write("FK_Table | " + foreignKeytable + "\n");
                            }
                            if (foreignKeyRef != "") {
                                bufferedWriter.write("FK_Table_ref | " + foreignKeyRef + "\n");
                            }
                            bufferedWriter.write("+---------------------+" + "\n");
                            bufferedWriter.append("\n");

                            bufferedReader.close();
                            count = 0;
                        }
                    }
                }
            }
        }
        bufferedWriter.close();
    }
}