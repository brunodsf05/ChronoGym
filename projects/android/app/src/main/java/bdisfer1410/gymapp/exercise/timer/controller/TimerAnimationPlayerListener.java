package bdisfer1410.gymapp.exercise.timer.controller;

/**
 * Listener interface for receiving events from the TimerAnimationPlayer.
 * It triggers when a TimerAnimation starts, ends, or when the entire animation queue is completed.
 */
public interface TimerAnimationPlayerListener {
    void onAnimationStart(TimerAnimation animation);
    void onAnimationEnd(TimerAnimation animation);
    void onQueueEnd();
}
