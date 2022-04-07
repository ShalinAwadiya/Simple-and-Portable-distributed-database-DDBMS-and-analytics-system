package ca.dal.database.utils;

public class StringUtils {

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
     * @param value
     * @return
     *
     * @author Harsh Shah
     */
    public static String valueOf(Object value){
        return String.valueOf(value);
    }
}
