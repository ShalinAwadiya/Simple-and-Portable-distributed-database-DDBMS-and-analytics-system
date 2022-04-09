package ca.dal.database.analytics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class CountQueries {

    String delimeter="<!!>";
    private List<String> databases = new ArrayList<>();
    public static void main(String []args)
    {

        CountQueries countQueries= new CountQueries();
        countQueries.databases.add("db1");
        countQueries.databases.add("db2");
        countQueries.databases.add("db3");
        System.out.println(countQueries.databases);
        countQueries.getQueryCount();
    }
    public void getQueryCount()
    {
        BufferedReader br=null;
        try {
            File file = new File("DatabaseLogs/QueryLogs.txt");
            FileReader fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = br.readLine();
            HashMap<String,List<String>> databaseLogs = new HashMap<>();

            while (line != null) {
                System.out.println(line);

                String[] logProperties = line.split(delimeter);
                for(int i=0;i<databases.size();i++)
                {
                    System.out.println("Database: "+databases.get(i));
                    if(logProperties[7].contains(databases.get(i)))
                    {
                        int indexOfUsername=logProperties[7].indexOf("username");
                        System.out.println("Index of Username:"+indexOfUsername);

                        String value=logProperties[7];
                        value = value.substring(1, value.length()-1);
                        String[] keyValuePairs = value.split(",");
                        HashMap<String,String> map = new HashMap<>();


                        for(String pair : keyValuePairs)
                        {
                            String[] entry = pair.split("=");
                            map.put(entry[0].trim(), entry[1].trim());
                        }
                        //System.out.println(map);
                        System.out.println("Username:"+map.get("username"));

                        if(databaseLogs.containsKey(databases.get(i)))
                        {
                            List<String> temp = databaseLogs.get(databases.get(i));
                            temp.add(logProperties[7]);
                            databaseLogs.put(databases.get(i),temp);
                        }
                        else
                        {
                            List<String> temp= new ArrayList<>();
                            temp.add(logProperties[7]);
                            databaseLogs.put(databases.get(i),temp);
                        }
                    }
                    System.out.println(databaseLogs);
                }

                //
                //for (int i = 0; i < logProperties.length; i++)
                //{
                    //System.out.println(logProperties[i]);
                //}
                 //System.out.println(logProperties[7]);

                line = br.readLine();
            }
            System.out.println("File Read Successfully");
        } catch (IOException e) {
            System.out.println("Exception:"+e.getMessage());
        }finally
        {
            try{
                if(br!=null)
                    br.close();
            }catch(Exception ex){
                System.out.println("Error in closing the BufferedReader"+ex);
            }
        }
    }
}
