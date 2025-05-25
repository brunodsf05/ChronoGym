package bdisfer1410.gymapp.exercise.models.routine.movement;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.util.java.Identifiable;

/**
 * Bundles an {@link List<ExerciseTransition>} with other data.
 */
public class ExerciseTransitions extends Identifiable implements Serializable, ExerciseCard {
    private final int DEFAULT_ICON = R.drawable.ic_exercise_default;

    public String name;
    public List<ExerciseTransition> list;

    public ExerciseTransitions(String name, List<ExerciseTransition> list) {
        this.name = name;
        this.list = list;
    }

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return (list != null && !list.isEmpty())
                ? list.get(0).getPose().getIcon()
                : DEFAULT_ICON;
    }

    @NonNull
    @Override
    public String getCardName() {
        return this.name;
    }

    @Nullable
    @Override
    public String getCardTags() {
        return getPrettierId();
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return "";
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return "";
    }
    //endregion
}
