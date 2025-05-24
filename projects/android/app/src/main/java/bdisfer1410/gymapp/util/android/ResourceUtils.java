package bdisfer1410.gymapp.util.android;

import android.annotation.SuppressLint;
import android.content.Context;

public class ResourceUtils {

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
}
