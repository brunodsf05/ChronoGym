package bdisfer1410.gymapp.exercise.serde;

import android.content.Context;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.activity.MainActivity;
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

    public static Result<Void, Integer> addOne(Context context, Exercise exercise) {
        // Get serialized input
        String jsonString = QuickFileManager
                .with(context)
                .file(FILENAME)
                .read();

        if (jsonString == null) return Result.err(R.string.file_json_deserialization_error);

        // Deserialize file
        ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(context, jsonString);
        Result<List<Exercise>, Integer> resultDes = exerciseSerdeJSON.deserialize();
        if (resultDes.isErr()) return Result.err(resultDes.getError());

        List<Exercise> exerciseList = resultDes.getValue();

        // Add one
        exerciseList.add(exercise);

        // Serialize into string
        Result<String, Integer> resultSer = exerciseSerdeJSON.serialize(exerciseList);
        if (resultSer.isErr()) return Result.err(resultSer.getError());
        String serialized = resultSer.getValue();

        // Save serialized output
        boolean writeSuccess = QuickFileManager
                .with(context)
                .file(FILENAME)
                .save(serialized);

        return writeSuccess
                ? Result.ok(null)
                : Result.err(R.string.file_json_serialization_error);
    }
}
