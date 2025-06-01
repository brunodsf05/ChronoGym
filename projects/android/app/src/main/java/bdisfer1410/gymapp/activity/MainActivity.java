package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeHelper;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.android.FabMenuBuilder;
import bdisfer1410.gymapp.util.android.FileDialog;
import bdisfer1410.gymapp.util.data.QuickFileManager;
import bdisfer1410.gymapp.util.java.ListTools;
import bdisfer1410.gymapp.util.java.StringUtils;

public class MainActivity extends AppCompatActivity {
    private List<ExerciseCard> cardList;
    private List<Exercise> exerciseList;
    private ExerciseCardAdapter adapter;
    private boolean canStartExercise = false;

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

        // Prepare exercises
        cardList = new ArrayList<>();
        exerciseList = new ArrayList<>();

        // Init views
        initExercisesListRecyclerView(cardList);
        initFabMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadExercises();
        canStartExercise = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FileDialog.handleActivityResult(this, requestCode, resultCode, data);
    }
    //endregion

    //region UI initializers
    private void initExercisesListRecyclerView(List<ExerciseCard> cards) {
        RecyclerView exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseCardAdapter(cards, this::onCardClick, this::onCardLongPress);
        exercisesList.setAdapter(adapter);
    }

    private void initFabMenu() {
        ConstraintLayout fabLayout = findViewById(R.id.fabLayout);
        Button fabMain = findViewById(R.id.fab_main);

        List<FabMenuBuilder.FabAction> fabActions = List.of(
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_explore), R.drawable.ic_ui_explore, v ->
                        Toast.makeText(this, "WIP: Explorar rutinas", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_import), R.drawable.ic_ui_import, v -> importExercises()),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_create), R.drawable.ic_ui_add, v ->
                        startEditorActivity(null, -1)
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

    //region Card functions
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
        if (!canStartExercise) {
            Log.d("MainActivity", "Can't open card options because \"canStartExercise\" is false...");
            return;
        }

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
                            startEditorActivity(exercise, position);
                            break;
                        case 1:
                            exportExercises(List.of(exercise));
                            break;
                        case 2:
                            deleteExercise(position);
                            break;
                    }
                })
                .show();
    }
    //endregion

    //region Activity launcher
    private void startExerciseActivity(TimerAnimationQueue animationQueue) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("queue", animationQueue);
        startActivity(intent);
    }

    private void startEditorActivity(Exercise exercise, int indexToOverwrite) {
        Intent intent = new Intent(this, EditorActivity.class);
        intent.putExtra("exercise", exercise);
        intent.putExtra("indexToOverwrite", indexToOverwrite);
        startActivity(intent);
    }
    //endregion

    //region Exercise storage
    @SuppressLint("NotifyDataSetChanged")
    private void reloadExercises() {
        // Load JSON from file
        String jsonString = QuickFileManager
                .with(MainActivity.this)
                .file(ExerciseSerdeHelper.FILENAME)
                .read();

        if (jsonString == null) {
            jsonString = ExerciseSerdeHelper.JSON_EMPTY;
            boolean success = ExerciseSerdeHelper.restart(MainActivity.this);
            if (!success) {
                throw new RuntimeException("JSON couldn't be restarted");
            }
        }

        // Deserialize from JSON
        ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(MainActivity.this, jsonString);
        Result<List<Exercise>, Integer> result = exerciseSerdeJSON.deserialize();
        Log.d("ExerciseSerdeJSON", result.isOk() ? result.toString() : getString(result.getError()));

        if (result.isOk()) {
            exerciseList = result.getValue();
            cardList = ListTools.cast(exerciseList, ExerciseCard.class);
        }
        else {
            Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
        }

        // Update items if adapter exists
        if (adapter != null)
            adapter.setItems(cardList);
    }

    private void deleteExercise(int index) {
        adapter.removeItem(index);
        exerciseList.remove(index);

        Result<String, Integer> serializeResult = new ExerciseSerdeJSON(MainActivity.this, "")
                .serialize(exerciseList);

        if (serializeResult.isOk()) {
            QuickFileManager
                    .with(MainActivity.this)
                    .file(ExerciseSerdeHelper.FILENAME)
                    .save(serializeResult.getValue());
            Toast.makeText(this, R.string.activity_main_delete_success, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, serializeResult.getError(), Toast.LENGTH_SHORT).show();
        }
    }

    private void importExercises() {
        /*
        String rawJsonString = QuickFileManager
                .with(MainActivity.this)
                .rawRes(R.raw.serialized_exercise_prototype)
                .read();

        QuickFileManager
                .with(MainActivity.this)
                .file("user_exercises.json")
                .save(rawJsonString);
        */
        FileDialog.readFile(this, result -> {
            if (result.isOk()) {
                // Parse exercises from string
                Result<List<Exercise>, Integer> importedExercises = new ExerciseSerdeJSON(MainActivity.this, result.getValue()).deserialize();
                if (importedExercises.isErr()) {
                    Toast.makeText(this, importedExercises.getError(), Toast.LENGTH_SHORT).show();
                    return;
                }

                // Add it
                Result<Void, Integer> addResult = ExerciseSerdeHelper.addMultiple(MainActivity.this, importedExercises.getValue());

                if (addResult.isOk())
                    reloadExercises();
                else
                    Toast.makeText(this, addResult.getError(), Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void exportExercises(List<Exercise> exercises) {
        // Serialize
        Result<String, Integer> resultSerialization = new ExerciseSerdeJSON(this, "").serialize(exercises);

        if (resultSerialization.isErr()) {
            Toast.makeText(this, resultSerialization.getError(), Toast.LENGTH_SHORT).show();
            return;
        }

        String filename = StringUtils.generateFileName(
                exercises.size() == 1 ? exercises.get(0).getName() : "rutina",
                new Date(),
                "json"
        );

        // Save
        FileDialog.saveFile(this, filename, resultSerialization.getValue(), result -> {
            Toast.makeText(
                    this,
                    result.isOk()
                            ? R.string.utils_file_dialog_success_saving_file
                            : result.getError(),
                    Toast.LENGTH_SHORT
            ).show();
        });
    }
    //endregion
}
