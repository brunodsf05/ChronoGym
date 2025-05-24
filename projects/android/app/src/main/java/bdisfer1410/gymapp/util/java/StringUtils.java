package bdisfer1410.gymapp.util.java;

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
}
