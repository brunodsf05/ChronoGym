package bdisfer1410.gymapp.exercise.models.routine.movement;

import java.io.Serializable;
import java.util.List;

import bdisfer1410.gymapp.util.java.Identifiable;

/**
 * Bundles an {@link List<ExerciseTransition>} with other data.
 */
public class ExerciseTransitions extends Identifiable implements Serializable {
    public String name;
    public List<ExerciseTransition> list;

    public ExerciseTransitions(String name, List<ExerciseTransition> list) {
        this.name = name;
        this.list = list;
    }
}
