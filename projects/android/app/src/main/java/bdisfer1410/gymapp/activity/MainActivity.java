package bdisfer1410.gymapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.mock.ExerciseMock;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;

public class MainActivity extends AppCompatActivity {
    //region Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        findViewById(R.id.button1).setOnClickListener(v -> startExerciseActivity(ExerciseMock.CALISTHENICS));
        findViewById(R.id.button2).setOnClickListener(v -> startExerciseActivity(ExerciseMock.TIMERS));
    }
    //endregion

    private void startExerciseActivity(TimerAnimationQueue animationQueue) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("queue", animationQueue);
        startActivity(intent);
    }
}
