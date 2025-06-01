package bdisfer1410.gymapp.exercise.serde;

import android.content.Context;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.data.QuickFileManager;

public class ExerciseSerdeHelper {
    public static final String FILENAME = "user_exercises.json";
    public static final String JSON_EMPTY = "[]";

    /**
     * @return If the file was restarted into an empty list
     */
    public static boolean restart(Context context) {
        return QuickFileManager
                .with(context)
                .file(FILENAME)
                .save(JSON_EMPTY);
    }

    /**
     * Adds one exercise to the user's exercise list file.
     * @return Result.getError() gives string resId.
     */
    public static Result<Void, Integer> addOne(Context context, Exercise exercise) {
        return addMultiple(context, List.of(exercise));
    }

    /**
     * Adds multiple exercises to the user's exercise list file.
     * @return Result.getError() gives string resId.
     */
    public static Result<Void, Integer> addMultiple(Context context, List<Exercise> exercises) {
        // Load exercise from JSON
        Result<List<Exercise>, Integer> resultDes = loadExercisesFromJSON(context);
        if (resultDes.isErr()) return Result.err(resultDes.getError());
        List<Exercise> exerciseList = resultDes.getValue();

        // Add one
        exerciseList.addAll(exercises);

        // Serialize into string
        return saveExercisesIntoJSON(context, exerciseList, R.string.file_json_serialization_error);
    }

    /**
     * Replaces one exercise from the user's exercise list file with another one.
     * The exercise to replace is selected via an {@code indexToOverwrite}.
     * @return Result.getError() gives string resId.
     */
    public static Result<Void, Integer> replaceOne(Context context, Exercise exercise, int indexToOverwrite) {
        // Load exercise from JSON
        Result<List<Exercise>, Integer> resultDes = loadExercisesFromJSON(context);
        if (resultDes.isErr()) return Result.err(resultDes.getError());
        List<Exercise> exerciseList = resultDes.getValue();

        // Overwrite
        boolean isIndexInside = indexToOverwrite >= 0 && indexToOverwrite <exerciseList.size();
        if (!isIndexInside) {
            return Result.err(R.string.file_json_serialization_error_overwrite);
        }

        exerciseList.set(indexToOverwrite, exercise);

        // Serialize into string
        return saveExercisesIntoJSON(context, exerciseList, R.string.file_json_serialization_error_overwrite);
    }

    private static Result<List<Exercise>, Integer> loadExercisesFromJSON(Context context) {
        // Get serialized input
        String jsonString = QuickFileManager
                .with(context)
                .file(FILENAME)
                .read();

        if (jsonString == null) return Result.err(R.string.file_json_deserialization_error);

        // Deserialize file
        ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(context, jsonString);
        return exerciseSerdeJSON.deserialize();
    }

    private static Result<Void, Integer> saveExercisesIntoJSON(Context context, List<Exercise> exerciseList, int writeError) {
        ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(context, "");

        Result<String, Integer> resultSer = exerciseSerdeJSON.serialize(exerciseList);
        if (resultSer.isErr()) return Result.err(resultSer.getError());
        String serialized = resultSer.getValue();

        // Save serialized output
        boolean writeSuccess = QuickFileManager
                .with(context)
                .file(FILENAME)
                .save(serialized);

        return writeSuccess ? Result.ok(null) : Result.err(writeError);
    }
}
