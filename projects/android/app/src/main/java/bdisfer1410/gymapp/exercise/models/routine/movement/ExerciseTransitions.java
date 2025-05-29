package bdisfer1410.gymapp.exercise.models.routine.movement;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.StringUtils;

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

    public String getName() {
        return name;
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
        return String.format("%s%s", getPrettierId(), list != null ? list.isEmpty() ? " (Pulse para añadir poses)" : "" : "");
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return StringUtils.formatMsIntoTime(
                list == null
                        ? 0
                        : list.stream().mapToInt(ExerciseTransition::getMsToNext).sum()
        );
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public String getCardExtra() {
        return String.format("%d poses", list == null ? 0 : list.size());
    }
    //endregion
}
