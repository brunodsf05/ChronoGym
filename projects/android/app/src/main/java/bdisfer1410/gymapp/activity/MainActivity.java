package bdisfer1410.gymapp.activity;

import android.content.Intent;
import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.android.FabMenuBuilder;
import bdisfer1410.gymapp.util.Result;

public class MainActivity extends AppCompatActivity {
    //region Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // findViewById(R.id.button1).setOnClickListener(v -> startExerciseActivity(ExerciseMock.CALISTHENICS));
        // findViewById(R.id.button2).setOnClickListener(v -> startExerciseActivity(ExerciseMock.TIMERS));

        ConstraintLayout fabLayout = findViewById(R.id.fabLayout);
        FloatingActionButton fabMain = findViewById(R.id.fab_main);

        List<FabMenuBuilder.FabAction> fabActions = List.of(
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_explore), R.drawable.ic_ui_explore, v ->
                        Toast.makeText(this, "WIP: Explorar rutinas", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_import), R.drawable.ic_ui_import, v -> {
                    Toast.makeText(this, "TEST: Importar rutina", Toast.LENGTH_SHORT).show();
                    // Read file
                    InputStream inputStream = getResources().openRawResource(R.raw.serialized_exercise_prototype);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while (true) {
                        try { if ((line = reader.readLine()) == null) break; }
                        catch (IOException e) {throw new RuntimeException(e); }
                        stringBuilder.append(line);
                    }
                    String jsonString = stringBuilder.toString();
                    // Deserialize
                    ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(
                            MainActivity.this, jsonString
                    );
                    // Output
                    Result<List<Exercise>, Integer> result = exerciseSerdeJSON.deserialize();
                    Log.d("ExerciseSerdeJSON", result.isOk() ? result.toString() : getString(result.getError()));
                    if (result.isOk() && !result.getValue().isEmpty()) {
                        startExerciseActivity(result.getValue().get(0).getQueue());
                    }
                }),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_create), R.drawable.ic_ui_add, v ->
                        Toast.makeText(this, "WIP: Crear rutina", Toast.LENGTH_SHORT).show()
                )
        );

        FabMenuBuilder.addFabButtons(this, fabLayout, fabMain, fabActions);

        fabMain.setOnClickListener(view -> {
            boolean isOpen = fabActions.get(0).generatedButton.getVisibility() == View.VISIBLE;

            fabMain.animate().rotation(isOpen ? 0f : 45f).setDuration(200).start();
            for (FabMenuBuilder.FabAction action : fabActions) {
                if (!isOpen) action.generatedButton.setVisibility(View.VISIBLE);
                action.generatedButton.setOnClickListener(action.onClickListener);
                action.generatedButton.setAlpha(isOpen ? 1f : 0f);
                action.generatedButton.animate().alpha(isOpen ? 0f : 1f).setDuration(200).withEndAction(
                        () -> {
                            if (isOpen) action.generatedButton.setVisibility(View.GONE);
                        }
                ).start();
            }
        });
    }
    //endregion

    private void startExerciseActivity(TimerAnimationQueue animationQueue) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("queue", animationQueue);
        startActivity(intent);
    }
}
