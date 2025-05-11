package bdisfer1410.gymapp.exercise.models;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;

public class ExerciseRest implements TimerAnimation {
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

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseNameText(R.string.exercise_timer_rest);
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseIconImage(R.drawable.ic_exercise_rest);
        return msDuration;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {

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
    //endregion
}
