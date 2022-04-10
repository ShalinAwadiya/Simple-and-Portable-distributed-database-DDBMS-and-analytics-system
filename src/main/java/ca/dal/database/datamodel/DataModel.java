package ca.dal.database.datamodel;

import ca.dal.database.storage.StorageManager;
import ca.dal.database.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ca.dal.database.utils.PrintUtils.error;
import static ca.dal.database.utils.StringUtils.builder;

/**
 * @author Meghdoot Ojha
 */
public class DataModel {
    /**
     * @param database
     * @author Meghdoot Ojha
     * @return
     */
    public int createERD(String database) {
        int count = 0;

        Path exportPath = Path.of("datamodel", database, builder(database, ".erd")).toAbsolutePath();

        if(!Files.exists(exportPath)){
            FileUtils.createDirectory("datamodel", database);
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Path.of("datamodel",
                    database, builder(database, ".erd")).toAbsolutePath().toString()));

            File file = new File(StorageManager.ROOT);

            if (file.exists()) {
                System.out.println("dire" + file);
                List<File> listLevel1 = List.of(file.listFiles());
                for (File fileIterate1 : listLevel1) {
                    System.out.println("fileitr: " + fileIterate1);
                    if (fileIterate1.isDirectory() && database.equals(fileIterate1.getName())) {
                        System.out.println("file: " + fileIterate1);
                        List<File> listLevel2 = List.of(fileIterate1.listFiles());
                        for (File fileIterate2 : listLevel2) {
                            if (fileIterate2.isDirectory()) {
                                List<File> listLevel3 = List.of(fileIterate2.listFiles());
                                for (File fileIterate3 : listLevel3) {
                                    if (fileIterate3.getName().contains("meta")) {
                                        BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIterate3));
                                        String line;
                                        String tableName = "";
                                        String columnName = "";
                                        String primaryKey = "";
                                        String foreignKey = "";
                                        String foreignKeyRef = "";
                                        String foreignKeytable = "";
                                        while ((line = bufferedReader.readLine()) != null) {
                                            if (count == 0) {
                                                String[] split = line.split(",");
                                                tableName = split[1];
                                                count++;
                                            } else if (line.startsWith("(pk")) {
                                                String[] split = line.split(",");
                                                primaryKey = split[1].replace(")", "");
                                            } else if (line.startsWith("(fk")) {
                                                String[] split = line.split(",");
                                                foreignKey = split[1].replace(")", "");
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
                                        if (primaryKey != "") {
                                            bufferedWriter.write("PK | " + primaryKey + "\n");
                                        }
                                        if (foreignKey != "") {
                                            bufferedWriter.write("FK | " + foreignKey + "\n");
                                            bufferedWriter.write("**N->1 relationship with below**\n");
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
                }
            }
            bufferedWriter.close();
            return 0;
        } catch (Exception e){
            e.printStackTrace();
            error("Something went wrong, Please try again");
            return -1;
        }
    }
}