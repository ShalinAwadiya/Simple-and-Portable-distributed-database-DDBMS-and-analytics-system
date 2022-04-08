package ca.dal.database.utils;

public class PrintUtils {

    public static void println(String message){
        System.out.println("");
        System.out.println(message);
        System.out.println("");
    }


    public static void success(String message){
        System.out.println("");
        System.out.println("\u2713 "+message);
        System.out.println("");
    }
}
