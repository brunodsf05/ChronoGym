package bdisfer1410.gymapp.exercise.models.routine.sets;

import java.io.Serializable;

import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;

public abstract class ExerciseSet extends Identifiable implements TimerAnimation, Serializable {
    protected final String name;

    public ExerciseSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
