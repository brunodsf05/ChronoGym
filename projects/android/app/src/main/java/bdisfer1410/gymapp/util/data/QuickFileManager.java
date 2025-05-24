package bdisfer1410.gymapp.util.data;

import android.content.Context;
import java.io.*;

/**
 * QuickFileManager provides simple methods to save and read text data (e.g., JSON)
 * from either a private file in the app's internal storage or a raw resource.
 * <p>
 * Usage:
 * <ul>
 *   <li>{@code QuickFileManager.with(context).file("data.json")} - to work with internal files.</li>
 *   <li>{@code QuickFileManager.with(context).rawRes(R.raw.my_resource)} - to read from raw resources.</li>
 * </ul>
 * <p>
 * Notes:
 * <ul>
 *   <li>Saving is only supported in file mode. Calling {@code save()} in raw resource mode returns false.</li>
 *   <li>{@code read()} automatically handles the appropriate source (file or raw).</li>
 *   <li>{@code exists()} is only meaningful in file mode.</li>
 * </ul>
 */
public class QuickFileManager {
    private final Context context;
    private String fileName = null;
    private Integer rawResId = null;

    /**
     * Creates a base instance of QuickFileManager with context only.
     *
     * @param context Application context
     * @return A base QuickFileManager instance
     */
    public static QuickFileManager with(Context context) {
        return new QuickFileManager(context);
    }

    /**
     * Private constructor
     */
    private QuickFileManager(Context context) {
        this.context = context;
    }

    /**
     * Sets file mode with given file name.
     *
     * @param fileName Name of the file
     * @return This instance with file mode set
     */
    public QuickFileManager file(String fileName) {
        this.fileName = fileName;
        this.rawResId = null;
        return this;
    }

    /**
     * Sets raw resource mode with the given resource ID.
     *
     * @param rawResId Raw resource ID (e.g., R.raw.my_file)
     * @return This instance with raw mode set
     */
    public QuickFileManager rawRes(int rawResId) {
        this.rawResId = rawResId;
        this.fileName = null;
        return this;
    }

    /**
     * Reads content from the selected source (file or raw resource).
     *
     * @return The content as String, or null if reading failed
     */
    public String read() {
        // Initialize variables
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;

        try {
            // Initialize file stream depending the type of file opened
            InputStream inputStream;

            if (fileName != null) {
                inputStream = context.openFileInput(fileName);
            }
            else if (rawResId != null) {
                inputStream = context.getResources().openRawResource(rawResId);
            }
            else {
                return null;
            }

            // Read in chunks, so is more optimized
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        }
        catch (IOException e) {
            return null;
        }
        finally {
            if (reader != null) {
                try { reader.close(); } catch (IOException ignored) {}
            }
        }
    }

    /**
     * Saves content to file. Will return false if using raw resource mode.
     *
     * @param text The text to save
     * @return true if saved successfully, false otherwise
     */
    public boolean save(String text) {
        if (fileName == null) return false;

        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    /**
     * Checks whether the file exists. Returns false in rawRes mode.
     *
     * @return true if file exists, false otherwise
     */
    public boolean exists() {
        if (fileName == null) return false;

        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }
}