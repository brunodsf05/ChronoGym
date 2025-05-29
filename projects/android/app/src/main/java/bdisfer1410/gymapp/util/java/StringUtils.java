package bdisfer1410.gymapp.util.java;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

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
}
