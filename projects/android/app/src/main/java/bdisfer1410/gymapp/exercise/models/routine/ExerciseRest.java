package bdisfer1410.gymapp.exercise.models.routine;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import java.io.Serializable;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.BeepGenerator;

public class ExerciseRest implements TimerAnimation, Serializable {
    private final int msDuration;
    private final int msBeforeBeepStart = 10_000;
    private int msToEmitBeep;
    private boolean hasToEmitBeep;

    public ExerciseRest(int msDuration) {
        this.msDuration = Math.max(msDuration, 0);
    }

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseNameText(R.string.model_exercise_rest);
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseIconImage(R.drawable.ic_exercise_rest);

        hasToEmitBeep = false;
        msToEmitBeep = (msDuration < msBeforeBeepStart)
                ? 0
                : msDuration - msBeforeBeepStart;

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
        if (hasToEmitBeep) {
            BeepGenerator.emit(BeepGenerator.Type.WARNING);
            return 0;
        }

        hasToEmitBeep = true;
        return msToEmitBeep;
    }

    @Override
    public int calculateDuration() {
        return msDuration;
    }
    //endregion
}
