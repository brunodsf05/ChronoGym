package bdisfer1410.gymapp.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.activity.editor.CardPage;
import bdisfer1410.gymapp.activity.editor.PagerEditorCardsAdapter;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.java.ListTools;

public class EditorActivity extends AppCompatActivity {
    private CardPage pagePoses, pageTransitions, pageSets, pageQueue, pageMain;
    private List<CardPage> pagesResources;
    private List<CardPage> pagesMain;
    private PagerEditorCardsAdapter pagerAdapter;
    private Exercise exercise;

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

        // Setup pages
        ViewPager2 viewPager = findViewById(R.id.pager);

        pagePoses = new CardPage(getString(R.string.activity_editor_page_poses), ListTools.cast(exercise.repoPoses, ExerciseCard.class));
        pageTransitions = new CardPage(getString(R.string.activity_editor_page_transitions), ListTools.cast(exercise.repoTransitions, ExerciseCard.class));
        pageSets = new CardPage(getString(R.string.activity_editor_page_sets), ListTools.cast(exercise.repoSets, ExerciseCard.class));

        pagesResources = List.of(pagePoses, pageTransitions, pageSets);
        pagesMain = List.of(pagePoses);

        pagerAdapter = new PagerEditorCardsAdapter(pagesMain, this::handleCardClick);

        viewPager.setAdapter(pagerAdapter);
    }

    private void handleCardClick(ExerciseCard exerciseCard) {
        Log.d(
                "EditorActivity",
                Arrays.toString(pagePoses.getCards().stream()
                        .map(ExerciseCard::getCardName)
                        .toArray())
        );
        Toast.makeText(
                this,
                "Clicked on: " + exerciseCard.getCardName(),
                Toast.LENGTH_SHORT
        ).show();

        if (pagerAdapter == null) return;
        pagerAdapter.setPages(
                pagerAdapter.getPages().size() == 1
                        ? pagesResources
                        : pagesMain
                );
    }
}
