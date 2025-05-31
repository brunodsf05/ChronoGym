package bdisfer1410.gymapp.activity;

import android.content.Intent;
import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.android.FabMenuBuilder;
import bdisfer1410.gymapp.util.data.QuickFileManager;
import bdisfer1410.gymapp.util.java.ListTools;

public class MainActivity extends AppCompatActivity {
    private List<ExerciseCard> cardList;
    private ExerciseCardAdapter adapter;
    private boolean canStartExercise = false;


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

        // Load exercises from file
        cardList = new ArrayList<>();

        String jsonString = QuickFileManager
                .with(MainActivity.this)
                .file("user_exercises.json")
                .read();

        if (jsonString != null) {
            ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(MainActivity.this, jsonString);
            Result<List<Exercise>, Integer> result = exerciseSerdeJSON.deserialize();
            Log.d("ExerciseSerdeJSON", result.isOk() ? result.toString() : getString(result.getError()));

            cardList = ListTools.cast(result.getValue(), ExerciseCard.class);
            //region TEMP
            Result<String, Integer> tmpResult = exerciseSerdeJSON.serialize(result.getValue());
            new MaterialAlertDialogBuilder(MainActivity.this)
                    .setMessage(tmpResult.getValue())
                    .show();
            //endregion
        }

        // Init views
        initExercisesListRecyclerView(cardList);
        initFabMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        canStartExercise = true;
    }

    private void initExercisesListRecyclerView(List<ExerciseCard> cards) {
        RecyclerView exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseCardAdapter(cards, this::onCardClick, this::onCardLongPress);
        exercisesList.setAdapter(adapter);
    }

    private void onCardClick(ExerciseCard card) {
        if (!canStartExercise) {
            Log.d("MainActivity", "Can't start exercise because \"canStartExercise\" is false...");
            return;
        }

        Log.d("MainActivity", "Clicked on card... Trying to play it!");
        Exercise exercise = null;
        TimerAnimationQueue queue = null;

        if (card instanceof Exercise) {
            exercise = (Exercise) card;
            Log.d("MainActivity", "Card is a valid Exercise object!");
        }

        if (exercise != null) {
            queue = exercise.getQueue();
        }

        if (queue == null) {
            Log.e("MainActivity", "Exercise does not have valid TimerAnimationQueue to play :(");
            Toast.makeText(this, R.string.activity_main_error_cant_play_queue, Toast.LENGTH_SHORT).show();
        }
        else {
            canStartExercise = false;
            startExerciseActivity(queue);
        }
    }

    private void onCardLongPress(View anchor, int position) {
        Exercise exercise;

        try {
            exercise = (Exercise) cardList.get(position);
        }
        catch (Exception e) {
            Log.e("MainActivity", "Couldn't identify card by long pressing");
            return;
        }

        String[] options = {
                getString(R.string.activity_main_menu_edit),
                getString(R.string.activity_main_menu_export),
                getString(R.string.activity_main_menu_delete)
        };

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.activity_main_longpress_hint)
                .setItems(options, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            startEditorActivity(exercise);
                            break;
                        case 1:
                            Toast.makeText(this, "TODO: Export exercise", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            adapter.removeItem(position);

                            Toast.makeText(this, "TODO: Delete exercise", Toast.LENGTH_SHORT).show();
                            break;
                    }
                })
                .show();
    }

    private void initFabMenu() {
        ConstraintLayout fabLayout = findViewById(R.id.fabLayout);
        Button fabMain = findViewById(R.id.fab_main);

        List<FabMenuBuilder.FabAction> fabActions = List.of(
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_explore), R.drawable.ic_ui_explore, v ->
                        Toast.makeText(this, "WIP: Explorar rutinas", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_import), R.drawable.ic_ui_import, v -> {
                    Toast.makeText(this, "TEST: Importar rutina", Toast.LENGTH_SHORT).show();
                    String rawJsonString = QuickFileManager
                            .with(MainActivity.this)
                            .rawRes(R.raw.serialized_exercise_prototype)
                            .read();

                    QuickFileManager
                            .with(MainActivity.this)
                            .file("user_exercises.json")
                            .save(rawJsonString);
                }),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_create), R.drawable.ic_ui_add, v ->
                        startEditorActivity(null)
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

    private void startExerciseActivity(TimerAnimationQueue animationQueue) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("queue", animationQueue);
        startActivity(intent);
    }

    private void startEditorActivity(Exercise exercise) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("exercise", exercise);
        startActivity(intent);
    }
}
