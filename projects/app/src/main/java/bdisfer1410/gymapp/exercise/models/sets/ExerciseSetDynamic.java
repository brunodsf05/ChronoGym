package bdisfer1410.gymapp.exercise.models.sets;

import java.util.List;

import bdisfer1410.gymapp.exercise.models.movement.ExerciseTransition;

public class ExerciseSetDynamic extends ExerciseSet {
    private final List<ExerciseTransition> transitions;
    private final int reps;
    private final int numberOfPoses;
    private final int msDuration;


    public ExerciseSetDynamic(String name, List<ExerciseTransition> transitions, int reps) {
        super(name);
        this.transitions = transitions;
        this.reps = reps;

        this.numberOfPoses = transitions.size();

        int oneRepMsDuration = transitions.stream().mapToInt(ExerciseTransition::getMsToNext).sum();
        int lastPoseMsDuration = transitions.get(this.numberOfPoses - 1).getMsToNext();
        this.msDuration = oneRepMsDuration * reps - lastPoseMsDuration;
    }
}