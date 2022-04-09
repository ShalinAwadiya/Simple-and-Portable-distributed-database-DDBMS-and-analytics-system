package ca.dal.database.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ListUtils {

    /**
     * @param projection
     * @param values
     * @return
     */
    public static List<Object> project(List<Integer> projection, List<Object> values){
        List<Object> result = new ArrayList<>();
        for(Integer project: projection){
            result.add(values.get(project));
        }

        return result;
    }
}
