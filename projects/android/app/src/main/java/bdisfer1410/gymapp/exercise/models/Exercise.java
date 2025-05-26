package bdisfer1410.gymapp.exercise.models;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExerciseTransitions;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.java.StringUtils;

public class Exercise implements ExerciseCard, Serializable {
    private String name;
    private Integer icon;
    private final TimerAnimationQueue queue;
    private List<String> tags;
    //region Repositories
    public List<ExercisePose> repoPoses = new ArrayList<>();
    public List<ExerciseTransitions> repoTransitions = new ArrayList<>();
    public List<TimerAnimation> repoSets = new ArrayList<>();
    public String iconPath = "";
    //endregion

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

    public void setName(String name) {
        this.name = name;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public TimerAnimationQueue getQueue() {
        return queue;
    }

    public List<String> getTags() {
        return tags;
    }

    /**
     * Repositories are list of objects that are unique, but later referenced in other site multiple
     * times via pointer.
     */
    public void setRepositories(List<ExercisePose> repoPoses, List<ExerciseTransitions> repoTransitions , List<TimerAnimation> repoSets) {
        this.repoPoses = repoPoses;
        this.repoTransitions = repoTransitions;
        this.repoSets = repoSets;
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
