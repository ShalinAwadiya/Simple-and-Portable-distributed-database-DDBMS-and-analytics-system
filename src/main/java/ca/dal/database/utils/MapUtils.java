package ca.dal.database.utils;

import java.util.HashMap;
import java.util.Map;

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
     */
    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return map;
    }

    public static <K, V> Map<K, V> of(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return map;
    }
}
