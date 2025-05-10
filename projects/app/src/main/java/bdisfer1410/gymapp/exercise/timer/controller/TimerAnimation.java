package bdisfer1410.gymapp.exercise.timer.controller;

import android.animation.ValueAnimator;

import androidx.annotation.NonNull;

import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;

/**
 * Defines how an object modifies an {@link TimerFragment} during an animation.
 *
 * This modifications are done constantly with {@link #onUpdate(TimerFragment, ValueAnimator)}
 * or/and at discrete intervals with {@link #onTick(TimerFragment)}.
 */
public interface TimerAnimation {
    /**
     * Executed at the start of the animation for initializing purposes.
     *
     * @param timer The fragment to modify it's views values.
     * @return The total duration of the {@link #onUpdate(TimerFragment, ValueAnimator)} animation in milliseconds.
     */
    int onStart(@NonNull TimerFragment timer);

    /**
     * Executed when both, {@link #onUpdate(TimerFragment, ValueAnimator)} and {@link #onTick(TimerFragment)},
     * have finished.
     *
     * @param timer The fragment to modify it's views values.
     */
    void onEnd(@NonNull TimerFragment timer);

    /**
     * Called constantly during the milliseconds return by {@link #onStart(TimerFragment)}.
     * Think of it like an animation which interpolates a value over time.
     *
     * @param timer The fragment to modify it's views values.
     * @param animation The value interpolated over time.
     */
    void onUpdate(@NonNull TimerFragment timer, ValueAnimator animation);

    /**
     * Called after intervals of time. It can be infinite unless you return zero or less.
     *
     * @param timer The fragment to modify it's views values.
     * @return The time in milliseconds that must have to pass to execute it again,
     *         if it's zero or less then it's finished.
     */
    int onTick(@NonNull TimerFragment timer);
}
