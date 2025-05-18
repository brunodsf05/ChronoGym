package bdisfer1410.gymapp.exercise.models.sets;

import java.io.Serializable;

import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;

public abstract class ExerciseSet implements TimerAnimation, Serializable {
    protected final String name;

    public ExerciseSet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
