package bdisfer1410.gymapp.exercise.models.routine.movement;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.StringUtils;

/**
 * Stores information about an static pose that's done during any exercise.
 */
public class ExercisePose extends Identifiable implements Serializable, ExerciseCard {
    private String name;
    private Integer icon;

    public ExercisePose(String name, Integer icon) {
        this.name = name;

        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return getIcon();
    }

    @NonNull
    @Override
    public String getCardName() {
        return getName();
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

    @NonNull
    @Override
    public ExerciseCardState getCardState() {
        return ExerciseCardState.NORMAL;
    }
    //endregion
}
