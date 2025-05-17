package bdisfer1410.gymapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.mock.ExerciseMock;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayer;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayerListener;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.Counter;
import bdisfer1410.gymapp.util.Voice;

public class ExerciseActivity extends AppCompatActivity {
    private TimerAnimationQueue queue;
    private TimerFragment timer;
    private TimerAnimationPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



        queue = ExerciseMock.CALISTHENICS;

        if (savedInstanceState == null) {
            initializeTimer();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (queue == null || player == null) {
            Log.e("TODO", "On closing, save current info (index, post delayed remaining time, etc...) and restore it");
            return;
        }

        Log.d("ExerciseActivity", "TimerFragment is ready to be used");
        Log.d("ExerciseActivity", "Starting a TimerAnimationQueue");
        Log.d("TimerAnimationQueue", String.format(
                "TimerAnimationQueue lasts %dms", queue.calculateTotalDuration()
        ));

        queue.counter.forEach(
                (timerAnimation, counter) -> Log.d("TimerAnimationQueue", String.format(
                        "TimerAnimationQueue has: %s - %s", counter, timerAnimation
                ))
        );

        player.start(queue);
    }

    private void initializeTimer() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        // Add timer fragment
        timer = new TimerFragment();

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, timer)
                .commitNow();

        // Link animation player to timer
        player = new TimerAnimationPlayer(timer);

        player.setListener(new TimerAnimationPlayerListener() {
            @Override
            public void onAnimationStart(TimerAnimation animation) {
                Log.d("TimerAnimationPlayerListener", "onAnimationStart() was called!");

                Voice.get().say(timer.getExerciseNameText());

                Counter animationCounter = queue.counter.get(animation);
                if (animationCounter == null) return;

                animationCounter.value += 1;
                timer.setSetCounterText(
                        String.valueOf(queue.counter.get(animation))
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
