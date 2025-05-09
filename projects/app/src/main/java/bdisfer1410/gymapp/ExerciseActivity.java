package bdisfer1410.gymapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;

public class ExerciseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        if (savedInstanceState == null) {
            loadTimerFragment(new TimerFragment());
        }
    }

    private void loadTimerFragment(TimerFragment timerFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, timerFragment);
        transaction.commit();
    }
}
