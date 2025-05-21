package bdisfer1410.gymapp.exercise.serde;

import static java.util.Map.of;

import android.content.Context;

import androidx.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.Result;

/**
 * Serializes/deserializes an {@link Exercise} to/from JSON.
 */
public class ExerciseSerdeJSON implements ExerciseSerde {
    private final static Map<String, Integer> ICONS = Map.of(
    );

    private String jsonString = null;
    private Context context = null;

    public ExerciseSerdeJSON(Context context, String jsonString) {
        this.context = context;
        this.jsonString = jsonString;
    }

    /**
     * Reads a {@code line} and decides if it has to be read as a string resource key.
     * @param line A string that can be a resource key
     * @return
     */
    private String getString(String line) {
        return line;
    }


    /**
     * Gets some icon res id from a string.
     * @param path A key for searching the res id.
     * @return The res id of the drawable
     */
    private int getIcon(String path) {
        Integer foundResId = ICONS.getOrDefault(path, null);
        return (foundResId == null || foundResId <= 0)
                ? R.drawable.ic_launcher_foreground
                : foundResId;
    }

    @NonNull
    @Override
    public Result<Void, Integer> serialize(List<Exercise> exercises) {
        return null;
    }

    //region Deserialization
    @NonNull
    @Override
    public Result<List<Exercise>, Integer> deserialize() {
        List<Exercise> exercises = new ArrayList<>();

        // The first item is an Array with multiple exercises
        JSONArray exercisesJSONArray;
        try { exercisesJSONArray = new JSONArray(jsonString); }
        catch (JSONException e) { return Result.err(R.string.file_json_deserialization_error); }

        // Each exercise is composed of simple data like: name, icon, tags
        for (int i = 0; i < exercisesJSONArray.length(); i++) {
            JSONObject exerciseJSONObject = exercisesJSONArray.optJSONObject(i);

            Result<Exercise, Integer> exerciseResult = deserializeExercise(exerciseJSONObject);

            if (exerciseResult.isErr())
                return Result.err(exerciseResult.getError());

            exercises.add(exerciseResult.getValue());
        }

        return Result.ok(exercises);
    }

    private Result<Exercise, Integer> deserializeExercise(JSONObject exerciseJSONObject) {
        if (exerciseJSONObject == null)
            return Result.err(R.string.file_json_deserialization_error_exercises);

        String name = getString(exerciseJSONObject.optString("name", "@exercise_name_notfound"));
        int icon = getIcon(exerciseJSONObject.optString("icon", "/exercise/notfound"));
        List<String> tags = parseTags(exerciseJSONObject);

        return Result.ok(new Exercise(name, icon, null, tags));
    }

    private List<String> parseTags(JSONObject exerciseJSONObject) {
        List<String> tagsList = new ArrayList<>();

        JSONArray tagsArray = exerciseJSONObject.optJSONArray("tags");
        if (tagsArray == null) return tagsList;

        for (int i = 0; i < tagsArray.length(); i++) {
            String tag = tagsArray.optString(i, null);
            if (tag != null) {
                tagsList.add(getString(tag));
            }
        }

        return tagsList;
    }
    //endregion
}
