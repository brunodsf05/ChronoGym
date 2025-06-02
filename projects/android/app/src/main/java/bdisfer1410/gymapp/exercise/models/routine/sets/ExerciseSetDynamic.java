package bdisfer1410.gymapp.exercise.models.routine.sets;

import android.animation.ValueAnimator;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;
import java.util.Objects;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.java.StringUtils;
import bdisfer1410.gymapp.util.media.Beep;

public class ExerciseSetDynamic extends ExerciseSet {
    private final int DEFAULT_ICON = R.drawable.ic_exercise_default;
    private final List<ExerciseTransition> transitions;
    public String transitionsId = "";
    private int reps;
    private final int numberOfPoses;
    private final int msDuration;
    private int poseIndex = 0;
    private int repsDone = 0;

    public ExerciseSetDynamic(String name, List<ExerciseTransition> transitions, int reps) {
        super(name);
        this.transitions = transitions;
        this.reps = reps;

        this.numberOfPoses = transitions.size();

        int oneRepMsDuration = transitions.stream().mapToInt(ExerciseTransition::getMsToNext).sum();
        int lastPoseMsDuration = transitions.isEmpty() ? 0 : transitions.get(this.numberOfPoses - 1).getMsToNext();
        this.msDuration = oneRepMsDuration * reps - lastPoseMsDuration;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseNameText(name);
        timer.setExerciseCounterText("0");

        boolean doesFirstPoseHasNotIcon = numberOfPoses > 0 && transitions.get(0).getPose().getIcon() == null;

        if (doesFirstPoseHasNotIcon) {
            timer.setExerciseIconImage(DEFAULT_ICON);
        }

        poseIndex = 0;
        repsDone = 0;

        return msDuration;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {
        Beep.emit(Beep.Type.HIGH);
    }

    @Override
    public void onUpdate(@NonNull TimerFragment timer, ValueAnimator animation) {
        int elapsedMs = (int) animation.getAnimatedValue();
        timer.setExerciseProgressValue(elapsedMs);
    }

    @Override
    public int onTick(@NonNull TimerFragment timer) {
        ExerciseTransition transition = transitions.get(poseIndex);
        Log.d("ExerciseSetDynamic", String.format("New pose %s", transition.getPose().getName()));

        // Update timer with current transition
        Integer icon = transition.getPose().getIcon();
        if (icon != null) timer.setExerciseIconImage(icon);

        // Manage reps cycle
        if (++poseIndex >= numberOfPoses) {
            repsDone++;
            poseIndex = 0;
            timer.setExerciseCounterText(String.valueOf(repsDone));
        }

        // Manage onTick cycle
        if (repsDone >= reps) {
            return 0;
        }

        Beep.emit(Beep.Type.NORMAL);
        return transition.getMsToNext();
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
        return Objects.requireNonNullElse(
                transitions.get(0).getPose().getIcon(),
                DEFAULT_ICON
        );
    }

    @NonNull
    @Override
    public String getCardName() {
        return getName();
    }

    @Nullable
    @Override
    public String getCardTags() {
        return String.format("%s, Set dinámico", getPrettierId());
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return String.format("x%s", reps);
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return StringUtils.formatMsIntoTime(msDuration);
    }

    @NonNull
    @Override
    public ExerciseCardState getCardState() {
        return ExerciseCardState.NORMAL;
    }
    //endregion
}