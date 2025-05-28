package bdisfer1410.gymapp.activity.editor;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;

public class EditorDialog {

    public interface OnFormSubmittedListener {
        void onFormSubmitted(String id, String name, int iconResId, @Nullable Integer number);
    }

    public static void showEditorDialog(
            Context context,
            String title,
            String labelNumber,
            boolean showNumber,
            int minNumber,
            @Nullable String defaultId,
            @Nullable String defaultName,
            int defaultIconResId,
            @Nullable Integer defaultNumber,
            List<String> blacklistIds,
            OnFormSubmittedListener listener
    ) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editor_generic, null);

        TextInputLayout inputLayoutId = dialogView.findViewById(R.id.inputLayoutId);
        TextInputLayout inputLayoutName = dialogView.findViewById(R.id.inputLayoutName);
        TextInputLayout inputLayoutNumber = dialogView.findViewById(R.id.inputLayoutNumber);

        TextInputEditText editTextId = dialogView.findViewById(R.id.editTextId);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextName);
        TextInputEditText editTextNumber = dialogView.findViewById(R.id.editTextNumber);
        GridView gridIcons = dialogView.findViewById(R.id.gridIcons);

        inputLayoutId.setHint(context.getString(R.string.activity_editor_dialog_any_id_label));
        inputLayoutName.setHint(context.getString(R.string.activity_editor_dialog_any_name_label));
        inputLayoutNumber.setHint(labelNumber);
        inputLayoutNumber.setVisibility(showNumber ? View.VISIBLE : View.GONE);

        if (defaultId != null) editTextId.setText(defaultId);
        if (defaultName != null) editTextName.setText(defaultName);
        if (defaultNumber != null) editTextNumber.setText(String.valueOf(defaultNumber));

        List<Integer> iconList = getIconList();
        final int[] selectedIconResId = {defaultIconResId};

        IconAdapter adapter = new IconAdapter(context, iconList, defaultIconResId, resId -> {
            selectedIconResId[0] = resId;
            gridIcons.setBackground(null);
        });
        gridIcons.setAdapter(adapter);

        Set<String> blacklistSet = new HashSet<>(blacklistIds);

        androidx.appcompat.app.AlertDialog dialog = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(R.string.activity_any_accept, null)
                .setNegativeButton(R.string.activity_any_deny, null)
                .show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String id = editTextId.getText() != null ? editTextId.getText().toString().trim() : "";
            String name = editTextName.getText() != null ? editTextName.getText().toString().trim() : "";
            String numberText = editTextNumber.getText() != null ? editTextNumber.getText().toString().trim() : "";

            boolean valid = true;
            Integer number = null;

            inputLayoutId.setError(null);
            inputLayoutName.setError(null);
            inputLayoutNumber.setError(null);

            if (id.isEmpty()) {
                inputLayoutId.setError(context.getString(R.string.activity_editor_dialog_any_id_error_required));
                valid = false;
            }
            else {
                if (blacklistSet.contains(id)) {
                    inputLayoutId.setError(context.getString(R.string.activity_editor_dialog_any_id_error_repeated));
                    valid = false;
                }
            }

            if (name.isEmpty()) {
                inputLayoutName.setError(context.getString(R.string.activity_editor_dialog_any_name_error_required));
                valid = false;
            }

            if (showNumber && !numberText.isEmpty()) {
                try {
                    number = Integer.parseInt(numberText);

                    if (number < minNumber) {
                        inputLayoutNumber.setError(context.getString(R.string.activity_editor_dialog_any_number_error_lower) + minNumber);
                        valid = false;
                    }
                }
                catch (NumberFormatException e) {
                    inputLayoutNumber.setError(context.getString(R.string.activity_editor_dialog_any_number_error_invalid));
                    valid = false;
                }
            }
            else {
                if (showNumber) {
                    inputLayoutNumber.setError(context.getString(R.string.activity_editor_dialog_any_number_error_invalid));
                    valid = false;
                }
            }

            if (selectedIconResId[0] <= 0) {
                Toast.makeText(context, "Please select an icon", Toast.LENGTH_SHORT).show();

                GradientDrawable errorBorder = new GradientDrawable();
                errorBorder.setColor(Color.TRANSPARENT);
                errorBorder.setStroke(
                        (int) (context.getResources().getDisplayMetrics().density * 2),
                        MaterialColors.getColor(context, com.google.android.material.R.attr.colorError, Color.RED));
                errorBorder.setCornerRadius(16);

                gridIcons.setBackground(errorBorder);

                valid = false;
            }
            else {
                gridIcons.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_grid_outline));
            }

            if (valid) {
                listener.onFormSubmitted(id, name, selectedIconResId[0], number);
                dialog.dismiss();
            }
        });
    }

    private static List<Integer> getIconList() {
        return new ArrayList<>(ExerciseSerdeJSON.ICONS.values());
    }

    static class IconAdapter extends ArrayAdapter<Integer> {

        public interface OnIconSelectedListener {
            void onIconSelected(int resId);
        }

        private final Context context;
        private final List<Integer> iconList;
        private final OnIconSelectedListener listener;
        private int selectedResId;

        IconAdapter(Context context, List<Integer> icons, int defaultSelected, OnIconSelectedListener listener) {
            super(context, 0, icons);
            this.context = context;
            this.iconList = icons;
            this.listener = listener;
            this.selectedResId = defaultSelected;
        }

        @Override
        public int getCount() {
            return iconList.size();
        }

        @Override
        public Integer getItem(int position) {
            return iconList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                int size = (int) (parent.getResources().getDisplayMetrics().density * 64);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                imageView.setPadding(16, 16, 16, 16);
            }
            else {
                imageView = (ImageView) convertView;
            }

            int resId = iconList.get(position);
            imageView.setImageResource(resId);

            int iconColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
            imageView.setColorFilter(iconColor, android.graphics.PorterDuff.Mode.SRC_IN);

            imageView.setBackgroundResource(resId == selectedResId ? R.drawable.bg_icon_selected : 0);

            imageView.setOnClickListener(v -> {
                selectedResId = resId;
                notifyDataSetChanged();
                listener.onIconSelected(resId);
                // Restore original border because it may be styled as error
                View parentView = (View) v.getParent();
                if (parentView instanceof GridView) {
                    parentView.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_grid_outline));
                }
            });

            return imageView;
        }
    }
}
