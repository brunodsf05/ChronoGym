package bdisfer1410.gymapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.mock.ExerciseMock;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayer;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayerListener;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationPlayerState;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.Counter;
import bdisfer1410.gymapp.util.Voice;

public class ExerciseActivity extends AppCompatActivity {
    //region Timer
    private TimerFragment timer;
    private TimerAnimationPlayer player;
    private TimerAnimationPlayerState state;
    //endregion
    //region UI
    private Button buttonToggleReproduction;
    //endregion
    //region State
    private boolean isPlaying = true;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // State
        state = new ViewModelProvider(this).get(TimerAnimationPlayerState.class);

        if (savedInstanceState == null) {
            state.animationQueue = ExerciseMock.CALISTHENICS;
        }

        // UI
        buttonToggleReproduction = findViewById(R.id.buttonToggleReproduction);
        buttonToggleReproduction.setOnClickListener(v -> {
            if (isPlaying) {
                player.stop();
            }
            else {
                player.play();
                buttonToggleReproduction.setEnabled(false); // TODO: Fix pausing logic & math
            }

            isPlaying = !isPlaying;
        });

        // Timer
        state.initialize();
        initializeTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (player == null) {
            Log.e("ExerciseActivity", "onStart() didn't receive nonNull player, exiting...");
            return;
        }

        Log.d("ExerciseActivity", "TimerFragment is ready to be used");
        Log.d("ExerciseActivity", "Starting a TimerAnimationQueue");
        Log.d("TimerAnimationQueue", String.format(
                "TimerAnimationQueue lasts %dms", state.animationQueue.calculateTotalDuration()
        ));

        state.animationQueue.counter.forEach(
                (timerAnimation, counter) -> Log.d("TimerAnimationQueue", String.format(
                        "TimerAnimationQueue has: %s - %s", counter, timerAnimation
                ))
        );

        player.play();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (player != null)
            player.stop();
    }

    private void initializeTimer() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();

        // Add timer fragment
        timer = new TimerFragment();

        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, timer)
                .commitNow();

        // Cancel previous player processes
        if (player != null)
            player.stop();

        // Link animation player to timer
        player = new TimerAnimationPlayer(timer, state);

        player.setListener(new TimerAnimationPlayerListener() {
            @Override
            public void onAnimationStart(TimerAnimation animation) {
                Log.d("TimerAnimationPlayerListener", "onAnimationStart() was called!");

                Voice.get().say(timer.getExerciseNameText());

                Counter animationCounter = state.animationQueue.counter.get(animation);
                if (animationCounter == null) return;

                animationCounter.value += 1;
                timer.setSetCounterText(
                        String.valueOf(state.animationQueue.counter.get(animation))
                );
            }

            @Override
            public void onAnimationEnd(TimerAnimation animation) {
                Log.d("TimerAnimationPlayerListener", "onAnimationEnd() was called!");
                buttonToggleReproduction.setEnabled(true); // TODO: Fix pausing logic & math
            }

            @Override
            public void onQueueEnd() {
                Log.d("TimerAnimationPlayerListener", "onQueueEnd() was called!");
            }
        });
    }
}
