package bdisfer1410.gymapp.activity.explorer;

import android.content.Context;
import android.util.Log;

import java.util.List;
import java.util.stream.Collectors;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerde;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.android.HttpTools;

public class ExplorerApi {

    public static final int ERROR_HTTP = R.string.utils_explorer_api_error;

    private static final String BASE_URL = "https://bdisfer1410.eu.pythonanywhere.com";
    private static final String TEMPLATE_URL = BASE_URL + "?exclusive=%s&inclusive=%s";

    public interface Listener {
        void onResponse(Result<List<Exercise>, Integer> result);
    }

    private static String buildUrl(List<String> exclusive, List<String> inclusive) {
        String exclusiveParam = formatListToParam(exclusive);
        String inclusiveParam = formatListToParam(inclusive);
        return String.format(TEMPLATE_URL, exclusiveParam, inclusiveParam);
    }

    private static String formatListToParam(List<String> list) {
        if (list == null || list.isEmpty()) return "";
        return list.stream()
                .map(tag -> "@" + tag)
                .collect(Collectors.joining(","));
    }

    public static void fetch(Context context, List<String> exclusive, List<String> inclusive, Listener listener) {
        String url = buildUrl(exclusive, inclusive);
        HttpTools httpTools = new HttpTools();

        Log.d("ExplorerApi", "Calling \""+url+"\"");
        httpTools.fetch(url, new HttpTools.Listener() {
            @Override
            public void onSuccess(String response) {
                ExerciseSerde serde = new ExerciseSerdeJSON(context, response);
                Result<List<Exercise>, Integer> result = serde.deserialize();
                listener.onResponse(result);
            }

            @Override
            public void onError(Exception e) {
                Log.e("ExplorerApi", e.toString());
                listener.onResponse(Result.err(ERROR_HTTP));
            }
        });
    }
}
