package bdisfer1410.gymapp.util.android;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Fetches a string via HTTP
 */
public class HttpTools {

    public interface Listener {
        void onSuccess(String response);
        void onError(Exception e);
    }

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public void fetch(String url, Listener listener) {
        executor.execute(() -> {
            HttpURLConnection connection = null;
            try {
                URL urlObj = new URL(url);
                connection = (HttpURLConnection) urlObj.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                String finalResult = getFinalResult(connection);

                mainHandler.post(() -> listener.onSuccess(finalResult));

            }
            catch (Exception e) {
                mainHandler.post(() -> listener.onError(e));
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    @NonNull
    private static String getFinalResult(HttpURLConnection connection) throws IOException {
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Código de error HTTP: " + responseCode);
        }

        InputStream inputStream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder result = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        reader.close();
        return result.toString();
    }
}
