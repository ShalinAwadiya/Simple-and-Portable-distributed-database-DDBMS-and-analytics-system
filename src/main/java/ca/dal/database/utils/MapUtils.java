package ca.dal.database.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Harsh Shah
 */
public class MapUtils {

    private MapUtils() {
    }

    /**
     * @param k1
     * @param v1
     * @param k2
     * @param v2
     * @param <K>
     * @param <V>
     * @return
     * @author Harsh Shah
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }
}
