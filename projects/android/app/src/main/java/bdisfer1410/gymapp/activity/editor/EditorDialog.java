package bdisfer1410.gymapp.activity.editor;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
        GridView gridIcons = dialogView.findViewById(R.id.gridIcons);

        inputLayoutId.setHint(labelId);
        inputLayoutName.setHint(labelName);
        inputLayoutNumber.setHint(labelNumber);
        inputLayoutNumber.setVisibility(showNumber ? View.VISIBLE : View.GONE);

        List<Integer> iconList = getIconList();
        final int[] selectedIconResId = {defaultIconResId};

        IconAdapter adapter = new IconAdapter(context, iconList, defaultIconResId, resId -> selectedIconResId[0] = resId);
        gridIcons.setAdapter(adapter);

        // Default values
        if (defaultId != null) editTextId.setText(defaultId);
        if (defaultName != null) editTextName.setText(defaultName);
        if (defaultNumber != null) editTextNumber.setText(String.valueOf(defaultNumber));

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
                            } catch (NumberFormatException e) {
                                number = null;
                            }
                        }
                    }

                    listener.onFormSubmitted(id, name, selectedIconResId[0], number);
                })
                .setNegativeButton("Cancel", null)
                .show();
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
                int size = (int) (parent.getResources().getDisplayMetrics().density * 100);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(size, size));
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                imageView.setPadding(4, 4, 4, 4);
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
            });

            return imageView;
        }
    }
}
