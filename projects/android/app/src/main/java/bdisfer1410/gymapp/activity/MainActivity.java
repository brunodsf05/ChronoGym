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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Arrays;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.android.FabMenuBuilder;
import bdisfer1410.gymapp.util.Result;
import bdisfer1410.gymapp.util.data.QuickFileManager;

public class MainActivity extends AppCompatActivity {
    private RecyclerView exercisesList;
    private ExerciseCardAdapter adapter;
    private List<ExerciseCard> cardList;

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

        //region exercisesListRecyclerView
        exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        cardList = Arrays.asList(
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return R.drawable.ic_exercise_default; }
                    @Override @NonNull public String getCardName() { return "Push-Ups"; }
                    @Override public String getCardTags() { return "Chest, Arms"; }
                    @Override @NonNull public String getCardInterval() { return "30s"; }
                    @Override public String getCardExtra() { return "x15"; }
                },
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return null; }
                    @Override @NonNull public String getCardName() { return "Plank"; }
                    @Override public String getCardTags() { return null; }
                    @Override @NonNull public String getCardInterval() { return "45s"; }
                    @Override public String getCardExtra() { return null; }
                },
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return R.drawable.ic_exercise_default; }
                    @Override @NonNull public String getCardName() { return "Squats"; }
                    @Override public String getCardTags() { return "Legs"; }
                    @Override @NonNull public String getCardInterval() { return "1m"; }
                    @Override public String getCardExtra() { return "3 sets"; }
                }
        );

        adapter = new ExerciseCardAdapter(cardList, card ->
                Toast.makeText(this, "Clicked: " + card.getCardName(), Toast.LENGTH_SHORT).show()
        );

        exercisesList.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                adapter.swapItems(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // Not used
            }
        });

        helper.attachToRecyclerView(exercisesList);
        //endregion

        // findViewById(R.id.button1).setOnClickListener(v -> startExerciseActivity(ExerciseMock.CALISTHENICS));
        // findViewById(R.id.button2).setOnClickListener(v -> startExerciseActivity(ExerciseMock.TIMERS));

        ConstraintLayout fabLayout = findViewById(R.id.fabLayout);
        Button fabMain = findViewById(R.id.fab_main);

        List<FabMenuBuilder.FabAction> fabActions = List.of(
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_explore), R.drawable.ic_ui_explore, v ->
                        Toast.makeText(this, "WIP: Explorar rutinas", Toast.LENGTH_SHORT).show()
                ),
                new FabMenuBuilder.FabAction(getString(R.string.activity_main_menu_import), R.drawable.ic_ui_import, v -> {
                    Toast.makeText(this, "TEST: Importar rutina", Toast.LENGTH_SHORT).show();
                    // Read file
                    String jsonString = QuickFileManager
                            .with(MainActivity.this)
                            .rawRes(R.raw.serialized_exercise_prototype)
                            .read();

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
