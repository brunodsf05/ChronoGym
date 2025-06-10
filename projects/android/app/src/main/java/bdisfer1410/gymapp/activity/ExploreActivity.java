package bdisfer1410.gymapp.activity;

import android.content.Intent;
import android.graphics.Insets;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.activity.explorer.ExplorerApi;
import bdisfer1410.gymapp.exercise.data.Tags;
import bdisfer1410.gymapp.exercise.models.Exercise;
import bdisfer1410.gymapp.util.android.HttpTools;
import bdisfer1410.gymapp.util.android.ResourceUtils;
import bdisfer1410.gymapp.util.android.TagListFragment;
import bdisfer1410.gymapp.util.android.TextDropdownDialog;

public class ExploreActivity extends AppCompatActivity {
    //region Views
    private TagListFragment fragmentTagFilterExclusive, fragmentTagFilterInclusive;
    private Button buttonClose, buttonHelp, buttonSearch;
    //endregion

    //region Android
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars()).toPlatformInsets();
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Init views
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleExit();
            }
        });
        initFragmentTagFilters();
        initSmallButtons();
        initSearchButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
        buttonSearch.setEnabled(true);
    }
    //endregion

    //region UI initializers
    private void initFragmentTagFilters() {
        // Bind views
        fragmentTagFilterExclusive = (TagListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentTagFilterExclusive);

        fragmentTagFilterInclusive = (TagListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragmentTagFilterInclusive);

        // Bind functionality
        fragmentTagFilterExclusive.setOnAddTagClickListener(() -> addTag(fragmentTagFilterExclusive));
        fragmentTagFilterInclusive.setOnAddTagClickListener(() -> addTag(fragmentTagFilterInclusive));
    }

    private void initSmallButtons() {
        buttonClose = findViewById(R.id.buttonClose);
        buttonHelp = findViewById(R.id.buttonHelp);

        buttonClose.setOnClickListener(v -> handleExit());
        buttonHelp.setOnClickListener(v ->
                new MaterialAlertDialogBuilder(this)
                        .setTitle(R.string.activity_explore_dialog_help_title)
                        .setMessage(R.string.activity_explore_dialog_help_message)
                        .setPositiveButton(R.string.activity_explore_dialog_help_accept, null)
                        .show()
        );
    }

    private void initSearchButton() {
        buttonSearch = findViewById(R.id.buttonAdd);
        buttonHelp = findViewById(R.id.buttonHelp);

        buttonSearch.setOnClickListener(v -> search());
    }
    //endregion

    //region UI handler
    private void handleExit() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.activity_explore_dialog_exit_title)
                .setMessage(R.string.activity_explore_dialog_exit_message)
                .setNegativeButton(R.string.activity_explore_dialog_exit_button_confirm, (dialog, which) -> finish())
                .setNeutralButton(R.string.activity_explore_dialog_exit_button_deny, null)
                .show();
    }

    /**
     * Shows a dialog to select a string and add it as a tag.
     * @param fragmentTagList The fragment that will receive the tag.
     * @param allOptions A list containing all the options that can be added. Keep in mind that the
     *                   displayed list will not show the elements already added.
     */
    private void addTag(TagListFragment fragmentTagList) {
        Map<String, String> allOptionsMap = Tags.data.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> !fragmentTagList.getTags().contains(entry.getKey()))
                .collect(Collectors.toMap(
                        entry -> getString(entry.getValue()),
                        Map.Entry::getKey
                ));

        int allOptionsSize = allOptionsMap.size();

        if (allOptionsSize == 0) {
            Toast.makeText(this, R.string.activity_explore_error_no_tags_to_add, Toast.LENGTH_SHORT).show();
            return;
        }

        List<String> allOptions = new ArrayList<>(allOptionsMap.keySet());

        TextDropdownDialog.show(this, getString(R.string.activity_explore_dialog_add_title), allOptions, 0, false, (selectedItem, numberInput) -> {
            fragmentTagList.addTranslatedTag(selectedItem, allOptionsMap.get(selectedItem));
            buttonSearch.setText(R.string.activity_explore_clickable_search_filtered);
            if (allOptionsSize == 1) fragmentTagList.hideAddButton();
            addTag(fragmentTagList);
        });
    }
    //endregion

    private void search() {
        buttonSearch.setEnabled(false);

        ExplorerApi.fetch(
                this,
                fragmentTagFilterExclusive.getTags(),
                fragmentTagFilterInclusive.getTags(),
                result -> {
                    if (result.isErr()) {
                        Toast.makeText(this, result.getError(), Toast.LENGTH_SHORT).show();
                        buttonSearch.setEnabled(true);
                        return;
                    }

                    Intent intent = new Intent(this, ImportActivity.class);
                    intent.putExtra("exercises", (Serializable) result.getValue());
                    startActivity(intent);
                }
        );
    }
}
