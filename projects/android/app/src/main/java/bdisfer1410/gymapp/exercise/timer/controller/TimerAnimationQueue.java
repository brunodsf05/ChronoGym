package bdisfer1410.gymapp.exercise.timer.controller;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import bdisfer1410.gymapp.util.Counter;

/**
 * Stores a list of {@link TimerAnimation} and also provides useful information such as the total
 * milliseconds.
 */
public class TimerAnimationQueue implements Iterable<TimerAnimation> {
    public List<TimerAnimation> queue;
    /**
     * Links any reference of {@link TimerAnimation} once to an integer.
     * Because this is a {@link HashMap}, the repeated {@link TimerAnimation} in the
     * {@link TimerAnimationQueue#queue} will be merged into the same counter.
     */
    public HashMap<TimerAnimation, Counter> counter;

    public TimerAnimationQueue(List<TimerAnimation> animationList) {
        this.queue = animationList;
        initializeAnimationCounter();
    }

    //region: Math
    /**
     * The sum of all {@link TimerAnimation} durations.
     * @return The time in milliseconds.
     */
    public int calculateTotalDuration() {
        return queue.stream()
                .mapToInt(TimerAnimation::calculateDuration)
                .sum();
    }
    //endregion

    //region: Counter
    /**
     * Restarts the {@link TimerAnimationQueue#counter}, setting every value to zero.
     */
    public void initializeAnimationCounter() {
        counter = new HashMap<>();

        queue.forEach(
                timerAnimation -> {
                    Counter counter = this.counter.getOrDefault(timerAnimation, new Counter(0));
                    if (counter == null) return;
                    counter.max += 1;
                    this.counter.put(timerAnimation, counter);
                }
        );
    }
    //endregion

    @NonNull
    @Override
    public Iterator<TimerAnimation> iterator() {
        return queue.iterator();
    }
}
