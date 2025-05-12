package bdisfer1410.gymapp.exercise.timer.controller;

import androidx.annotation.NonNull;

import java.util.Iterator;
import java.util.List;

/**
 * Stores a list of {@link TimerAnimation} and also provides useful information such as the total
 * milliseconds.
 */
public class TimerAnimationQueue implements Iterable<TimerAnimation> {
    public List<TimerAnimation> animationQueue;

    public TimerAnimationQueue(List<TimerAnimation> animationList) {
        this.animationQueue = animationList;
    }

    /**
     * The sum of all {@link TimerAnimation} durations.
     * @return The time in milliseconds.
     */
    public int calculateTotalDuration() {
        return animationQueue.stream()
                .mapToInt(TimerAnimation::calculateDuration)
                .sum();
    }

    @NonNull
    @Override
    public Iterator<TimerAnimation> iterator() {
        return animationQueue.iterator();
    }
}
