package bdisfer1410.gymapp.exercise.serde;

import androidx.annotation.NonNull;

import java.util.List;

import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.Result;

/**
 * Defines a contract for serializing and deserializing {@link Exercise} instances.
 * Implementations must handle internally where the data is stored or loaded from.
 */
public interface ExerciseSerde {
    /**
     * Serializes an {@link Exercise}. The implementing object must
     * internally define the destination where the serialized file will be saved.
     *
     * @param exercise The {@link Exercise} instance to serialize.
     * @return A {@link Result} where {@code Ok<String>} contains the serialized exercise list,
     *         and {@code Err<Integer>} contains a string resource ID from
     *         {@link bdisfer1410.gymapp.R.string} describing the error.
     */
    @NonNull Result<String, Integer> serialize(List<Exercise> exercises);

    /**
     * Deserializes an {@link Exercise}. The implementing object must
     * internally define the source from which the exercise is loaded.
     *
     * @return A {@link Result} where {@code Ok<Exercise>} contains the deserialized object,
     *         and {@code Err<Integer>} contains a string resource ID from
     *         {@link bdisfer1410.gymapp.R.string} describing the error.
     */
    @NonNull Result<List<Exercise>, Integer> deserialize();
}
