package ca.dal.database.logger;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.UUID;

public class EventLog {

    private static int logId = 0;

    //    public static void main (String []args)
//    {
//        EventLog eventLog= new EventLog();
//
//        HashMap<String,String> data=new HashMap<>();
//        data.put("one","two");
//        eventLog.writeLog("Warning Log","Event has started",data);
//        eventLog.readLog();
//    }
    public void readLog() {
        BufferedReader br = null;
        try {
            File file = new File("DatabaseLogs/EventLogs.txt");
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            while (line != null) {
                System.out.println(line);

                String[] logProperties = line.split("\t");
                for (int i = 0; i < logProperties.length; i++) {
                    System.out.println(logProperties[i]);
                }

                line = br.readLine();
            }
            System.out.println("File Read Successfully");
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

    public void writeLog(String type, String message, HashMap<String, String> data) {
        BufferedWriter bw = null;
        try {
            File directory = new File("DatabaseLogs");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File("DatabaseLogs/EventLogs.txt");
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            FileWriter fw = new FileWriter(file);
            bw = new BufferedWriter(fw);

            logId = logId + 1;
            bw.write(logId + "\t");


            String uuid = UUID.randomUUID().toString();
            bw.write(uuid + "\t");


            String date = String.valueOf(java.time.LocalDate.now());
            bw.write(date + "\t");


            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = dtf.format(java.time.LocalTime.now());
            bw.write(time + "\t");


            bw.write(type + "\t");
            bw.write(message + "\t");
            bw.write(String.valueOf(data));

            System.out.println("File written Successfully");

        } catch (IOException e) {
            System.out.println("Exception:" + e.getMessage());
        } finally {
            try {
                if (bw != null) bw.close();
            } catch (Exception ex) {
                System.out.println("Error in closing the BufferedWriter" + ex);
            }
        }
    }
}
