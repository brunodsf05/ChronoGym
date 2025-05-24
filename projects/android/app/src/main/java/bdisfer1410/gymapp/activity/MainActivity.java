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
    private ExerciseCardAdapter adapter;


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
        List<ExerciseCard> cardList = new ArrayList<>();

        String jsonString = QuickFileManager
                .with(MainActivity.this)
                .file("user_exercises.json")
                .read();

        if (jsonString != null) {
            ExerciseSerdeJSON exerciseSerdeJSON = new ExerciseSerdeJSON(MainActivity.this, jsonString);
            Result<List<Exercise>, Integer> result = exerciseSerdeJSON.deserialize();
            Log.d("ExerciseSerdeJSON", result.isOk() ? result.toString() : getString(result.getError()));

            cardList = ListTools.cast(result.getValue(), ExerciseCard.class);
        }

        // Init views
        initExercisesListRecyclerView(cardList);
        initFabMenu();
    }

    private void initExercisesListRecyclerView(List<ExerciseCard> cards) {
        RecyclerView exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseCardAdapter(cards, this::onCardClick, this::onCardLongPress);
        exercisesList.setAdapter(adapter);

        /*
        adapter = new ExerciseCardAdapter(cards, this::onCardClick, null);
        exercisesList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                try {
                    adapter.wait(from, to);
                }
                catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not used
            }
        });

        helper.attachToRecyclerView(exercisesList);
        */
    }

    private void onCardClick(ExerciseCard card) {
        Log.d("ActivityMain", "Clicked on card... Trying to play it!");
        Exercise exercise = null;
        TimerAnimationQueue queue = null;

        if (card instanceof Exercise) {
            exercise = (Exercise) card;
            Log.d("ActivityMain", "Card is a valid Exercise object!");
        }

        if (exercise != null) {
            queue = exercise.getQueue();
        }

        if (queue == null) {
            Log.e("ActivityMain", "Exercise does not have valid TimerAnimationQueue to play :(");
            Toast.makeText(this, R.string.activity_main_error_cant_play_queue, Toast.LENGTH_SHORT).show();
        }
        else {
            startExerciseActivity(queue);
        }
    }

    private void onCardLongPress(View anchor, int position) {
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
                            Toast.makeText(this, "TODO: Edit exercise", Toast.LENGTH_SHORT).show();
                            // TODO: abrir pantalla edición
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

    private void startExerciseActivity(TimerAnimationQueue animationQueue) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("queue", animationQueue);
        startActivity(intent);
    }
}
