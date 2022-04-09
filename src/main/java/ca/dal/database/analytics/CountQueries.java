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
            HashMap<String,HashMap<String,Integer>> databaseLogs = new HashMap<>();

            while (line != null) {
                System.out.println(line);

                String[] logProperties = line.split(delimeter);
                for(int i=0;i<databases.size();i++)
                {
                    System.out.println("Database: "+databases.get(i));
                    if(logProperties[7].contains(databases.get(i)))
                    {
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
                        String username=map.get("username");
                        System.out.println("Username:"+username);

                        if(databaseLogs.containsKey(databases.get(i)))
                        {
                            HashMap<String,Integer> temp = databaseLogs.get(databases.get(i));
                            if(temp.containsKey(username))
                            {
                                int counter=temp.get(username);
                                counter=counter+1;
                                temp.put(username,counter);
                            }
                            else
                            {
                                temp.put(username,1);
                            }
                            databaseLogs.put(databases.get(i),temp);
                        }
                        else
                        {
                            HashMap<String,Integer> temp = new HashMap<>();
                            temp.put(username,1);

                            databaseLogs.put(databases.get(i),temp);
                        }
                    }
                }

                //
                //for (int i = 0; i < logProperties.length; i++)
                //{
                    //System.out.println(logProperties[i]);
                //}
                 //System.out.println(logProperties[7]);

                line = br.readLine();
            }
            System.out.println(databaseLogs);
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
