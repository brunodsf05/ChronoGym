package bdisfer1410.gymapp.activity;

import android.graphics.Insets;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.java.ListTools;


public class ImportActivity extends AppCompatActivity {
    private Button buttonAdd, buttonClose;
    private CheckBox checkboxSelectAll;
    private List<ExerciseCard> cardList;
    private List<Exercise> exerciseList;
    private ExerciseCardAdapter adapter;

    //region Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Prepare exercises
        exerciseList = new ArrayList<>();
        cardList = new ArrayList<>();

        if (savedInstanceState == null) {
            Serializable serializableExerciseList = getIntent().getSerializableExtra("exercises");

            if (serializableExerciseList instanceof List<?>) {
                for (Object obj : (List<?>) serializableExerciseList) {
                    if (obj instanceof Exercise) {
                        exerciseList.add((Exercise) obj);
                        cardList.add((ExerciseCard) obj);
                    }
                }
            }
        }

        // Init views
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleExit();
            }
        });
        initExercisesListRecyclerView(cardList);
        initClickable();
    }
    //endregion

    //region UI handlers
    private void handleExit() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.activity_import_dialog_exit_title)
                .setMessage(R.string.activity_import_dialog_exit_message)
                .setNegativeButton(R.string.activity_import_dialog_exit_button_confirm, (dialog, which) -> finish())
                .setNeutralButton(R.string.activity_import_dialog_exit_button_deny, null)
                .show();
    }

    //endregion

    //region UI initializers
    private void initExercisesListRecyclerView(List<ExerciseCard> cards) {
        RecyclerView exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseCardAdapter(cards, this::onCardClick, null);
        exercisesList.setAdapter(adapter);
    }

    private void initClickable() {
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonClose = findViewById(R.id.buttonClose);
        buttonAdd.setOnClickListener(v -> saveExercises());
        buttonClose.setOnClickListener(v -> handleExit());
    }

    //endregion

    //region Card functions
    private void onCardClick(ExerciseCard card) {
        Log.d("ImportActivity", "Toggling enabled state of pressed card");
        Toast.makeText(this, "onCardClick(ExerciseCard)", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region Exercise storage
    private void saveExercises() {
        Log.d("ImportActivity", "Saving selected exercises into storage");
        Toast.makeText(this, "saveExercises()", Toast.LENGTH_SHORT).show();
    }
    //endregion
}
