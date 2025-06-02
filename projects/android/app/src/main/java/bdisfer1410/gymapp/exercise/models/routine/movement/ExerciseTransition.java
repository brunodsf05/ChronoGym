package bdisfer1410.gymapp.exercise.models.routine.movement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.StringUtils;

/**
 * Bundles an {@link ExercisePose} with the time to start the next {@link ExerciseTransition}.
 * This time is in milliseconds.
 */
public class ExerciseTransition extends Identifiable implements Serializable, ExerciseCard {
    private final ExercisePose pose;
    private final int msToNext;

    public ExerciseTransition(ExercisePose pose, int msToNext) {
        this.pose = pose;
        this.msToNext = msToNext;
    }

    public ExercisePose getPose() {
        return pose;
    }

    public int getMsToNext() {
        return msToNext;
    }

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return pose.getCardIcon();
    }

    @NonNull
    @Override
    public String getCardName() {
        return pose.getCardName();
    }

    @Nullable
    @Override
    public String getCardTags() {
        return pose.getPrettierId();
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return StringUtils.formatMsIntoSeconds(msToNext);
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
