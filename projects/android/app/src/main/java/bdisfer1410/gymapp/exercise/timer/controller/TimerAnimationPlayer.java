package bdisfer1410.gymapp.exercise.timer.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationPlayerState;
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
    private TimerAnimationPlayerListener listener;
    private final TimerAnimationPlayerState state;
    //endregion
    //region Loops
    private ValueAnimator loopUpdate;
    private Handler loopTick;
    private long loopTickStartTime = 0;
    //endregion
    //endregion

    public TimerAnimationPlayer(TimerFragment timer, TimerAnimationPlayerState state) {
        this.timer = timer;
        this.state = state;
    }

    //region LifeCycle
    public void stop() {
        if (loopUpdate != null && loopUpdate.isRunning()) {
            long msElapsed = loopUpdate.getCurrentPlayTime();
            state.msElapsedUpdateLoop += (int)msElapsed;
            loopUpdate.cancel();
            loopUpdate = null;

            Log.d(LOG_TAG_LOOP_UPDATE, String.format("Stopped. msElapsed = %d", msElapsed));
        }

        if (loopTick != null) {
            long loopTickElapsedTime = System.currentTimeMillis() - loopTickStartTime;
            state.msDurationTickLoop = (int)Math.max(0, state.msDurationTickLoop - loopTickElapsedTime);
            loopTick.removeCallbacksAndMessages(null);
            loopTick = null;
        }
    }
    //endregion

    //region Player
    public void play() {
        // Previous state
        if (state.hasAnimationStarted) {
            Log.d(LOG_TAG_QUEUE, "Starting a new animation...");
            state.msDurationUpdateLoop = state.animationCurrent.onStart(timer);
            state.msDurationTickLoop = 0;
            state.msElapsedUpdateLoop = 0;
            listener.onAnimationStart(state.animationCurrent);
            state.hasUpdateLoopFinished = false;
            state.hasTickLoopFinished = false;
            state.hasAnimationStarted = false;
        }
        else {
            Log.d(LOG_TAG_QUEUE, "Recovering previous animation...");
        }

        // Create update loop
        loopUpdate = ValueAnimator.ofInt(state.msElapsedUpdateLoop, state.msDurationUpdateLoop);
        loopUpdate.setDuration(state.msDurationUpdateLoop - state.msElapsedUpdateLoop);
        loopUpdate.setInterpolator(new LinearInterpolator());
        loopUpdate.addUpdateListener(animation -> state.animationCurrent.onUpdate(timer, animation));
        loopUpdate.addListener(new AnimatorListenerAdapter() {
            private boolean wasCanceled = false;

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                wasCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (wasCanceled) return;

                Log.d(LOG_TAG_LOOP_UPDATE, "The update loop has finished.");
                state.hasUpdateLoopFinished = true;
                tryToStartNextAnimation();
            }
        });
        loopUpdate.start();

        // Create tick loop
        loopTick = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                state.msDurationTickLoop = state.animationCurrent.onTick(timer);
                loopTickStartTime = System.currentTimeMillis();

                if (state.msDurationTickLoop > 0) {
                    loopTick.postDelayed(this, state.msDurationTickLoop);
                    Log.d(LOG_TAG_LOOP_TICK, String.format("animation.onTick() was called! It will be called again in %dms...", state.msDurationTickLoop));
                }
                else {
                    Log.d(LOG_TAG_LOOP_TICK, "animation.onTick() was called! There will be no more calls.");
                    state.hasTickLoopFinished = true;
                    tryToStartNextAnimation();
                }
            }
        };
        Log.d(LOG_TAG_LOOP_TICK, String.format("Time until starting again loop tick %d", state.msDurationTickLoop));
        loopTick.postDelayed(runnable, state.msDurationTickLoop);
    }

    private void tryToStartNextAnimation() {
        boolean canStartNextAnimation = state.hasFinishedAllLoops();

        if (canStartNextAnimation)
            playNext();
    }

    /**
     * @return If the animation is played or not.
     */
    public boolean playNext() {
        // Decide if we play the next animation or execute the finalization
        boolean animationQueueIsEmpty = !state.hasNext();

        if (animationQueueIsEmpty) {
            Log.d(LOG_TAG_QUEUE, "There are no more animations left to play.");
            listener.onQueueEnd();
            return false;
        }

        // Play next
        stop();
        state.hasAnimationStarted = true;
        state.animationCurrent.onEnd(timer);
        listener.onAnimationEnd(state.animationCurrent);
        state.next();
        play();
        return true;
    }

    /**
     * @return If the animation is played or not.
     */
    public boolean playPrev() {
        // Decide if we play the prev animation or avoid null pointer
        if (!state.hasPrev()) {
            Log.d(LOG_TAG_QUEUE, "There are no more animations left to play.");
            return false;
        }

        stop();
        state.hasAnimationStarted = true;
        listener.onAnimationEnd(state.animationCurrent);
        state.prev();
        play();
        return true;
    }
    //endregion

    //region Setters
    public void setListener(TimerAnimationPlayerListener listener) {
        this.listener = listener;
    }
    //endregion
}
