package ca.dal.database.utils;

import ca.dal.database.transaction.TransactionManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static ca.dal.database.constant.ApplicationConstants.LINE_FEED;
import static ca.dal.database.utils.PrintUtils.print;

public class FileUtils {

    private static final Logger logger = Logger.getLogger(TransactionManager.class.getName());

    /**
     * @param start
     * @param tails
     * @return
     */
    public static int createDirectory(String start, String... tails) {

        Path path = Path.of(start, tails);

        if (path == null) {
            return -1;
        }

        if (Files.notExists(path)) {
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
     */
    public static int createFile(String start, String... tails) {

        Path path = Path.of(start, tails);

        if (path == null) {
            return -1;
        }

        if (Files.notExists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * @param startLocation
     * @param location
     * @return
     */
    public static List<String> read(String startLocation, String... location) {
        try {

            Path path = Path.of(startLocation, location);
            path = path.toAbsolutePath();

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
    public static int write(String line, String startLocation, String... location) {
        return write(Arrays.asList(line), StandardOpenOption.TRUNCATE_EXISTING, startLocation, location);
    }

    public static int write(List<String> lines, String startDirectory, String... location) {
        return write(lines, StandardOpenOption.TRUNCATE_EXISTING, startDirectory, location);
    }

    public static int appendLn(List<String> lines, String startDirectory, String... location) {
        lines.add(0, LINE_FEED);
        return write(lines, StandardOpenOption.APPEND, startDirectory, location);
    }

    /**
     * @param line
     * @param startLocation
     * @param location
     * @return
     */
    public static int append(String line, String startLocation, String... location) {
        return write(Arrays.asList(line), StandardOpenOption.APPEND, startLocation, location);
    }

    public static int appendLn(String line, String startLocation, String... location) {
        return write(Arrays.asList(LINE_FEED, line), StandardOpenOption.APPEND, startLocation, location);
    }

    /**
     * @param lines
     * @param option
     * @param startDirectory
     * @param location
     * @return
     */
    public static int write(List<String> lines, StandardOpenOption option, String startDirectory, String... location) {

        Path path = Path.of(startDirectory, location);

        if (path == null) {
            return -1;
        }

        path = path.toAbsolutePath();

        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            Files.write(path, lines, option, StandardOpenOption.CREATE);
        } catch (IOException e) {
            return -1;
        }

        return 0;
    }

    /**
     * @param index
     * @param line
     * @param start
     * @param tails
     * @return
     */
    public static int writeAt(int index, String line, String start, String... tails) {

        String path = buildAndValidatePathString(start, tails);

        if (null == path) {
            return -1;
        }

        try {
            writeAt(path, index, line);
        } catch (IOException e) {
            return -1;
        }

        return 0;
    }

    public static void writeAt(String path, int index, String line) throws IOException {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile("path", "w");
        } catch (FileNotFoundException e) {
            logger.log(Level.INFO, e.getMessage());
        }

        List<Long> pointers = new ArrayList<>();

        for (int i = 0; i < index && raf.readLine() != null; i++) {
            pointers.add(raf.getFilePointer());
        }

        raf.seek(pointers.get(index));
        raf.writeChars(line);
        raf.close();
    }

    private static String buildAndValidatePathString(String start, String... tails) {
        Path path = Path.of(start, tails);

        if (path == null) {
            return null;
        }

        path = path.toAbsolutePath();

        if (Files.notExists(path)) {
            return null;
        }

        return path.toString();
    }

    private static Path buildAndValidatePath(String start, String... tails) {
        Path path = Path.of(start, tails);

        if (path == null) {
            return null;
        }

        path = path.toAbsolutePath();

        if (Files.notExists(path)) {
            return null;
        }

        return path;
    }

    public static List<String> read(InputStream inputStream) {

        List<String> lines = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lines;
    }

    public static void createTempFile(String directory, String fileName, List<String> lines) {
        createDirectory(directory, "temp");
        createFile(directory, "temp", fileName);
        write(lines, directory, "temp", fileName);
    }

    public static void removeTempFile(String directory, String tempTableId) {
        try {
            Files.deleteIfExists(Path.of(directory, "temp", tempTableId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isExists(String start, String... tails) {
        Path path = Path.of(start, tails);

        if (path == null) {
            return false;
        }

        return Files.exists(path);
    }
}
