package bdisfer1410.gymapp.util.java;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Utility class for String related operations.
 */
public class StringUtils {

    /**
     * Formats a duration given in milliseconds into a time string formatted as "M:SS".
     *
     * <p>Minutes can be one or more digits.</p>
     * <p>Seconds are always shown with two digits, padded with leading zero if needed.</p>
     *
     * @param milliseconds the time duration in milliseconds
     * @return formatted time string in "M:SS" format
     */
    public static String formatMsIntoTime(long milliseconds) {
        long totalSeconds = milliseconds / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    /**
     * Formats a duration given in milliseconds into seconds.
     *
     * @param milliseconds the time duration in milliseconds
     * @return formatted time string seconds
     */
    public static String formatMsIntoSeconds(long milliseconds) {
        double seconds = milliseconds / 1000.0;

        DecimalFormat format = new DecimalFormat("0.0#");
        format.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ENGLISH));

        return format.format(seconds)+"s";
    }

    /**
     * Generates a sanitized file name from a title, date, and extension.
     * Example: "my_routine-2025_06_01_12_30_00.json"
     *
     * @param title The original title.
     * @param date The date and time to include. When null, it is excluded from final name.
     * @param extension The file extension (without dot).
     * @return A safe file name.
     */
    public static String generateFileName(String title, Date date, String extension) {
        // Normalize title: lowercase, replace non-letters with underscore, collapse multiple underscores
        String fileName = Objects.requireNonNullElse(title, "file")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "")
                .replaceAll("_+", "_");

        if (date != null) {
            // Format date
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT).format(date);
            fileName += "-"+timestamp;
        }

        // Add extension
        return fileName+"."+extension;
    }
}
