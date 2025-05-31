package bdisfer1410.gymapp.exercise.serde;

import android.content.Context;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.data.QuickFileManager;

public class ExerciseSerdeHelper {
    public static final String FILENAME = "user_exercises.json";
    public static Result<Void, Integer> addOne(Context context, Exercise exercise) {
        // Get serialized input
        String jsonString = QuickFileManager
                .with(context)
                .file(FILENAME)
                .read();

        if (jsonString == null) return Result.err(R.string.file_json_deserialization_error);

        // Deserialize file
        ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(context, jsonString);
        Result<List<Exercise>, Integer> result = exerciseSerdeJSON.deserialize();
        if (result.isErr()) return Result.err(result.getError());

        List<Exercise> exerciseList = result.getValue();

        // Add one
        exerciseList.add(exercise);

        // Serialize into string
        String serialized = "";

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
