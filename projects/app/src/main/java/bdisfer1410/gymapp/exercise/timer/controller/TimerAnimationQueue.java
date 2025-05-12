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

    @NonNull
    @Override
    public Iterator<TimerAnimation> iterator() {
        return animationQueue.iterator();
    }
}
