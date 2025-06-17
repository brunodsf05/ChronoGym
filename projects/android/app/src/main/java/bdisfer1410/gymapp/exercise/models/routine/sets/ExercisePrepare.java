package bdisfer1410.gymapp.exercise.models.routine.sets;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.StringUtils;
import bdisfer1410.gymapp.util.media.Beep;

public class ExercisePrepare extends Identifiable implements TimerAnimation, Serializable, ExerciseCard {
    private final int DEFAULT_ICON = R.drawable.ic_exercise_rest;
    private final int MS_DURATION = 10_000;
    private final int MS_BEEPS = 1_000;
    private final int MS_BEEP_TIMES = 3;
    private int remainingBeeps;
    private boolean hasToWaitFirstBeep = true;

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseNameText(R.string.model_exercise_prepare);
        timer.setExerciseProgressMax(MS_DURATION);
        timer.setExerciseIconImage(DEFAULT_ICON);
        timer.setSetCounterText("");

        remainingBeeps = MS_BEEP_TIMES;
        hasToWaitFirstBeep = true;

        return MS_DURATION;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {

    }

    @Override
    public void onUpdate(@NonNull TimerFragment timer, ValueAnimator animation) {
        int elapsedMs = (int) animation.getAnimatedValue();

        int remainingMs = MS_DURATION - elapsedMs;
        int remainingSeconds = (remainingMs / 1000) + 1;
        timer.setExerciseCounterText(String.valueOf(remainingSeconds));

        timer.setExerciseProgressValue(elapsedMs);
    }

    @Override
    public int onTick(@NonNull TimerFragment timer) {
        if (hasToWaitFirstBeep) {
            hasToWaitFirstBeep = false;
            return MS_DURATION - ((MS_BEEP_TIMES + 1) * MS_BEEPS);
        }
        else if (remainingBeeps > 0) {
            remainingBeeps--;
            Beep.emit(Beep.Type.NORMAL);
            return MS_BEEPS;
        }

        Beep.emit(Beep.Type.WARNING);
        return 0;
    }

    @Override
    public int calculateDuration() {
        return MS_DURATION;
    }
    //endregion

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return DEFAULT_ICON;
    }

    @NonNull
    @Override
    public String getCardName() {
        return "Prepárate";
    }

    @Nullable
    @Override
    public String getCardTags() {
        return getPrettierId();
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return StringUtils.formatMsIntoTime(MS_DURATION);
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return "";
    }

    @NonNull
    @Override
    public ExerciseCardState getCardState() {
        return ExerciseCardState.NORMAL;
    }
    //endregion
}
