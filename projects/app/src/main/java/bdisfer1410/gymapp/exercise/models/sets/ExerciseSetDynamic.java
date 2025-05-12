package bdisfer1410.gymapp.exercise.models.sets;

import android.animation.ValueAnimator;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.movement.ExerciseTransition;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;

public class ExerciseSetDynamic extends ExerciseSet {
    private final List<ExerciseTransition> transitions;
    private final int reps;
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
        int lastPoseMsDuration = transitions.get(this.numberOfPoses - 1).getMsToNext();
        this.msDuration = oneRepMsDuration * reps - lastPoseMsDuration;
    }

    //region TimerAnimation
    @Override
    public int onStart(@NonNull TimerFragment timer) {
        timer.setExerciseProgressMax(msDuration);
        timer.setExerciseNameText(name);
        timer.setExerciseCounterText("0");

        boolean doesFirstPoseHasNotIcon = numberOfPoses > 0 && transitions.get(0).getPose().getIcon() == null;

        if (doesFirstPoseHasNotIcon) {
            timer.setExerciseIconImage(R.drawable.ic_exercise_default);
        }

        poseIndex = 0;
        repsDone = 0;

        return msDuration;
    }

    @Override
    public void onEnd(@NonNull TimerFragment timer) {

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
        timer.setSetCounterText("");
        // Manage reps cycle
        if (++poseIndex >= numberOfPoses) {
            repsDone++;
            poseIndex = 0;
            timer.setExerciseCounterText(String.valueOf(repsDone));
        }

        return repsDone >= reps ? 0 : transition.getMsToNext();
    }

    @Override
    public int calculateDuration() {
        return msDuration;
    }
    //endregion
}