package bdisfer1410.gymapp.util.android;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Objects;

import bdisfer1410.gymapp.R;

public class ResourceUtils {

    private static String preffix;

    /**
     * Retrieves the resource ID of a string by its name (key).
     *
     * @param context The Context used to access resources.
     * @param key The name of the string resource.
     * @return The resource ID if found, or 0 if not found.
     */
    @SuppressLint("DiscouragedApi")
    public static int getStringResId(Context context, String key) {
        return context.getResources().getIdentifier(key, "string", context.getPackageName());
    }

    /**
     * Retrieves the string value associated with the given key.
     *
     * @param context The Context used to access resources.
     * @param key The name of the string resource.
     * @return The string value if found, or null if not found.
     */
    public static String fromKey(Context context, String key) {
        if (context == null || key == null) return null;
        int resId = getStringResId(context, key);
        return resId != 0 ? context.getString(resId) : null;
    }

    /**
     * Retrieves the string value associated with the given key, or returns a default value if not found.
     *
     * @param context The Context used to access resources.
     * @param key The name of the string resource.
     * @param defaultValue The default string to return if the resource is not found.
     * @return The string value if found, or the defaultValue if not found.
     */
    public static String fromKeyOrDefault(Context context, String key, String defaultValue) {
        if (context == null || key == null) return defaultValue;
        int resId = getStringResId(context, key);
        return resId != 0 ? context.getString(resId) : defaultValue;
    }

    /**
     * Searches for the string resource key whose value matches the given query,
     * filtering only keys that start with a specified prefix.
     *
     * @param context The Context used to access resources.
     * @param query The exact string value to search for.
     * @param prefix The prefix that the resource key must start with.
     * @return The resource key name if found, or null if not found.
     */
    public static String findKey(Context context, String query, String prefix) {
        if (context == null || query == null || prefix == null) return null;

        Field[] fields = R.string.class.getDeclaredFields();

        return Arrays.stream(fields)
                .filter(f -> {
                    return f.getName().startsWith(prefix);
                })
                .map(f -> {
                    try {
                        int resId = f.getInt(null);
                        String value = context.getString(resId);
                        if (query.equals(value)) {
                            return f.getName();
                        }
                    }
                    catch (Exception e) { /* Ignore and continue */ }

                    return null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

}
