package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimation;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayer;
import bdisfer1410.gymapp.exercise.timer.controller.TimerAnimationPlayerListener;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationPlayerState;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.Counter;
import bdisfer1410.gymapp.util.media.Voice;

public class ExerciseActivity extends AppCompatActivity {
    //region Timer
    private TimerFragment timer;
    private TimerAnimationPlayer player;
    private TimerAnimationPlayerState state;
    //endregion
    //region UI
    private MaterialButton buttonToggleReproduction, buttonBackwardReproduction, buttonForwardReproduction, buttonReturn;
    //endregion
    //region State
    private boolean isPlaying = true;
    //endregion

    //region Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // State
        state = new ViewModelProvider(this).get(TimerAnimationPlayerState.class);

        if (savedInstanceState == null) {
            Object obj = getIntent().getSerializableExtra("queue");

            if (!(obj instanceof TimerAnimationQueue)){
                Log.w("ExerciseActivity", "Didn't receive valid TimerAnimationQueue from Intent");
                Toast.makeText(this, R.string.activity_exercise_error_invalid_intent, Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            state.animationQueue = (TimerAnimationQueue) obj;
        }

        initializeGUI();

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
    //endregion

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
                displayCounter(state.animationQueue.counter.get(animation));
            }

            @Override
            public void onAnimationEnd(TimerAnimation animation) {
                Log.d("TimerAnimationPlayerListener", "onAnimationEnd() was called!");
                updatePauseButtonStyle();
                buttonToggleReproduction.setEnabled(true); // TODO: Fix pausing logic & math
            }

            @Override
            public void onQueueEnd() {
                Log.d("TimerAnimationPlayerListener", "onQueueEnd() was called!");
                player.stop();
                showFinalDialog();
            }
        });
    }

    private void initializeGUI() {
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
            updatePauseButtonStyle();
        });

        buttonBackwardReproduction = findViewById(R.id.buttonBackwardReproduction);
        buttonBackwardReproduction.setEnabled(false);
        buttonBackwardReproduction.setOnClickListener(v -> {
            // Unpause
            isPlaying = true;
            updatePauseButtonStyle();
            buttonToggleReproduction.setEnabled(true);

            // Skip to previous animation
            TimerAnimation oldAnimation = state.animationCurrent;
            boolean hasSkippedToPrev = player.playPrev();
            if (!hasSkippedToPrev) return;

            // Get the new animation to play
            TimerAnimation newAnimation = state.animationCurrent;

            // Decrement counters for consistency
            Counter oldAnimationCounter = state.animationQueue.counter.get(oldAnimation);
            if (oldAnimationCounter != null) oldAnimationCounter.value--;

            Counter newAnimationCounter = state.animationQueue.counter.get(newAnimation);
            if (newAnimationCounter == null) return;
            newAnimationCounter.value--;
            displayCounter(newAnimationCounter);

            // Toggle usability of skip to prev button for better L~O~O~K~S
            buttonBackwardReproduction.setEnabled(state.hasPrev());
        });

        buttonForwardReproduction = findViewById(R.id.buttonForwardReproduction);
        buttonForwardReproduction.setOnClickListener(v -> {
            // Unpause
            isPlaying = true;
            buttonToggleReproduction.setEnabled(true);

            // Play next
            player.playNext();
            buttonBackwardReproduction.setEnabled(true);
        });

        buttonReturn = findViewById(R.id.buttonReturn);
        buttonReturn.setOnClickListener(v -> finish());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // TODO: Fix pausing logic & math
                if (buttonToggleReproduction.isEnabled()) {
                    player.stop();
                    isPlaying = false;
                }

                new MaterialAlertDialogBuilder(ExerciseActivity.this)
                        .setTitle(R.string.activity_exercise_prompt_title_exit)
                        .setMessage(R.string.activity_exercise_prompt_message_exit)
                        .setPositiveButton(R.string.activity_exercise_prompt_action_exit_accept, (dialog, which) -> finish())
                        .setNegativeButton(R.string.activity_exercise_prompt_action_exit_deny, (dialog, which) -> {
                            // TODO: Fix pausing logic & math
                            if (buttonToggleReproduction.isEnabled()) {
                                player.play();
                                isPlaying = true;
                                buttonToggleReproduction.setEnabled(false);
                            }
                        })
                        .show();
            }
        });

    }

    private void showFinalDialog() {
        TextView textTitleFinished = findViewById(R.id.textTitleFinished);
        Voice.get().say(textTitleFinished.getText().toString());

        View timerFragmentContainer = findViewById(R.id.fragmentContainer);
        List<View> invisibleText = List.of(textTitleFinished, findViewById(R.id.textMessageFinished));

        Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        Animation fadeIn1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation fadeIn2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                List.of(
                        buttonBackwardReproduction,
                        buttonToggleReproduction,
                        buttonForwardReproduction
                ).forEach(
                        button -> button.setVisibility(View.GONE)
                );
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                timerFragmentContainer.setVisibility(View.GONE);
                invisibleText.forEach(view -> view.setVisibility(View.VISIBLE));
                invisibleText.forEach(view -> view.startAnimation(fadeIn1));
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });


        fadeIn1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                buttonToggleReproduction.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                buttonReturn.setVisibility(View.VISIBLE);
                buttonReturn.startAnimation(fadeIn2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        timerFragmentContainer.startAnimation(fadeOut);
    }

    //region UI
    private void displayCounter(Counter counter) {
        timer.setSetCounterText(String.valueOf(counter));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updatePauseButtonStyle() {
        buttonToggleReproduction.setIcon(
                getDrawable(isPlaying ? R.drawable.ic_ui_pause : R.drawable.ic_ui_play)
        );
    }
    //endregion
}
