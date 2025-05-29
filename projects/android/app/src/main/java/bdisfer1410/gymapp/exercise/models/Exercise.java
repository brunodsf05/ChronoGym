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
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.ListTools;
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

    //region Repositories
    public boolean updatePoseFromRepo(ExercisePose updatedPose) {
        int i = 0;
        boolean idsMatches = false;

        for (ExercisePose pose : repoPoses) {
            idsMatches = pose.getId().equals(updatedPose.getId());

            if (idsMatches)
                break;

            i++;
        }

        if (idsMatches) {
            repoPoses.get(i).setIcon(updatedPose.getIcon());
            repoPoses.get(i).setName(updatedPose.getName());
        }

        return false;
    }

    public List<String> getRepoPosesIds() {
        return Identifiable.getIds(ListTools.cast(repoPoses, Identifiable.class));
    }

    public boolean updateTransitionFromRepo(ExerciseTransitions updatedTransitionList) {
        int i = 0;
        boolean idsMatches = false;

        for (ExerciseTransitions pose : repoTransitions) {
            idsMatches = pose.getId().equals(updatedTransitionList.getId());

            if (idsMatches)
                break;

            i++;
        }

        if (idsMatches) {
            ExerciseTransitions originalTransitionList = repoTransitions.get(i);
            originalTransitionList.name = updatedTransitionList.name;
        }

        return false;
    }

    public List<String> getRepoTransitionsIds() {
        return Identifiable.getIds(ListTools.cast(repoTransitions, Identifiable.class));
    }

    public List<String> getRepoSetsIds() {
        return Identifiable.getIds(ListTools.cast(repoSets, Identifiable.class));
    }

    public boolean removePose(ExercisePose pose) {
        return false;
    }


    public boolean removeTransitionList(ExerciseTransitions transitionList) {
        // Search transition
        int i = 0;
        boolean idsMatches = false;

        for (ExerciseTransitions pose : repoTransitions) {
            idsMatches = pose.getId().equals(transitionList.getId());

            if (idsMatches)
                break;

            i++;
        }

        if (!idsMatches) return false;

        // Search if some set uses this transition
        // TODO: IMPLEMENT WHEN NECESSARY

        // Remove
        repoTransitions.remove(i);
        return true;
    }
    //endregion

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
