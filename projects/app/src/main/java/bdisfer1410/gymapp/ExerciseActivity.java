package bdisfer1410.gymapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import bdisfer1410.gymapp.exercise.timer.view.TimerFragment;
import bdisfer1410.gymapp.util.OnFragmentReadyListener;

public class ExerciseActivity extends AppCompatActivity implements OnFragmentReadyListener {

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

    @Override
    public void onFragmentReady() {
        Log.d("ExerciseActivity", "TimerFragment is ready to be used");
    }
}
