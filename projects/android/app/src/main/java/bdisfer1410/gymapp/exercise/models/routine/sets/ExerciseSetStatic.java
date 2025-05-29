package bdisfer1410.gymapp.exercise.models.routine.sets;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.java.StringUtils;
import bdisfer1410.gymapp.util.media.Beep;

public class ExerciseSetStatic extends ExerciseSet {
    private final ExercisePose pose;
    private final int msDuration;

    public ExerciseSetStatic(String name, ExercisePose pose, int msDuration) {
        super(name);
        this.pose = pose;
        this.msDuration = msDuration;
    }

    public ExercisePose getPose() {
        return pose;
    }

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseNameText(name);
        timer.setExerciseIconImage(
                Objects.requireNonNullElse(pose.getIcon(), R.drawable.ic_exercise_default)
        );
        Beep.emit(Beep.Type.NORMAL);
        return msDuration;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {
        Beep.emit(Beep.Type.HIGH);
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

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return pose.getIcon();
    }

    @NonNull
    @Override
    public String getCardName() {
        return getName();
    }

    @Nullable
    @Override
    public String getCardTags() {
        return String.format("%s, Set estático", getPrettierId());
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
