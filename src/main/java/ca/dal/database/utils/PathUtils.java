package ca.dal.database.utils;

import java.nio.file.Path;

public class PathUtils {

    private PathUtils() {
    }

    public static String absolute(String start, String... more) {
        return Path.of(start, more).toAbsolutePath().toString();
    }

    public static String builder(String start, String... more){
        return absolute(start, more);
    }
}
