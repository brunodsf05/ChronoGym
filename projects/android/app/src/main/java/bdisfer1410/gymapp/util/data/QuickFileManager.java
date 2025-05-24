package bdisfer1410.gymapp.util.data;

import android.content.Context;
import java.io.*;

/**
 * QuickFileManager provides simple methods to save and read text data (e.g., JSON)
 * to and from a private file in the app's internal storage.
 */
public class QuickFileManager {
    private final String fileName;
    private final Context context;

    /**
     * Static factory method to create a new instance.
     *
     * @param context  Application context
     * @param fileName Name of the file
     * @return New QuickFileManager instance
     */
    public static QuickFileManager with(Context context, String fileName) {
        return new QuickFileManager(context, fileName);
    }

    /**
     * Private constructor
     */
    private QuickFileManager(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    /**
     * Saves the given text to internal storage.
     *
     * @param text The text to save
     * @return true if saved successfully, false otherwise
     */
    public boolean save(String text) {
        try (FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE)) {
            fos.write(text.getBytes());
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Reads the content of the file.
     *
     * @return The file content as String, or null if reading failed
     */
    public String read() {
        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(fileName);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Checks whether the file exists.
     *
     * @return true if the file exists, false otherwise
     */
    public boolean exists() {
        File file = new File(context.getFilesDir(), fileName);
        return file.exists();
    }
}
