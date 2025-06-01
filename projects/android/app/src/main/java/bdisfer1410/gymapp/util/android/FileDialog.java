package bdisfer1410.gymapp.util.android;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.util.Result;

public class FileDialog {

    public interface FileCallback<T> {
        void onResult(Result<T, Integer> result);
    }

    public static final int READ_REQUEST_CODE = 100;
    public static final int WRITE_REQUEST_CODE = 101;

    private static FileCallback<String> readCallback;
    private static FileCallback<Void> writeCallback;
    private static String contentToSave;

    /**
     * Start file picker to read file.
     * Must be paired with call to handleActivityResult in your Activity.
     */
    public static void readFile(@NonNull Activity activity, FileCallback<String> callback) {
        readCallback = callback;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, READ_REQUEST_CODE);
    }

    /**
     * Start create file dialog to save content.
     * Must be paired with call to handleActivityResult in your Activity.
     */
    public static void saveFile(@NonNull Activity activity, String filename, String content, FileCallback<Void> callback) {
        writeCallback = callback;
        contentToSave = content;
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, filename);
        activity.startActivityForResult(intent, WRITE_REQUEST_CODE);
    }

    /**
     * Must be called from Activity's onActivityResult.
     * Handles reading or saving the file and calls the appropriate callback.
     */
    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data,
                                            @StringRes int errorReadRes, @StringRes int errorWriteRes, @StringRes int errorFileNotFoundRes) {
        if (resultCode != Activity.RESULT_OK || data == null || data.getData() == null) {
            if (requestCode == READ_REQUEST_CODE && readCallback != null) {
                readCallback.onResult(Result.err(errorReadRes));
                readCallback = null;
            }
            if (requestCode == WRITE_REQUEST_CODE && writeCallback != null) {
                writeCallback.onResult(Result.err(errorWriteRes));
                writeCallback = null;
                contentToSave = null;
            }
            return;
        }
        Uri uri = data.getData();
        if (requestCode == READ_REQUEST_CODE && readCallback != null) {
            readCallback.onResult(readFileFromUri(activity, uri, errorFileNotFoundRes, errorReadRes));
            readCallback = null;
        }
        else if (requestCode == WRITE_REQUEST_CODE && writeCallback != null) {
            writeCallback.onResult(saveFileToUri(activity, contentToSave, uri, errorWriteRes));
            writeCallback = null;
            contentToSave = null;
        }
    }


    /**
     * Must be called from Activity's onActivityResult.
     * Handles reading or saving the file and calls the appropriate callback.
     */
    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        handleActivityResult(
                activity,
                requestCode,
                resultCode,
                data,
                R.string.utils_file_dialog_error_reading_file,
                R.string.utils_file_dialog_error_saving_file,
                R.string.utils_file_dialog_error_file_not_found
        );
    }

    private static Result<String, Integer> readFileFromUri(Activity activity, Uri uri, @StringRes int errorFileNotFoundRes, @StringRes int errorReadRes) {
        try {
            InputStream inputStream = activity.getContentResolver().openInputStream(uri);
            if (inputStream == null) return Result.err(errorFileNotFoundRes);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) sb.append(line).append("\n");
            reader.close();
            return Result.ok(sb.toString());
        }
        catch (IOException e) {
            return Result.err(errorReadRes);
        }
    }

    private static Result<Void, Integer> saveFileToUri(Activity activity, String content, Uri uri, @StringRes int errorWriteRes) {
        try {
            OutputStream outputStream = activity.getContentResolver().openOutputStream(uri);
            if (outputStream == null) return Result.err(errorWriteRes);
            outputStream.write(content.getBytes());
            outputStream.close();
            return Result.ok(null);
        }
        catch (IOException e) {
            return Result.err(errorWriteRes);
        }
    }
}
