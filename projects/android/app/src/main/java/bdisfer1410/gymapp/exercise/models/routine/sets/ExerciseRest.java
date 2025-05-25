package bdisfer1410.gymapp.exercise.models.routine.sets;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.java.StringUtils;
import bdisfer1410.gymapp.util.media.Beep;

public class ExerciseRest extends Identifiable implements TimerAnimation, Serializable, ExerciseCard {
    private final int DEFAULT_ICON = R.drawable.ic_exercise_rest;
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
        timer.setExerciseIconImage(DEFAULT_ICON);

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
            Beep.emit(Beep.Type.WARNING);
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

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return DEFAULT_ICON;
    }

    @NonNull
    @Override
    public String getCardName() {
        return "Descanso";
    }

    @Nullable
    @Override
    public String getCardTags() {
        return getPrettierId();
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return StringUtils.formatMsIntoTime(msDuration);
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return "";
    }
    //endregion
}
