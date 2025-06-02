package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
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


import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.java.ListTools;


public class ImportActivity extends AppCompatActivity {
    private Button buttonAdd, buttonClose;
    private MaterialCheckBox checkboxSelectAll;
    private List<ExerciseCard> cardList;
    private List<Exercise> exerciseList;
    private ExerciseCardAdapter adapter;
    private int selectedCounter = 0;

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

    @SuppressLint("NotifyDataSetChanged")
    private void updateCardList() {
        cardList = ListTools.cast(exerciseList, ExerciseCard.class);
        adapter.notifyDataSetChanged();
    }

    private void updateCheckboxStyle() {
        checkboxSelectAll.setCheckedState(
                selectedCounter == 0
                        ? MaterialCheckBox.STATE_UNCHECKED
                : selectedCounter == exerciseList.size()
                        ? MaterialCheckBox.STATE_CHECKED
                        : MaterialCheckBox.STATE_INDETERMINATE
        );
    }
    //endregion

    //region UI initializers
    private void initExercisesListRecyclerView(List<ExerciseCard> cards) {
        RecyclerView exercisesList = findViewById(R.id.exercisesList);
        exercisesList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ExerciseCardAdapter(cards, this::onCardClick, true);
        exercisesList.setAdapter(adapter);
    }

    private void initClickable() {
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonClose = findViewById(R.id.buttonClose);
        checkboxSelectAll = findViewById(R.id.checkboxSelectAll);

        buttonAdd.setOnClickListener(v -> saveExercises());
        buttonClose.setOnClickListener(v -> handleExit());
        checkboxSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> makeAllExercisesSelected(isChecked));
    }

    //endregion

    //region Card functions
    private void onCardClick(ExerciseCard card) {
        Log.d("ImportActivity", "Toggling enabled state of pressed card");
        Boolean isSelected = toggleExerciseSelection((Exercise) card);
        if (isSelected == null) return;

        // Update UI
        selectedCounter += isSelected ? 1 : -1;
        updateCheckboxStyle();
        updateCardList();
    }

    /**
     * Toggles the selected style of an exercise.
     * @param exercise The exercise to un/selected
     * @return If the exercise ended up being selected (true) unselected (false) or it couldn't do it (null)
     */
    private Boolean toggleExerciseSelection(Exercise exercise) {
        if (exercise.cardState == ExerciseCardState.DISABLED)
            return null;


        boolean isUnselected = exercise.cardState == ExerciseCardState.NORMAL;
        exercise.cardState = isUnselected
                ? ExerciseCardState.SELECTED
                : ExerciseCardState.NORMAL;

        return isUnselected;
    }

    private void makeAllExercisesSelected(boolean selected) {
        selectedCounter = selected ? exerciseList.size() : 0;

        ExerciseCardState newState = selected ? ExerciseCardState.SELECTED : ExerciseCardState.NORMAL;

        for (Exercise exercise : exerciseList) {
            exercise.cardState = newState;
        }

        updateCardList();
    }
    //endregion

    //region Exercise storage
    private void saveExercises() {
        Log.d("ImportActivity", "Saving selected exercises into storage");
        Toast.makeText(this, "saveExercises()", Toast.LENGTH_SHORT).show();
    }
    //endregion
}
