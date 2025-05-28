package bdisfer1410.gymapp.activity.editor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.color.MaterialColors;


import java.util.ArrayList;
import java.util.List;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.serde.ExerciseSerdeJSON;

public class EditorDialog {

    public interface OnFormSubmittedListener {
        void onFormSubmitted(String id, String name, int iconResId, @Nullable Integer number);
    }

    public static void showEditorDialog(
            Context context,
            String labelId,
            String labelName,
            String labelIcon,
            String labelNumber,
            boolean showNumber,
            @Nullable String defaultId,
            @Nullable String defaultName,
            int defaultIconResId,
            @Nullable Integer defaultNumber,
            OnFormSubmittedListener listener
    ) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editor_generic, null);

        TextInputLayout inputLayoutId = dialogView.findViewById(R.id.inputLayoutId);
        TextInputLayout inputLayoutName = dialogView.findViewById(R.id.inputLayoutName);
        TextInputLayout inputLayoutNumber = dialogView.findViewById(R.id.inputLayoutNumber);

        TextInputEditText editTextId = dialogView.findViewById(R.id.editTextId);
        TextInputEditText editTextName = dialogView.findViewById(R.id.editTextName);
        TextInputEditText editTextNumber = dialogView.findViewById(R.id.editTextNumber);
        Spinner spinnerIcons = dialogView.findViewById(R.id.spinnerIcons);

        inputLayoutId.setHint(labelId);
        inputLayoutName.setHint(labelName);
        inputLayoutNumber.setHint(labelNumber);

        List<Integer> iconList = getIconList();
        IconAdapter adapter = new IconAdapter(context, iconList);
        spinnerIcons.setAdapter(adapter);

        // Set default values
        if (defaultId != null) editTextId.setText(defaultId);
        if (defaultName != null) editTextName.setText(defaultName);
        if (defaultNumber != null) editTextNumber.setText(String.valueOf(defaultNumber));
        inputLayoutNumber.setVisibility(showNumber ? View.VISIBLE : View.GONE);

        int defaultIconIndex = iconList.indexOf(defaultIconResId);
        if (defaultIconIndex >= 0) spinnerIcons.setSelection(defaultIconIndex);

        new MaterialAlertDialogBuilder(context)
                .setTitle("Edit Item")
                .setView(dialogView)
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String id = editTextId.getText().toString().trim();
                    String name = editTextName.getText().toString().trim();
                    Integer number = null;

                    if (showNumber) {
                        String numberText = editTextNumber.getText().toString().trim();
                        if (!numberText.isEmpty()) {
                            try {
                                number = Integer.parseInt(numberText);
                            }
                            catch (NumberFormatException e) {
                                number = null;
                            }
                        }
                    }

                    int selectedIconResId = (int) spinnerIcons.getSelectedItem();
                    listener.onFormSubmitted(id, name, selectedIconResId, number);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private static List<Integer> getIconList() {
        return new ArrayList<>(ExerciseSerdeJSON.ICONS.values());
    }

    static class IconAdapter extends ArrayAdapter<Integer> {
        private final Context context;
        private final List<Integer> iconList;

        IconAdapter(Context context, List<Integer> icons) {
            super(context, android.R.layout.simple_spinner_item, icons);
            this.context = context;
            this.iconList = icons;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getIconView(position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getIconView(position);
        }

        private View getIconView(int position) {
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(iconList.get(position));

            // Apply Material theme color tint
            int iconColor = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, Color.BLACK);
            imageView.setColorFilter(iconColor, android.graphics.PorterDuff.Mode.SRC_IN);

            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setPadding(16, 16, 16, 16);
            return imageView;
        }
    }
}
