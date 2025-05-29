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
import java.util.Objects;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExerciseTransitions;
import bdisfer1410.gymapp.exercise.models.routine.sets.ExerciseRest;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.models.routine.sets.ExerciseSetDynamic;
import bdisfer1410.gymapp.exercise.models.routine.sets.ExerciseSetStatic;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.android.ResourceUtils;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.java.Identifiable;

/**
 * Serializes/deserializes an {@link Exercise} to/from JSON.
 */
public class ExerciseSerdeJSON implements ExerciseSerde {
    public final static Map<String, Integer> ICONS = Map.of(
            "/generic/unknown", R.drawable.ic_missing,
            "/generic/fullbody", R.drawable.ic_exercise_generic_full_body,
            "/pose/push_up_from_flat_floor", R.drawable.ic_exercise_pose_push_up,
            "/pose/push_down_from_flat_floor", R.drawable.ic_exercise_pose_push_down,
            "/pose/pull_up_with_bar", R.drawable.ic_exercise_pose_pull_up,
            "/pose/pull_down_with_bar", R.drawable.ic_exercise_pose_pull_down
    );

    private final static String NAME_RESOURCE_KEY_PREFIX = "file_json_string_";

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
        if (!line.startsWith("@"))
            return line;

        String string_resource_key =
                NAME_RESOURCE_KEY_PREFIX + line.replaceFirst("@", "");

