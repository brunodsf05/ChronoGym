package bdisfer1410.gymapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import bdisfer1410.gymapp.exercise.mock.ExerciseMock;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayer;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayerListener;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationQueue;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.Counter;
import bdisfer1410.gymapp.util.OnFragmentReadyListener;
import bdisfer1410.gymapp.util.Voice;

public class ExerciseActivity extends AppCompatActivity implements OnFragmentReadyListener {
    private TimerAnimationQueue animationQueue;
    private TimerFragment timerFragment;
    private TimerAnimationPlayer animationPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        if (savedInstanceState == null) {
            timerFragment = new TimerFragment();
            loadTimerFragment(timerFragment);
            animationQueue = ExerciseMock.CALISTHENICS;
            animationPlayer.setListener(new TimerAnimationPlayerListener() {
                @Override
                public void onAnimationStart(TimerAnimation animation) {
                    Log.d("TimerAnimationPlayerListener", "onAnimationStart() was called!");

                    Voice.get().say(timerFragment.getExerciseNameText());

                    Counter animationCounter = animationQueue.counter.get(animation);
                    if (animationCounter == null) return;

                    animationCounter.value += 1;
                    timerFragment.setSetCounterText(
                            String.valueOf(animationQueue.counter.get(animation))
                    );
                }

                @Override
                public void onAnimationEnd(TimerAnimation animation) {
                    Log.d("TimerAnimationPlayerListener", "onAnimationEnd() was called!");
                }

                @Override
                public void onQueueEnd() {
                    Log.d("TimerAnimationPlayerListener", "onQueueEnd() was called!");
                }
            });
        }
    }

    private void loadTimerFragment(TimerFragment timerFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, timerFragment);
        transaction.commit();

        animationPlayer = new TimerAnimationPlayer(timerFragment);
    }

    @Override
    public void onFragmentReady() {
        Log.d("ExerciseActivity", "TimerFragment is ready to be used");
        Log.d("ExerciseActivity", "Starting a TimerAnimationQueue");
        Log.d("TimerAnimationQueue", String.format(
                "TimerAnimationQueue lasts %dms", animationQueue.calculateTotalDuration()
        ));

        animationQueue.counter.forEach(
                (timerAnimation, counter) -> Log.d("TimerAnimationQueue", String.format(
                        "TimerAnimationQueue has: %s - %s", counter, timerAnimation
                ))
        );

        animationPlayer.start(animationQueue);
    }
}
