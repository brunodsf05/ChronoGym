package bdisfer1410.gymapp.exercise.models.sets;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import java.util.Objects;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.BeepGenerator;

public class ExerciseSetStatic extends ExerciseSet {
    private final ExercisePose pose;
    private final int msDuration;

    public ExerciseSetStatic(String name, ExercisePose pose, int msDuration) {
        super(name);
        this.pose = pose;
        this.msDuration = msDuration;
    }

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseNameText(name);
        timer.setExerciseIconImage(
                Objects.requireNonNullElse(pose.getIcon(), R.drawable.ic_exercise_default)
        );
        BeepGenerator.emit(BeepGenerator.Type.NORMAL);
        return msDuration;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {
        BeepGenerator.emit(BeepGenerator.Type.HIGH);
    }

    @Override
    public void onUpdate(@NonNull TimerFragment timer, ValueAnimator animation) {
        int elapsedMs = (int) animation.getAnimatedValue();

        int remainingMs = msDuration - elapsedMs;
        int remainingSeconds = (remainingMs / 1000) + 1;
        timer.setExerciseCounterText(String.valueOf(remainingSeconds));

        timer.setExerciseProgressValue(elapsedMs);
    }

    @Override
    public int onTick(@NonNull TimerFragment timer) {
        return 0;
    }

    @Override
    public int calculateDuration() {
        return msDuration;
    }
    //endregion
}
