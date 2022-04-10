package ca.dal.database.extractor;

import ca.dal.database.utils.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ca.dal.database.utils.PrintUtils.error;

/**
 * @author Meghdoot Ojha
 */
public class DataExtract {

    public int exportDB(String path) {
        int count = 0;

        Path exportPath = Path.of("sql_dump", "export.sql").toAbsolutePath();

        if(!Files.exists(exportPath)){
            FileUtils.createDirectory("sql_dump");
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Path.of("sql_dump", "export.sql").toAbsolutePath().toString()));
            File file = new File(path);
            if (file.exists()) {
                List<File> list1 = List.of(file.listFiles());
                for (File fileIterate1 : list1) {
                    if (fileIterate1.isDirectory()) {
                        List<File> list2 = List.of(fileIterate1.listFiles());
                        for (File fileIterate2 : list2) {
                            if (fileIterate2.isDirectory()) {
                                List<File> list3 = List.of(fileIterate2.listFiles());
                                for (File fileIterate3 : list3) {
                                    BufferedReader bufferedReader = new BufferedReader(new FileReader(fileIterate3));
                                    if (fileIterate3.getName().contains("meta")) {
                                        String lineRead;
                                        String tableName = "";
                                        String columnName = "";
                                        String primarykey = "";
                                        String foreignkey = "";
                                        String foreignKeyRef = "";
                                        String foreignKeyTable = "";
                                        while ((lineRead = bufferedReader.readLine()) != null) {
                                            if (count == 0) {
                                                String[] split = lineRead.split(",");
                                                tableName = split[1];
                                                System.out.println("tablename: " + tableName);
                                                count++;
                                            } else if (lineRead.startsWith("(pk")) {
                                                String[] split = lineRead.split(",");
                                                primarykey = split[1].replace(")", "");
                                            } else if (lineRead.startsWith("(fk")) {
                                                String[] split = lineRead.split(",");
                                                foreignkey = split[1].replace(")", "");
                                            } else if (lineRead.startsWith("(f_ref")) {
                                                String[] split = lineRead.split(",");
                                                foreignKeyRef = split[1].replace(")", "");
                                            } else if (lineRead.startsWith("(f_table")) {
                                                String[] split = lineRead.split(",");
                                                foreignKeyTable = split[1].replace(")", "");

                                            } else {
                                                String[] keyValue = lineRead.split(",");
                                                columnName = columnName + keyValue[0].replace("(", "") + " " +
                                                        keyValue[1].replace(")", "") + ",";

                                            }
                                        }

                                        String drop = "drop table if exists " + tableName;
                                        bufferedWriter.write(drop);
                                        bufferedWriter.newLine();
                                        String create = "Create table " + tableName + "(" + columnName;

                                        if (primarykey != "") {
                                            create = create + "primary key (" + primarykey + ")";
                                        }

                                        if (foreignkey != "" && foreignKeyRef != "" && foreignKeyTable != "") {

                                            create = create + ", foreign key (" + foreignkey
                                                    + ") references " + foreignKeyTable + "(" + foreignKeyRef + ")";
                                        }
                                        create = create + ");";
                                        bufferedWriter.write(create);
                                        bufferedWriter.newLine();
                                        System.out.println(create);
                                        count = 0;
                                    }
                                    if (fileIterate3.getName().contains("rows")) {
                                        String line = "";
                                        String insert = "INSERT INTO " + fileIterate3.getName().replace(".rows", "") + " VALUES (";
                                        String readData = "";
                                        while ((line = bufferedReader.readLine()) != null) {
                                            if (!line.startsWith("[#")) {
                                                readData = readData + line + ",";
                                                count++;
                                            } else {
                                                int excludeLast = readData.length() - 1;
                                                if (count != 0) {
                                                    readData = readData.substring(0, excludeLast) + "),(";
                                                }
                                            }
                                        }
                                        readData = readData.substring(0, readData.length() - 1) + ");";
                                        insert = insert + readData;
                                        bufferedWriter.write(insert);
                                        bufferedWriter.newLine();
                                        System.out.println("insert " + insert);
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
        } catch (IOException e){
            e.printStackTrace();
            error("Something went wrong, Please try again");
            return -1;
        }
    }
}