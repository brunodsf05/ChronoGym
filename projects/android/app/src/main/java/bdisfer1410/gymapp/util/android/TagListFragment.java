package bdisfer1410.gymapp.util.android;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.R;

/**
 * Fragment that shows a container with tags and a button.
 * A {@link TagListFragment.OnAddTagClickListener} can be linked to the button on click action.
 */
public class TagListFragment extends Fragment {
    private FlexboxLayout flexboxTags;
    private final List<String> tags = new ArrayList<>();
    private View addChip;
    private OnAddTagClickListener onAddTagClickListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tag_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flexboxTags = view.findViewById(R.id.flexboxTags);
        setupAddChip();
    }

    private void setupAddChip() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        addChip = inflater.inflate(R.layout.chip_add, flexboxTags, false);

        addChip.setOnClickListener(v -> {
            if (onAddTagClickListener != null) {
                onAddTagClickListener.onAddTagClick();
            }
        });

        flexboxTags.addView(addChip);
    }

    public void addTag(String tag) {
        if (tag != null) {
            String trimmed = tag.trim();
            if (!trimmed.isEmpty() && !tags.contains(trimmed)) {
                tags.add(trimmed);
                addTagChip(trimmed);
            }
        }
    }

    public void addTranslatedTag(String displayed, String stored) {
        if (stored != null) {
            String trimmed = stored.trim();
            if (!trimmed.isEmpty() && !tags.contains(trimmed)) {
                tags.add(trimmed); // store actual value
                addTagChip(displayed, trimmed); // display translated value
            }
        }
    }

    public List<String> getTags() {
        return new ArrayList<>(tags);
    }

    private void addTagChip(String tag) {
        addTagChip(tag, tag);
    }

    private void addTagChip(String displayedText, String storedValue) {
        flexboxTags.removeView(addChip);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View tagView = inflater.inflate(R.layout.chip_tag, flexboxTags, false);

        TextView textTag = tagView.findViewById(R.id.textTag);
        ImageView closeIcon = tagView.findViewById(R.id.closeIcon);

        textTag.setText(displayedText);

        closeIcon.setOnClickListener(v -> {
            flexboxTags.removeView(tagView);
            int index = tags.indexOf(storedValue);
            if (index != -1) {
                tags.remove(index);
                addChip.setVisibility(View.VISIBLE);
            }
        });

        flexboxTags.addView(tagView);
        flexboxTags.addView(addChip);
    }

    public void setOnAddTagClickListener(OnAddTagClickListener listener) {
        this.onAddTagClickListener = listener;
    }

    public void hideAddButton() {
        addChip.setVisibility(View.GONE);
    }

    public interface OnAddTagClickListener {
        void onAddTagClick();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