        return ResourceUtils.fromKeyOrDefault(context, string_resource_key, line);
    }


    /**
     * Gets some icon res id from a string.
     * @param path A key for searching the res id.
     * @return The res id of the drawable
     */
    private int getIcon(String path) {
        Integer foundResId = ICONS.getOrDefault(path, null);
        return (foundResId == null || foundResId <= 0)
                ? R.drawable.ic_missing
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

        Log.d("ExerciseSerdeJSON", String.format("des::[?]{name} = \"%s\"", name));
        Log.d("ExerciseSerdeJSON", String.format("des::[?]{icon} = %d", icon));
        Log.d("ExerciseSerdeJSON", String.format("des::[?]{tags} = \"%s\"", tags));

        Result<Exercise, Integer> exerciseOnlyQueueResult = parseExercise(
                exerciseJSONObject.optJSONObject("exercise")
        );

        TimerAnimationQueue queue = exerciseOnlyQueueResult.isOk()
                ? exerciseOnlyQueueResult.getValue().getQueue()
                : null;

        if (queue == null) {
            return Result.err(exerciseOnlyQueueResult.getError());
        }

        Exercise exercise = new Exercise(name, icon, queue, tags);
        exercise.setRepositories(
                exerciseOnlyQueueResult.getValue().repoPoses,
                exerciseOnlyQueueResult.getValue().repoTransitions,
                exerciseOnlyQueueResult.getValue().repoSets
        );

        return Result.ok(exercise);
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

    private Result<Exercise, Integer> parseExercise(JSONObject exerciseJSONObject) {
        // Prepare parsing
        if (exerciseJSONObject == null)
            return Result.err(R.string.file_json_deserialization_error_exercise);

        HashMap<String, ExercisePose> mapPoses = new HashMap<>();
        HashMap<String, ExerciseTransitions> mapTransitions = new HashMap<>();
        HashMap<String, TimerAnimation> mapSets = new HashMap<>();
        List<TimerAnimation> queue = new ArrayList<>();

        // Parse poses
        JSONArray posesJSONArray = exerciseJSONObject.optJSONArray("poses");
        if (posesJSONArray == null) return Result.err(R.string.file_json_deserialization_error_poses);

        for (int i = 0; i < posesJSONArray.length(); i++) {
            JSONObject poseJSONObject = posesJSONArray.optJSONObject(i);
            if (poseJSONObject == null) continue;

            String poseId = poseJSONObject.optString("id");
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{poses}[%d]{id} = \"%s\"", i, poseId));
            if (poseId.isEmpty()) continue; // Pose id is obligatory

            String poseName = getString(poseJSONObject.optString("name", "@pose_name_notfound"));
            int poseIcon = getIcon(poseJSONObject.optString("icon", "/pose/notfound"));
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{poses}[%d]{poseName} = \"%s\"", i, poseName));
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{poses}[%d]{poseIcon} = %d", i, poseIcon));

            mapPoses.put(poseId, new ExercisePose(poseName, poseIcon));
            if (mapPoses.get(poseId) != null)
                Objects.requireNonNull(mapPoses.get(poseId)).setId(poseId);
        }

        // Parse transitions
        JSONArray transitionsJSONArray = exerciseJSONObject.optJSONArray("transitions");
        if (transitionsJSONArray == null) return Result.err(R.string.file_json_deserialization_error_transitions);

        for (int i = 0; i < transitionsJSONArray.length(); i++) {
            JSONObject transitionJSONObject = transitionsJSONArray.optJSONObject(i);
            if (transitionJSONObject == null) continue;

            String transitionId = transitionJSONObject.optString("id");
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{transitions}[%d]{id} = \"%s\"", i, transitionId));
            if (transitionId.isEmpty()) continue; // Transition id is obligatory

            String transitionName = getString(transitionJSONObject.optString("name"));
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{transitions}[%d]{name} = \"%s\"", i, transitionName));

            JSONArray transitionPosesJSONArray = transitionJSONObject.optJSONArray("poses");
            if (transitionPosesJSONArray == null) return Result.err(R.string.file_json_deserialization_error_transition_poses);

            List<ExerciseTransition> transitionsList = new ArrayList<>();
            for (int j = 0; j < transitionPosesJSONArray.length(); j++) {
                JSONObject transitionPoseJSONObject = transitionPosesJSONArray.optJSONObject(j);
                if (transitionPoseJSONObject == null) continue;

                String transitionPoseId = transitionPoseJSONObject.optString("id");
                Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{transitions}[%d]{poses}[%d]{id} = \"%s\"", i, j, transitionPoseId));
                if (!mapPoses.containsKey(transitionPoseId)) continue; // transitionPose id must exists in poses

                int transitionPoseTime = transitionPoseJSONObject.optInt("time", -1);
                Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{transitions}[%d]{poses}[%d]{time} = %d", i, j, transitionPoseTime));
                if (transitionPoseTime < 0) continue; // transitionPose time can't break the laws of the universe

                transitionsList.add(new ExerciseTransition(mapPoses.get(transitionPoseId), transitionPoseTime));
            }
            mapTransitions.put(transitionId, new ExerciseTransitions(transitionName, transitionsList));
            if (mapTransitions.get(transitionId) != null)
                Objects.requireNonNull(mapTransitions.get(transitionId)).setId(transitionId);
        }

        // Parse sets
        JSONArray setsJSONArray = exerciseJSONObject.optJSONArray("sets");
        if (setsJSONArray == null) return Result.err(R.string.file_json_deserialization_error_sets);

        for (int i = 0; i < setsJSONArray.length(); i++) {
            // { id: "str", type: "str", data: { ??? } }
            JSONObject setJSONObject = setsJSONArray.optJSONObject(i);
            if (setJSONObject == null) continue;

            String setId = setJSONObject.optString("id");
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{id} = \"%s\"", i, setId));
            if (setId.isEmpty()) continue; // Set id is obligatory

            String setType = setJSONObject.optString("type");
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{type} = \"%s\"", i, setType));
            if (setType.isEmpty()) continue; // Set type is obligatory

            JSONObject setDataJSONObject = setJSONObject.optJSONObject("data");
            if (setDataJSONObject == null) continue;

            switch (setType) {
                case "rest": // { duration: 123 }
                    int setDataRestDuration = setDataJSONObject.optInt("duration", -1);
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{duration} = %d", i, setDataRestDuration));
                    if (setDataRestDuration < 0) continue;

                    mapSets.put(setId, new ExerciseRest(setDataRestDuration));
                    break;

                case "set_static": // { name: "str", pose: "id" duration: 123 }
                    String setDataSetStaticName = getString(setDataJSONObject.optString("name"));
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{name} = \"%s\"", i, setDataSetStaticName));

                    String setDataSetStaticPose = setDataJSONObject.optString("pose");
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{pose} = \"%s\"", i, setDataSetStaticPose));
                    if (!mapPoses.containsKey(setDataSetStaticPose)) continue; // setDataSetStaticPose id must exists in poses

                    int setDataSetStaticDuration = setDataJSONObject.optInt("duration", -1);
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{duration} = %d", i, setDataSetStaticDuration));
                    if (setDataSetStaticDuration < 0) continue;

                    mapSets.put(setId, new ExerciseSetStatic(setDataSetStaticName, mapPoses.get(setDataSetStaticPose), setDataSetStaticDuration));
                    break;

                case "set_dynamic":
                    String setDataSetDynamicName = getString(setDataJSONObject.optString("name"));
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{name} = \"%s\"", i, setDataSetDynamicName));

                    String setDataSetDynamicTransition = setDataJSONObject.optString("transition");
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{transition} = \"%s\"", i, setDataSetDynamicTransition));
                    if (!mapTransitions.containsKey(setDataSetDynamicTransition)) continue; // setDataSetDynamicTransition id must exists in poses

                    int setDataSetDynamicRepetitions = setDataJSONObject.optInt("repetitions", -1);
                    Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{sets}[%d]{data}{repetitions} = %d", i, setDataSetDynamicRepetitions));
                    if (setDataSetDynamicRepetitions < 0) continue;

                    List<ExerciseTransition> let = mapTransitions.get(setDataSetDynamicTransition).list;
                    let = let == null ? new ArrayList<>() : let; // Android cries if I don't do this

                    ExerciseSetDynamic esd = new ExerciseSetDynamic(setDataSetDynamicName, let, setDataSetDynamicRepetitions);
                    esd.transitionsId = setDataSetDynamicTransition;
                    mapSets.put(setId, esd);
                    break;
            }
            if (mapSets.get(setId) instanceof Identifiable)
                ((Identifiable) Objects.requireNonNull(mapSets.get(setId))).setId(setId);
        }

        // Parse queue
        JSONArray queueJSONArray = exerciseJSONObject.optJSONArray("queue");
        if (queueJSONArray == null) return Result.err(R.string.file_json_deserialization_error_queue);

        for (int i = 0; i < queueJSONArray.length(); i++) {
            String queueSetName = queueJSONArray.optString(i);
            Log.d("ExerciseSerdeJSON", String.format("des::[?]{exercise}{queue}[%d] = %s", i, queueSetName));

            TimerAnimation set = mapSets.getOrDefault(queueSetName, null);
            if (set == null) continue;

            queue.add(set);
        }

        // Build empty exercise with only queue and repos
        Exercise repos = new Exercise("", 1, new TimerAnimationQueue(queue), null);
        repos.setRepositories(
                new ArrayList<>(mapPoses.values()),
                new ArrayList<>(mapTransitions.values()),
                new ArrayList<>(mapSets.values())
        );
        return Result.ok(repos);
    }
    //endregion
}
