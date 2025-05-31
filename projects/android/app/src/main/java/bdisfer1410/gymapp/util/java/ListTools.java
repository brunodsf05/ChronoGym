package bdisfer1410.gymapp.util.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Utility class for list operations.
 */
public class ListTools {

    /**
     * Safely casts a list of objects of type T to a list of type R.
     *
     * <p>This method includes only elements that are instances of the target class,
     * avoiding {@link ClassCastException} at runtime.</p>
     *
     * @param <T> the source type of the elements in the input list
     * @param <R> the target type to cast elements to
     * @param list the input list with elements of type T
     * @param clazz the Class object of the target type R
     * @return a new list with elements casted to type R, containing only elements assignable to R
     */
    public static <T, R> List<R> cast(List<T> list, Class<R> clazz) {
        List<R> result = new ArrayList<>();

        if (list != null) {
            for (T element : list) {
                if (clazz.isInstance(element)) {
                    result.add(clazz.cast(element));
                }
            }
        }

        return result;
    }

    public static <K, V> K getKeyByValue(Map<K, V> map, V value) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            if ((value == null && entry.getValue() == null) || (value != null && value.equals(entry.getValue()))) {
                return entry.getKey();
            }
        }
        return null;
    }
}
