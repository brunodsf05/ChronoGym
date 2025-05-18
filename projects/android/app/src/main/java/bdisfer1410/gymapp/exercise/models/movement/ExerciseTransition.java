package bdisfer1410.gymapp.exercise.models.movement;

import java.io.Serializable;

/**
 * Bundles an {@link ExercisePose} with the time to start the next {@link ExerciseTransition}.
 * This time is in milliseconds.
 */
public class ExerciseTransition implements Serializable {
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
