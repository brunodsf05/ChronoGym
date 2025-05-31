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
import java.util.stream.Collectors;

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
import bdisfer1410.gymapp.util.java.ListTools;

/**
 * Serializes/deserializes an {@link Exercise} to/from JSON.
 */
public class ExerciseSerdeJSON implements ExerciseSerde {
    public final static String ICON_MISSING_PATH = "/generic/unknown";

    public final static Map<String, Integer> ICONS = Map.of(
            ICON_MISSING_PATH, R.drawable.ic_missing,
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

    //region Utils
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
     * Reads a {@code line} and tries to transform it into a translatable string.
     * @param line The line to assure its storage as translation.
     * @return The translatable that starts with "@"
     */
    private String serializeString(String line) {
        // Already a translation
        if (line.startsWith("@"))
            return line;

        // Search string key from value
        String stringResId = ResourceUtils.findKey(context, line, NAME_RESOURCE_KEY_PREFIX);
        if (stringResId == null)
            return line;


        return "@"+stringResId.replace(NAME_RESOURCE_KEY_PREFIX,"");
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

    /**
     * Gets some icon path from res id.
     * @param icon The res id to transform into a icon path.
     * @return The icon path.
     */
    private String iconToPath(int icon) {
        String path = ListTools.getKeyByValue(ICONS, icon);
        return Objects.requireNonNullElse(path, ICON_MISSING_PATH);
    }
    //endregion

    @NonNull
    @Override
    public Result<String, Integer> serialize(List<Exercise> exercises) {
        JSONArray exercisesJSONArray = new JSONArray();

        try {
            for (Exercise exercise : exercises) {
                // Add object to JSON
                JSONObject exerciseAllJSONObject = new JSONObject();
                exercisesJSONArray.put(exerciseAllJSONObject);
                // Add information
                exerciseAllJSONObject.put("name", serializeString(exercise.getName()));
                exerciseAllJSONObject.put("icon", iconToPath(exercise.getIcon()));
                exerciseAllJSONObject.put("tags", exercise.getTags().stream().map(this::serializeString).collect(Collectors.toList()));
                // Initialize exercise resources
                JSONObject exerciseJSONObject = new JSONObject();
                JSONArray exercisePoseJSONArray = new JSONArray();
                JSONArray exerciseTransitionsJSONArray = new JSONArray();
                JSONArray exerciseSetsJSONArray = new JSONArray();
                exerciseAllJSONObject.put("exercise", exerciseJSONObject);
                exerciseJSONObject.put("poses", exercisePoseJSONArray);
                exerciseJSONObject.put("transitions", exerciseTransitionsJSONArray);
                exerciseJSONObject.put("sets", exerciseSetsJSONArray);
                // Add poses
                for (ExercisePose pose: exercise.repoPoses) {
                    JSONObject exercisePoseJSONObject = new JSONObject();
                    exercisePoseJSONArray.put(exercisePoseJSONObject);
                    exercisePoseJSONObject.put("id", pose.getId());
                    exercisePoseJSONObject.put("name", serializeString(pose.getName()));
                    exercisePoseJSONObject.put("icon", iconToPath(pose.getIcon()));
                }
                // Add transitions
                for (ExerciseTransitions transitions : exercise.repoTransitions) {
                    JSONObject exerciseTransitionsJSONObject = new JSONObject();
                    exerciseTransitionsJSONArray.put(exerciseTransitionsJSONObject);

                    exerciseTransitionsJSONObject.put("id", transitions.getId());
                    exerciseTransitionsJSONObject.put("name", serializeString(transitions.getName()));

                    JSONArray exerciseTransitionListJSONArray = new JSONArray();
                    exerciseTransitionsJSONObject.put("poses", exerciseTransitionListJSONArray);
                    for (ExerciseTransition transition : transitions.list) {
                        JSONObject exerciseTransitionPoseJSONObject = new JSONObject();
                        exerciseTransitionListJSONArray.put(exerciseTransitionPoseJSONObject);

                        exerciseTransitionPoseJSONObject.put("id", transition.getPose().getId());
                        exerciseTransitionPoseJSONObject.put("time", transition.getMsToNext());
                    }
                }
                // Add sets
                for (TimerAnimation timerAnimation : exercise.repoSets) {
                    // Prepare object to add
                    JSONObject exerciseSetJSONObject = new JSONObject();
                    String type, id;

                    // Get id
                    if (timerAnimation instanceof Identifiable) {
                        id = ((Identifiable) timerAnimation).getId();
                    }
                    else {
                        Log.w("ExerciseSerdeJSON", "ser::[?]{exercise}{sets][?]{id} Not found :(");
                        continue;
                    }

                    // Get information
                    if (timerAnimation instanceof ExerciseRest) {
                        type = "rest";
                    }
                    else if (timerAnimation instanceof ExerciseSetStatic) {
                        type = "set_static";
                    }
                    else if (timerAnimation instanceof ExerciseSetDynamic) {
                        type = "set_dynamic";
                    }
                    else {
                        Log.w("ExerciseSerdeJSON", "ser::[?]{exercise}{sets][?]{type} Not valid :(");
                        continue;
                    }

                    exerciseSetJSONObject.put("id", id);
                    exerciseSetJSONObject.put("type", type);

                    // Get data
                    JSONObject exerciseSetDataJSONObject = new JSONObject();
                    exerciseSetJSONObject.put("data", exerciseSetDataJSONObject);

                    switch (type) {
                        case "rest":
                            exerciseSetDataJSONObject.put("duration", ((ExerciseRest) timerAnimation).getMsDuration());
                            break;

                        case "set_static":
                            exerciseSetDataJSONObject.put("name", serializeString(((ExerciseSetStatic) timerAnimation).getName()));
                            exerciseSetDataJSONObject.put("pose", ((ExerciseSetStatic) timerAnimation).getPose().getId());
                            exerciseSetDataJSONObject.put("duration", ((ExerciseSetStatic) timerAnimation).getMsDuration());
                            break;

                        case "set_dynamic":
                            exerciseSetDataJSONObject.put("name", serializeString(((ExerciseSetDynamic) timerAnimation).getName()));
                            exerciseSetDataJSONObject.put("transition", ((ExerciseSetDynamic) timerAnimation).transitionsId);
                            exerciseSetDataJSONObject.put("repetitions", ((ExerciseSetDynamic) timerAnimation).getReps());
                            break;

                        default:
                            Log.w("ExerciseSerdeJSON", "ser::[?]{exercise}{sets][?]{type} Not valid :(");
                            continue;
                    }

                    exerciseJSONObject.put("queue", exercise.getQueue().list.stream()
                            .filter(ta -> ta instanceof Identifiable)
                            .map(ta -> ((Identifiable) ta).getId())
                            .collect(Collectors.toList())
                    );

                    // Add object to array
                    exerciseSetsJSONArray.put(exerciseSetJSONObject);
                }
            }
        }
        catch (JSONException e) {
            Log.e("ExerciseSerdeJSON", e.toString());
        }

        return Result.ok(exercisesJSONArray.toString());
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
