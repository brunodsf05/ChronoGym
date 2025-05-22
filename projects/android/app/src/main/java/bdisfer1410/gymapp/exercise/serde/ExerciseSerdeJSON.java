package bdisfer1410.gymapp.exercise.serde;

import static java.util.Map.of;

import android.content.Context;
import android.util.Log;

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
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
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

        Result<TimerAnimationQueue, Integer> queueResult = parseExercise(
                exerciseJSONObject.optJSONObject("exercise")
        );

        return queueResult.isOk()
                ? Result.ok(new Exercise(name, icon, null, tags))
                : Result.err(queueResult.getError());
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

    private Result<TimerAnimationQueue, Integer> parseExercise(JSONObject exerciseJSONObject) {
        // Prepare parsing
        if (exerciseJSONObject == null)
            return Result.err(R.string.file_json_deserialization_error_exercise);

        HashMap<String, ExercisePose> poses = new HashMap<>();

        // Parse poses
        JSONArray posesJSONArray = exerciseJSONObject.optJSONArray("poses");
        if (posesJSONArray == null) return Result.err(R.string.file_json_deserialization_error_poses);

        for (int i = 0; i < posesJSONArray.length(); i++) {
            JSONObject poseJSONObject = posesJSONArray.optJSONObject(i);
            if (poseJSONObject == null) continue;

            String poseId = poseJSONObject.optString("id");
            if (poseId.isEmpty()) continue; // Pose id is obligatory

            String poseName = getString(exerciseJSONObject.optString("name", "@pose_name_notfound"));
            int poseIcon = getIcon(exerciseJSONObject.optString("icon", "/pose/notfound"));

            poses.put(poseId, new ExercisePose(poseName, poseIcon));
        }

        // Parse transitions
        JSONArray transitionsJSONArray = exerciseJSONObject.optJSONArray("transitions");
        if (transitionsJSONArray == null) return Result.err(R.string.file_json_deserialization_error_transitions);

        for (int i = 0; i < transitionsJSONArray.length(); i++) {
            JSONObject transitionJSONObject = transitionsJSONArray.optJSONObject(i);
            if (transitionJSONObject == null) continue;

            String transitionId = transitionJSONObject.optString("id");
            if (transitionId.isEmpty()) continue; // Transition id is obligatory

            JSONArray transitionPosesJSONArray = transitionJSONObject.optJSONArray("poses");
            if (transitionPosesJSONArray == null) return Result.err(R.string.file_json_deserialization_error_transition_poses);

            for (int j = 0; j < transitionPosesJSONArray.length(); j++) {
                JSONObject transitionPoseJSONObject = transitionPosesJSONArray.optJSONObject(i);
                if (transitionPoseJSONObject == null) continue;

                String transitionPoseId = transitionJSONObject.optString("id");
                if (transitionPoseId.isEmpty()) continue; // transitionPose id is obligatory

                int transitionPoseTime = transitionPoseJSONObject.optInt("time", -1);
                if (transitionPoseTime <= 0) continue; // transitionPose time can't break the laws of the universe

            }
        }

        // Parse sets
        JSONArray setsJSONArray = exerciseJSONObject.optJSONArray("sets");
        if (setsJSONArray == null) return Result.err(R.string.file_json_deserialization_error_sets);

        // Parse queue
        JSONArray queueJSONArray = exerciseJSONObject.optJSONArray("queue");
        if (queueJSONArray == null) return Result.err(R.string.file_json_deserialization_error_queue);

        return Result.err(R.string.file_json_deserialization_error);
    }
    //endregion
}
