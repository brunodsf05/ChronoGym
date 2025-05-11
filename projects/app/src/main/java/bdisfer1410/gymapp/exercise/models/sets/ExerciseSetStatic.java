package bdisfer1410.gymapp.exercise.models.sets;

import bdisfer1410.gymapp.exercise.models.movement.ExercisePose;

public class ExerciseSetStatic extends ExerciseSet {
    private final ExercisePose pose;
    private final int msDuration;

    public ExerciseSetStatic(String name, ExercisePose pose, int msDuration) {
        super(name);
        this.pose = pose;
        this.msDuration = msDuration;
    }
}
