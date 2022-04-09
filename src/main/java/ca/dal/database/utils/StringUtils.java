package ca.dal.database.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class StringUtils {

    private static final Logger logger = Logger.getLogger(StringUtils.class.getName());

    private StringUtils(){}

    /**
     * @param str
     * @return
     *
     * @author Harsh Shah
     */
    public static boolean isNotEmpty(String str){
        if(str == null){
            return false;
        }

        str = str.trim();

        return str.length() != 0;
    }

    /**
     * @param str
     * @return
     *
     * @author Harsh Shah
     */
    public static boolean isEmpty(String str){
        return !isNotEmpty(str);
    }

    /**
     * @param strs
     * @return
     *
     * @author Harsh Shah
     */
    public static boolean isAnyEmpty(String... strs) {
        for(String str: strs){
            if(isEmpty(str)){
                return true;
            }
        }

        return false;
    }

    /**
     * @param strings
     * @return
     *
     * @author Harsh Shah
     */
    public static boolean isAllEmpty(String... strings) {
        for(String str: strings){
            if(isNotEmpty(str)){
                return false;
            }
        }

        return true;
    }

    /**
     * @param values
     * @return
     */
    public static String builder(String... values){

        StringBuilder builder = new StringBuilder();

        for(String value: values){
            builder.append(value);
        }

        return builder.toString();
    }

    /**
     * @param value
     * @return
     *
     * @author Harsh Shah
     */
    public static String valueOf(Object value){
        return String.valueOf(value);
    }

    /**
     * @param string
     * @return
     */
    public static String getHash(String string){
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return bytesToHex(digest.digest(string.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * @param hash
     * @return
     */
    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }


    /**
     * @param str
     * @param count
     * @return
     */
    public static String repeat(String str, int count){
        return str.repeat(count);
    }

    public static String repeadAndjoin(String str, int count, String with){

        List<String> strs = new ArrayList<>();

        for(int i = 0; i < count; i++){
            strs.add(str);
        }

        return strs.stream().collect(Collectors.joining(with));

    }


}
