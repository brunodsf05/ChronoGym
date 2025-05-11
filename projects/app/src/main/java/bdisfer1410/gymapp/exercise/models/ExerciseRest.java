package bdisfer1410.gymapp.exercise.models;

public class ExerciseRest {
    private int msDuration;

    public ExerciseRest(int msDuration) {
        setMsDuration(msDuration);
    }

    public int getMsDuration() {
        return msDuration;
    }

    public void setMsDuration(int msDuration) {
        this.msDuration = Math.max(msDuration, 0);
    }
}
