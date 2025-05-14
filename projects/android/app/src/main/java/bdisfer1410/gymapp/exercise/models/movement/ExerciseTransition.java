package bdisfer1410.gymapp.exercise.models.movement;

/**
 * Bundles an {@link ExercisePose} with the time to start the next {@link ExerciseTransition}.
 * This time is in milliseconds.
 */
public class ExerciseTransition {
    private final ExercisePose pose;
    private final int msToNext;

    public ExerciseTransition(ExercisePose pose, int msToNext) {
        this.pose = pose;
        this.msToNext = msToNext;
    }

    public ExercisePose getPose() {
        return pose;
    }

    public int getMsToNext() {
        return msToNext;
    }
}
