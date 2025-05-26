package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.activity.editor.CardPage;
import bdisfer1410.gymapp.activity.editor.PagerEditorCardsAdapter;
import bdisfer1410.gymapp.activity.editor.SimpleCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.java.Identifiable;
import bdisfer1410.gymapp.util.java.ListTools;
import me.relex.circleindicator.CircleIndicator3;

public class EditorActivity extends AppCompatActivity {
    //region Pages
    private CardPage pagePoses, pageTransitions, pageSets, pageExerciseQueue, pageExerciseInfo, pageExerciseFile;
    private List<CardPage> sectionExercise, sectionFiles, sectionResources;
    private PagerEditorCardsAdapter pagerAdapter;
    private Sections openedSection = Sections.EXERCISE;
    //endregion
    //region Views
    private CircleIndicator3 indicator;
    private ViewPager2 viewPager;
    //endregion
    private Exercise exercise;

    private enum Sections {
        EXERCISE, FILE, RESOURCES;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Load exercise to edit or start a new one
        if (savedInstanceState == null) {
            Object obj = getIntent().getSerializableExtra("exercise");

            if (obj instanceof Exercise){
                Log.d("EditorActivity", "Loading Exercise from Intent");
                exercise = (Exercise) obj;
            }
            else {
                Log.d("EditorActivity", "Creating new exercise object");
                exercise = new Exercise("", 0, null, null);
                exercise.setRepositories(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
            }
        }

        // Setup sections
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.sections);
        toggleGroup.addOnButtonCheckedListener(this::handleSectionClick);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleExit();
            }
        });

        // Setup pages
        initPages();
        pagePoses.setCards(ListTools.cast(exercise.repoPoses, ExerciseCard.class));
        pageTransitions.setCards(ListTools.cast(exercise.repoTransitions, ExerciseCard.class));
        pageSets.setCards(ListTools.cast(exercise.repoSets, ExerciseCard.class));
        pageExerciseQueue.setCards(ListTools.cast(exercise.getQueue().list, ExerciseCard.class));

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new PagerEditorCardsAdapter(sectionExercise, this::handleCardClick);
        viewPager.setAdapter(pagerAdapter);

        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        updateCardExerciseInformation();
    }

    //region Setup
    private void initPages() {
        // Resources
        pagePoses = new CardPage(getString(R.string.activity_editor_page_poses), List.of());
        pageTransitions = new CardPage(getString(R.string.activity_editor_page_transitions), List.of());
        pageSets = new CardPage(getString(R.string.activity_editor_page_sets), List.of());

        sectionResources = List.of(pagePoses, pageTransitions, pageSets);

        // Exercise
        pageExerciseInfo = new CardPage(getString(R.string.activity_editor_page_exercise_info), List.of(
                new SimpleCard("name", R.drawable.ic_editor_name, "???", getString(R.string.activity_editor_action_desc_rename_exercise)),
                new SimpleCard("icon", R.drawable.ic_missing, getString(R.string.activity_editor_action_title_reiconify_exercise), getString(R.string.activity_editor_action_desc_reiconify_exercise))
        ));
        pageExerciseQueue = new CardPage(getString(R.string.activity_editor_page_exercise_queue), List.of(), true);

        sectionExercise = List.of(pageExerciseInfo, pageExerciseQueue);

        // Files
        pageExerciseFile = new CardPage(getString(R.string.activity_editor_section_file), List.of(
                new SimpleCard("save", R.drawable.ic_ui_save, getString(R.string.activity_editor_action_name_save), getString(R.string.activity_editor_action_desc_save)),
                new SimpleCard("help", R.drawable.ic_ui_help, getString(R.string.activity_editor_action_name_help), getString(R.string.activity_editor_action_desc_help)),
                new SimpleCard("exit", R.drawable.ic_ui_close, getString(R.string.activity_editor_action_name_exit), getString(R.string.activity_editor_action_desc_exit))
        ));

        sectionFiles = List.of(
                pageExerciseFile
        );
    }
    //endregion

    //region HighLevel Handlers
    private void handleExit() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.activity_editor_action_title_exit)
                .setNegativeButton(R.string.activity_editor_action_button_exit_confirm, (dialog, which) -> finish())
                .setNeutralButton(R.string.activity_editor_action_button_exit_deny, null)
                .show();
    }

    private void handleSectionClick(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (!isChecked) return;

        // Determine which section is selected
        openedSection = Map.of(
                R.id.sectionExercise, Sections.EXERCISE,
                R.id.sectionFile, Sections.FILE,
                R.id.sectionResources, Sections.RESOURCES
        ).getOrDefault(checkedId, Sections.EXERCISE);

        // Swap pages based on enum
        Log.d("EditorActivity", String.format("Clicked on section \"%s\"", openedSection));

        pagerAdapter.setPages(
                Map.of(
                        Sections.EXERCISE, sectionExercise,
                        Sections.FILE, sectionFiles,
                        Sections.RESOURCES, sectionResources
                ).getOrDefault(openedSection, List.of())
        );

        // Update indicator
        indicator.setViewPager(viewPager);

        indicator.setVisibility(
                (pagerAdapter.getPages().size() > 1)
                        ? View.VISIBLE
                        : View.INVISIBLE
        );

        // Do a little animation
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.pop);
        viewPager.startAnimation(fadeIn);
    }

    private void handleCardClick(ExerciseCard exerciseCard) {
        if (exerciseCard == null || pagerAdapter == null) {
            Log.e("EditorActivity", "Can't handle click because \"exerciseCard\" or \"pagerAdapter\" is null");
            return;
        }

        Log.d("EditorActivity", String.format("Clicked on card from section \"%s\" with name \"%s\"", openedSection, exerciseCard.getCardName()));

        switch (openedSection) {
            case EXERCISE : handleCardClickOnPageExercise (exerciseCard); break;
            case FILE     : handleCardClickOnPageFile     (exerciseCard); break;
            case RESOURCES: handleCardClickOnPageResources(exerciseCard); break;

            default:
                Toast.makeText(this, R.string.activity_editor_error_invalid_section, Toast.LENGTH_SHORT).show();
        }
    }
    //endregion

    //region Handlers: ExerciseCard
    private void handleCardClickOnPageExercise(ExerciseCard exerciseCard) {
        Toast.makeText(this, "handleCardClickOnPageExercise", Toast.LENGTH_SHORT).show();
    }

    private void handleCardClickOnPageFile(ExerciseCard exerciseCard) {
        // Manage Identifiable instance
        if (!(exerciseCard instanceof Identifiable)) {
            Log.e("EditorActivity", "Selected card is not an instance of \"Identifiable\"");
           return;
        }

        Identifiable identifiable = (Identifiable) exerciseCard;

        // Execute based of id
        switch (identifiable.getId()) {
            case "save":
                Toast.makeText(this, "TODO: Implement save", Toast.LENGTH_SHORT).show();
                break;

            case "help":
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.activity_editor_action_title_help)
                        .setMessage(R.string.activity_editor_action_message_help)
                        .setPositiveButton(R.string.activity_editor_action_button_help_confirm, null)
                        .show();
                break;

            case "exit":
                handleExit();
                break;

            default:
                Log.e("EditorActivity", "Selected card id is not a valid pageFile action");
                break;
        }
    }

    private void handleCardClickOnPageResources(ExerciseCard exerciseCard) {
        Toast.makeText(this, "handleCardClickOnPageResources", Toast.LENGTH_SHORT).show();
    }
    //endregion

    //region Updaters
    @SuppressLint("NotifyDataSetChanged")
    private void updateCardExerciseInformation() {
        ((SimpleCard)pageExerciseInfo.getCards().get(0)).setText(exercise.getName());
        ((SimpleCard)pageExerciseInfo.getCards().get(1)).setIcon(exercise.getIcon());
        pagerAdapter.notifyDataSetChanged();
    }
    //endregion
}
