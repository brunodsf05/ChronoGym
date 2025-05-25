package bdisfer1410.gymapp.activity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
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

public class EditorActivity extends AppCompatActivity {
    private CardPage pagePoses, pageTransitions, pageSets;

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

        ViewPager2 viewPager = findViewById(R.id.pager);

        pagePoses = new CardPage(getString(R.string.activity_editor_page_poses), new ArrayList<>());
        pageTransitions = new CardPage(getString(R.string.activity_editor_page_transitions), new ArrayList<>());
        pageSets = new CardPage(getString(R.string.activity_editor_page_sets), new ArrayList<>());

        List<CardPage> pages = List.of(pagePoses, pageTransitions, pageSets);

        //region Mock
        pagePoses.setCards(new ArrayList<>(Arrays.asList(
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
        )));
        pageTransitions.setCards(new ArrayList<>(Arrays.asList(
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return R.drawable.ic_exercise_default; }
                    @Override @NonNull public String getCardName() { return "Jumping Jacks"; }
                    @Override public String getCardTags() { return "Cardio"; }
                    @Override @NonNull public String getCardInterval() { return "30s"; }
                    @Override public String getCardExtra() { return null; }
                },
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return R.drawable.ic_exercise_default; }
                    @Override @NonNull public String getCardName() { return "Mountain Climbers"; }
                    @Override public String getCardTags() { return "Core"; }
                    @Override @NonNull public String getCardInterval() { return "30s"; }
                    @Override public String getCardExtra() { return "Fast pace"; }
                }
        )));

        pageSets.setCards(new ArrayList<>(Arrays.asList(
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return null; }
                    @Override @NonNull public String getCardName() { return "Stretching"; }
                    @Override public String getCardTags() { return "Cool Down"; }
                    @Override @NonNull public String getCardInterval() { return "2m"; }
                    @Override public String getCardExtra() { return "Full body"; }
                },
                new ExerciseCard() {
                    @Override public Integer getCardIcon() { return null; }
                    @Override @NonNull public String getCardName() { return "Breathing"; }
                    @Override public String getCardTags() { return null; }
                    @Override @NonNull public String getCardInterval() { return "1m"; }
                    @Override public String getCardExtra() { return "Relaxation"; }
                }
        )));
        //endregion

        PagerEditorCardsAdapter pagerAdapter = new PagerEditorCardsAdapter(
                pages,
                exerciseCard -> Toast.makeText(this,
                        "Clicked on: " + exerciseCard.getCardName(),
                        Toast.LENGTH_SHORT).show()
        );

        viewPager.setAdapter(pagerAdapter);
    }
}
