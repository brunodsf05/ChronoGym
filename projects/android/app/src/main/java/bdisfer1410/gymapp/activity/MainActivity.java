package bdisfer1410.gymapp.activity;

import android.content.Intent;
import android.graphics.Insets;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.FabMenuBuilder;

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
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_explore), R.drawable.ic_explore, v ->
                        Toast.makeText(this, "WIP: Explorar rutinas", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_import), R.drawable.ic_import, v ->
                        Toast.makeText(this, "WIP: Importar rutina", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_create), R.drawable.ic_add, v ->
                        Toast.makeText(this, "WIP: Crear rutina", Toast.LENGTH_SHORT).show()
                )
        );

        FabMenuBuilder.addFabButtons(this, fabLayout, fabMain, fabActions);

        fabMain.setOnClickListener(view -> {
            boolean isOpen = fabActions.get(0).generatedButton.getVisibility() == View.VISIBLE;
            for (FabMenuBuilder.FabAction action : fabActions) {
                action.generatedButton.setVisibility(isOpen ? View.GONE : View.VISIBLE);
                action.generatedButton.setOnClickListener(action.onClickListener);
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
