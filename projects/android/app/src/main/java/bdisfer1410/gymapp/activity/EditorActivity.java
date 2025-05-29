package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
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
import bdisfer1410.gymapp.activity.editor.EditorDialogBuilder;
import bdisfer1410.gymapp.activity.editor.PagerEditorCardsAdapter;
import bdisfer1410.gymapp.activity.editor.SimpleCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.exercise.models.routine.movement.ExercisePose;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;
import bdisfer1410.gymapp.exercise.timer.state.TimerAnimationQueue;
import bdisfer1410.gymapp.util.android.IconPickerDialog;
import bdisfer1410.gymapp.util.android.TextInputDialog;
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
    private Button buttonEditionCancel, buttonEditionModify, buttonAdd;
    private LinearLayout editionButtons;
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
                exercise = new Exercise(getString(R.string.activity_editor_action_default_rename_exercise), R.drawable.ic_missing, new TimerAnimationQueue(new ArrayList<>()), new ArrayList<>());
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

        // Set up views
        buttonEditionCancel = findViewById(R.id.buttonEditionCancel);
        buttonEditionModify = findViewById(R.id.buttonEditionModify);
        buttonAdd = findViewById(R.id.buttonAdd);
        editionButtons = findViewById(R.id.editionButtons);

        buttonAdd.setOnClickListener(v -> handleButtonAddClick());

        // Setup pages
        initPages();
        pagePoses.setCards(ListTools.cast(exercise.repoPoses, ExerciseCard.class));
        pageTransitions.setCards(ListTools.cast(exercise.repoTransitions, ExerciseCard.class));
        pageSets.setCards(ListTools.cast(exercise.repoSets, ExerciseCard.class));
        pageExerciseQueue.setCards(ListTools.cast(exercise.getQueue().list, ExerciseCard.class));

        // Setup pager
        viewPager = findViewById(R.id.pager);
        pagerAdapter = new PagerEditorCardsAdapter(sectionExercise, this::handleCardClick, exerciseCard -> Toast.makeText(this, "TODO: LongPress delete dialog", Toast.LENGTH_SHORT).show());
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

        sectionExercise = List.of(pageExerciseQueue);

        // Files
        pageExerciseFile = new CardPage(getString(R.string.activity_editor_section_file), List.of(
                new SimpleCard("save", R.drawable.ic_ui_save, getString(R.string.activity_editor_action_name_save), getString(R.string.activity_editor_action_desc_save)),
                new SimpleCard("help", R.drawable.ic_ui_help, getString(R.string.activity_editor_action_name_help), getString(R.string.activity_editor_action_desc_help)),
                new SimpleCard("exit", R.drawable.ic_ui_close, getString(R.string.activity_editor_action_name_exit), getString(R.string.activity_editor_action_desc_exit))
        ));

        sectionFiles = List.of(pageExerciseFile, pageExerciseInfo);
    }
    //endregion

    //region HighLevel Handlers
    private void handleExit() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.activity_editor_action_title_exit)
                .setMessage(R.string.activity_editor_action_message_exit)
                .setNegativeButton(R.string.activity_editor_action_button_exit_confirm, (dialog, which) -> finish())
                .setNeutralButton(R.string.activity_editor_action_button_exit_deny, null)
                .show();
    }

    private void handleSectionClick(MaterialButtonToggleGroup group, int checkedId, boolean isChecked) {
        if (!isChecked) return;

        editionButtons.setVisibility(View.GONE);

        // Determine which section is selected
        openedSection = Map.of(
                R.id.sectionExercise, Sections.EXERCISE,
                R.id.sectionFile, Sections.FILE,
                R.id.sectionResources, Sections.RESOURCES
        ).getOrDefault(checkedId, Sections.EXERCISE);

        buttonAdd.setVisibility(
                openedSection == Sections.FILE
                        ? View.GONE
                        : View.VISIBLE
        );

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

    private void handleButtonAddClick() {
        int currentPageIndex = viewPager.getCurrentItem();
        Log.d("EditorActivity", "Página actual del ViewPager: " + currentPageIndex);

        switch (viewPager.getCurrentItem()) {
            case 0: handleButtonAddPose(); break;
            case 1: Toast.makeText(this, "TODO: addTransition", Toast.LENGTH_SHORT).show(); break;
            case 2: Toast.makeText(this, "TODO: addSet", Toast.LENGTH_SHORT).show(); break;
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

            case "name":
                TextInputDialog.show(this, getString(R.string.activity_editor_action_title_rename_exercise), exercise.getName(), text -> {
                    exercise.setName(text);
                    updateCardExerciseInformation();
                });
                break;

            case "icon":
                IconPickerDialog.show(this, ExerciseSerdeJSON.ICONS, (key, iconResId) -> {
                    exercise.iconPath = key;
                    exercise.setIcon(iconResId);
                    updateCardExerciseInformation();
                });
                break;

            default:
                Log.e("EditorActivity", "Selected card id is not a valid pageFile action");
                break;
        }
    }

    private void handleCardClickOnPageResources(ExerciseCard exerciseCard) {
        switch (viewPager.getCurrentItem()) {
            case 0:
                handleButtonModifyPose((ExercisePose) exerciseCard);
                break;
            case 1:
                Toast.makeText(this, "TODO: modTransition", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "TODO: modSet", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //endregion

    //region Handlers: ExerciseCard: Pose
    @SuppressLint("NotifyDataSetChanged")
    private void handleButtonAddPose() {
        EditorDialogBuilder.pose(this, exercise.getRepoPosesIds(), (id, name, iconResId, number) -> {
            Log.d("EditorActivity", "Creating new pose");
            ExercisePose pose = new ExercisePose(name, iconResId);
            pose.setId(id);
            exercise.repoPoses.add(pose);
            pagePoses.setCards(ListTools.cast(exercise.repoPoses, ExerciseCard.class));
            pagerAdapter.notifyDataSetChanged();
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void handleButtonModifyPose(ExercisePose oldPose) {
        EditorDialogBuilder.pose(
                this,
                exercise.getRepoPosesIds(),
                oldPose.getId(),
                oldPose.getName(),
                oldPose.getIcon(),
                (id, name, iconResId, number
            ) -> {
            Log.d("EditorActivity", "Modifying pose");
            ExercisePose pose = new ExercisePose(name, iconResId);
            pose.setId(id);
            exercise.updatePoseFromRepo(pose);
            pagePoses.setCards(ListTools.cast(exercise.repoPoses, ExerciseCard.class));
            pagerAdapter.notifyDataSetChanged();
        });
    }
    //endregion

    //region Handlers: ExerciseCard: Transitions
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
