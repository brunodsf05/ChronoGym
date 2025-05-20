package bdisfer1410.gymapp.util;

import android.content.Context;
import android.view.View;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.material.button.MaterialButton;

import java.util.List;

/**
 * Adds entries to an menu that's opened by some floating action button.
 */
public class FabMenuBuilder {
    public static void addFabButtons(Context context, ConstraintLayout parentLayout, View anchorView, List<FabAction> actions) {
        ConstraintSet constraintSet = new ConstraintSet();
        parentLayout.removeAllViews();
        parentLayout.addView(anchorView);

        for (FabAction action : actions) {
            // New button
            MaterialButton fabSub = new MaterialButton(context);
            fabSub.setId(View.generateViewId());
            fabSub.setText(action.text);

            // Give some style
            fabSub.setIconResource(action.iconResId);
            fabSub.setIconGravity(MaterialButton.ICON_GRAVITY_START);
            fabSub.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            fabSub.setCornerRadius(32);

            // Apply margins and constraint layout (some parts that all subFabs shares)
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0);
            fabSub.setLayoutParams(params);

            // Functionality
            fabSub.setVisibility(View.GONE);
            fabSub.setOnClickListener(action.onClickListener);

            parentLayout.addView(fabSub);
            action.generatedButton = fabSub;
        }

        constraintSet.clone(parentLayout);

        // Apply remaining constraint layout (the one that connects one button to another vertically)
        int lastId = anchorView.getId();

        for (FabAction action : actions) {
            int id = action.generatedButton.getId();

            constraintSet.connect(id, ConstraintSet.END, anchorView.getId(), ConstraintSet.END);
            constraintSet.connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START);
            constraintSet.connect(id, ConstraintSet.BOTTOM, lastId, ConstraintSet.TOP, 0);

            lastId = id;
        }

        constraintSet.connect(actions.get(0).generatedButton.getId(), ConstraintSet.BOTTOM, anchorView.getId(), ConstraintSet.TOP, 12);

        constraintSet.applyTo(parentLayout);
    }

    public static class FabAction {
        public String text;
        public int iconResId;
        public View.OnClickListener onClickListener;
        public MaterialButton generatedButton;

        public FabAction(String text, int iconResId, View.OnClickListener onClickListener) {
            this.text = text;
            this.iconResId = iconResId;
            this.onClickListener = onClickListener;
        }
    }
}
