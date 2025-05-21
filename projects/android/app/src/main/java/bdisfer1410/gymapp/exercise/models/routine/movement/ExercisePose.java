package bdisfer1410.gymapp.exercise.models.routine.movement;

import java.io.Serializable;

/**
 * Stores information about an static pose that's done during any exercise.
 */
public class ExercisePose implements Serializable {
    private final String name;
    private final Integer icon;

    public ExercisePose(String name, Integer icon) {
        this.name = name;

        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }
}
