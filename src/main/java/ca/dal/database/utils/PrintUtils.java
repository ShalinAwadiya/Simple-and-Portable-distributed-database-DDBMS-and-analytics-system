package ca.dal.database.utils;

public class PrintUtils {

    public static void print(String message){
        System.out.print(message);
    }

    /**
     * @param message
     */
    public static void println(String message){
        System.out.println("");
        System.out.println(message);
        System.out.println("");
    }

    public static void println(String... messages){
        System.out.println("");
        for(String message: messages) {
            System.out.println(message);
        }
        System.out.println("");
    }

    /**
     * @param message
     */
    public static void success(String message){
        System.out.println("");
        System.out.println("\u2714 "+message);
        System.out.println("");
    }

    /**
     * @param message
     */
    public static void error(String message){
        System.out.println("");
        System.out.println("\u2716 "+message);
        System.out.println("");
    }
}
