package bdisfer1410.gymapp.exercise.timer.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import java.util.Iterator;
import java.util.List;

import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;

/**
 * An object that plays a {@code List<TimerAnimation>} on a {@link TimerFragment} object.
 * It triggers events that are listenable with the {@link TimerAnimationPlayerListener}.
 */
public class TimerAnimationPlayer {
    //region Variables
    //region Configuration
    private static final String LOG_TAG_BASE = TimerAnimationPlayer.class.getSimpleName()+".";
    private static final String LOG_TAG_LOOP_TICK = LOG_TAG_BASE+"tick";
    private static final String LOG_TAG_LOOP_UPDATE = LOG_TAG_BASE+"update";
    private static final String LOG_TAG_QUEUE = LOG_TAG_BASE+"queue";
    //endregion
    //region State
    private final TimerFragment timer;
    private Iterator<TimerAnimation> animationQueue;
    private TimerAnimation currentAnimation;
    private TimerAnimationPlayerListener listener;
    private boolean hasUpdateLoopFinished = false;
    private boolean hasTickLoopFinished = false;
    //endregion
    //endregion

    public TimerAnimationPlayer(TimerFragment timer) {
        this.timer = timer;
    }

    //region Player
    public void start(Iterable<TimerAnimation> animationQueue) {
        this.animationQueue = animationQueue.iterator();
        play();
    }

    private void play() {
        // Decide if we play the next animation or execute the finalization
        boolean animationQueueIsEmpty = !animationQueue.hasNext();

        if (animationQueueIsEmpty) {
            Log.d(LOG_TAG_QUEUE, "There are no more animations left to play.");
            listener.onQueueEnd();
            return;
        }

        // Initialization
        Log.d(LOG_TAG_QUEUE, "Starting a new animation...");
        currentAnimation = animationQueue.next();
        int duration = currentAnimation.onStart(timer);
        listener.onAnimationStart(currentAnimation);
        hasUpdateLoopFinished = false;
        hasTickLoopFinished = false;

        // Create update loop
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, duration);
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(state -> currentAnimation.onUpdate(timer, state));
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                Log.d(LOG_TAG_LOOP_UPDATE, "The update loop has finished.");
                hasUpdateLoopFinished = true;
                tryToStartNextAnimation();
            }
        });
        valueAnimator.start();

        // Create tick loop
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                int delayMs = currentAnimation.onTick(timer);

                if (delayMs > 0) {
                    handler.postDelayed(this, delayMs);
                    Log.d(LOG_TAG_LOOP_TICK, String.format("animation.onTick() was called! It will be called again in %dms...", delayMs));
                }
                else {
                    Log.d(LOG_TAG_LOOP_TICK, "animation.onTick() was called! There will be no more calls.");
                    hasTickLoopFinished = true;
                    tryToStartNextAnimation();
                }
            }
        };
        handler.post(runnable);
    }

    private void tryToStartNextAnimation() {
        boolean canStartNextAnimation = hasUpdateLoopFinished && hasTickLoopFinished;

        if (canStartNextAnimation) {
            currentAnimation.onEnd(timer);
            listener.onAnimationEnd(currentAnimation);
            play();
        }
    }
    //endregion

    //region Setters
    public void setListener(TimerAnimationPlayerListener listener) {
        this.listener = listener;
    }
    //endregion
}
