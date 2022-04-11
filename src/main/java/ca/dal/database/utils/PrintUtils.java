package ca.dal.database.utils;

import java.util.List;

import static ca.dal.database.utils.StringUtils.repeadAndjoin;
import static ca.dal.database.utils.StringUtils.repeat;

public class PrintUtils {

    public static void print(String message) {
        System.out.print(message);
    }

    public static void println(String message) {
        System.out.println(message);
    }

    /**
     * @param message
     */
    public static void printWithMargin(String message) {
        System.out.println();
        System.out.println(message);
        System.out.println();
    }

    /**
     * @param messages
     */
    public static void printWithMargin(String... messages) {
        System.out.println();
        for (String message : messages) {
            System.out.println(message);
        }
        System.out.println();
    }

    /**
     * @param message
     */
    public static void success(String message) {
        System.out.println();
        System.out.println("\u2714 " + message);
        System.out.println();
    }

    public static void success(String message, Object... values) {
        success(String.format(message, values));
    }

    /**
     * @param message
     */
    public static void error(String message) {
        System.out.println();
        System.out.println("\u2716 " + message);
        System.out.println();
    }

    /**
     * @param message
     * @param values
     */
    public static void error(String message, Object... values) {
        error(String.format(message, values));
    }

    /**
     * @param headers
     * @param matrix
     */
    public static void printMatrix(List<String> headers, List<List<Object>> matrix) {
        println("");
        println(String.format(repeadAndjoin("  %15s  ", headers.size(), "|"), headers.toArray()));
        println(repeat("-", (19 * headers.size()) + headers.size() - 1));
        for (List<Object> row : matrix) {
            println(String.format(repeadAndjoin("  %15s  ", headers.size(), "|"), row.toArray()));
        }
        println("");
    }
}
