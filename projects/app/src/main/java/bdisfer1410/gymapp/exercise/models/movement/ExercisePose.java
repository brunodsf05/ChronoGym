package bdisfer1410.gymapp.exercise.models.movement;

/**
 * Stores information about an static pose that's done during any exercise.
 */
public class ExercisePose {
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
