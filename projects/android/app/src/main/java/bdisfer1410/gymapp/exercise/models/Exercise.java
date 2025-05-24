package bdisfer1410.gymapp.exercise.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.java.StringUtils;

public class Exercise implements ExerciseCard {
    private final String name;
    private final Integer icon;
    private final TimerAnimationQueue queue;
    private final List<String> tags;

    public Exercise(String name, Integer icon, TimerAnimationQueue queue, List<String> tags) {
        this.name = name;
        this.icon = icon;
        this.queue = queue;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public Integer getIcon() {
        return icon;
    }

    public TimerAnimationQueue getQueue() {
        return queue;
    }

    public List<String> getTags() {
        return tags;
    }

    @NonNull
    @Override
    public String toString() {
        return "Exercise{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", tags=" + tags +
                ", queue=" + queue +
                '}';
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
        if (getTags() == null) return null;
        return String.join(", ", getTags());
    }

    @NonNull
    @SuppressLint("DefaultLocale")
    @Override
    public String getCardInterval() {
        return StringUtils.formatMsIntoTime(getQueue().calculateTotalDuration());
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return "";
    }
    //endregion
}
