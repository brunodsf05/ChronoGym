package bdisfer1410.gymapp.exercise.models;

import java.util.List;

import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;

public class Exercise {
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
}
