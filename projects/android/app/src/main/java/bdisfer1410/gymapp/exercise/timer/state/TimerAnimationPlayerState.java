package bdisfer1410.gymapp.exercise.timer.state;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import java.util.Iterator;

import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;

/**
 * Stores the state of the {@link bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayer}
 * allowing persistence against the Android's lifecycle.
 */
public class TimerAnimationPlayerState extends ViewModel implements Iterator<TimerAnimation> {
    public boolean wasInitialized = false;
    public TimerAnimationQueue animationQueue;
    public TimerAnimation animationCurrent;
    public int animationIndex = 0;
    public boolean hasUpdateLoopFinished = false;
    public boolean hasTickLoopFinished = false;
    public boolean hasAnimationStarted = true;
    public int msDurationUpdateLoop = 0;
    public int msElapsedUpdateLoop = 0;
    public int msElapsedTickLoop = 0;

    public TimerAnimationPlayerState() {
        Log.d("TimerAnimationPlayerState", "Initialized");
    }

    /**
     * Restarts this state once during its lifecycle.
     */
    public void initialize() {
        if (!wasInitialized) {
            restart();
            wasInitialized = true;
        }
    }

    public void restart() {
        animationIndex = 0;
        hasUpdateLoopFinished = false;
        hasTickLoopFinished = false;
        hasAnimationStarted = true;
        msDurationUpdateLoop = 0;
        msElapsedUpdateLoop = 0;
        msElapsedTickLoop = 0;

        loadAnimationCurrent();

        Log.d("TimerAnimationPlayerState", "Restarted");
    }

    public boolean hasFinishedAllLoops() {
        return hasUpdateLoopFinished && hasTickLoopFinished;
    }

    public void loadAnimationCurrent() {
        boolean isAnimationIndexValid = animationIndex >= 0 && animationIndex < animationQueue.list.size();

        animationCurrent = isAnimationIndexValid
                ? animationQueue.list.get(animationIndex)
                : null;
    }

    //region Iterator
    @Override
    public boolean hasNext() {
        return animationIndex + 1 < animationQueue.list.size();
    }

    @Override
    public TimerAnimation next() {
        if (!hasNext()) return null;

        animationIndex++;
        animationCurrent = animationQueue.list.get(animationIndex);
        return animationCurrent;
    }
    //endregion
}
