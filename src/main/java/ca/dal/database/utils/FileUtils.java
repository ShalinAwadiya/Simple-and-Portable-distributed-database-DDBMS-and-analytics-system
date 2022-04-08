package ca.dal.database.utils;

import ca.dal.database.transaction.TransactionManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;

/**
 * @author Harsh Shah
 */
public class FileUtils {

    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());


    public boolean isExists(String start, String... tails){
        Path path = Path.of(start, tails);

        if(path == null){
            return false;
        }

        return Files.exists(path);
    }

    /**
     * @param start
     * @param tails
     * @return
     * @author Harsh Shah
     */
    public static int createDirectory(String start, String... tails){

        Path path = Path.of(start, tails);

        if(path == null){
            return -1;
        }

        if(Files.notExists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                logger.log(Level.INFO, e.getMessage());
                return -1;
            }
        }
        return 0;
    }

    /**
     * @param start
     * @param tails
     * @return
     * @author Harsh Shah
     */
    public static int createFile(String start, String... tails){

        Path path = Path.of(start, tails);

        if(path == null){
            return -1;
        }

        if(Files.notExists(path)){
            try {
                Files.createFile(path);
            } catch (IOException e) {
                logger.log(Level.INFO, e.getMessage());
                return -1;
            }
        }
        return 0;
    }


    /**
     * @param startLocation
     * @param location
     * @return
     *
     * @author Harsh Shah
     */
    public static List<String> read(String startLocation, String... location) {
        try {

            Path path = Path.of(startLocation, location);

            if (!Files.exists(path)) {
                return null;
            }

            return Files.readAllLines(path);
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }


    /**
     * @param line
     * @param startLocation
     * @param location
     * @return
     */
    public static int write(String line, String startLocation, String... location){
        return write(Arrays.asList(line), StandardOpenOption.TRUNCATE_EXISTING, startLocation, location);
    }

    public static int write(List<String> lines, String startDirectory, String... location){
        return write(lines, StandardOpenOption.TRUNCATE_EXISTING, startDirectory, location);
    }

    public static int appendLn(List<String> lines, String startDirectory, String... location){
        lines.add(0, LINE_FEED);
        return write(lines, StandardOpenOption.APPEND, startDirectory, location);
    }

    /**
     * @param line
     * @param startLocation
     * @param location
     * @return
     */
    public static int append(String line, String startLocation, String... location){
        return write(Arrays.asList(line), StandardOpenOption.APPEND, startLocation, location);
    }

    public static int appendLn(String line, String startLocation, String... location){
        return write(Arrays.asList(LINE_FEED, line), StandardOpenOption.APPEND, startLocation, location);
    }


    /**
     * @param lines
     * @param option
     * @param startDirectory
     * @param location
     * @return
     */
    public static int write(List<String> lines, StandardOpenOption option,
                            String startDirectory, String... location) {

        Path path = Path.of(startDirectory, location);

        if(path == null){
            return -1;
        }

        path = path.toAbsolutePath();

        if(Files.notExists(path)){
            return -1;
        }

        try {
            Files.write(path, lines, option, StandardOpenOption.CREATE);
        } catch (IOException e) {
            return -1;
        }

        return 0;
    }
}

