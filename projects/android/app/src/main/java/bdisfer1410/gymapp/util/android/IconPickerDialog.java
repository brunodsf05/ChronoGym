package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IconPickerDialog {
    private static final Integer COLUMNS = 6;

    public interface IconPickCallback {
        void onIconPicked(String key, int iconResId);
    }

    public static void show(@NonNull Context context,
                            @NonNull Map<String, Integer> iconMap,
                            @NonNull IconPickCallback callback) {

        List<String> keys = new ArrayList<>(iconMap.keySet());
        List<Integer> iconResIds = new ArrayList<>();
        for (String key : keys) {
            iconResIds.add(iconMap.get(key));
        }

        // Crear GridView programáticamente
        GridView gridView = new GridView(context);
        gridView.setNumColumns(COLUMNS);
        gridView.setVerticalSpacing(24);
        gridView.setHorizontalSpacing(24);
        gridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
        gridView.setAdapter(new IconGridAdapter(context, keys, iconResIds));

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Selecciona un icono")
                .setView(gridView)
                .setNegativeButton("Cancelar", null);

        final AlertDialog dialog = builder.create();

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedKey = keys.get(position);
            int selectedIconResId = iconResIds.get(position);
            callback.onIconPicked(selectedKey, selectedIconResId);
            dialog.dismiss(); // Cierra el diálogo
        });

        dialog.show();
    }

    private static class IconGridAdapter extends BaseAdapter {
        private final Context context;
        private final List<String> keys;
        private final List<Integer> iconResIds;
        private final LayoutInflater inflater;
        private final @ColorInt int iconTint;

        IconGridAdapter(Context context, List<String> keys, List<Integer> iconResIds) {
            this.context = context;
            this.keys = keys;
            this.iconResIds = iconResIds;
            this.inflater = LayoutInflater.from(context);
            this.iconTint = MaterialColors.getColor(context, com.google.android.material.R.attr.colorOnSurface, 0xFF000000);
        }

        @Override
        public int getCount() {
            return iconResIds.size();
        }

        @Override
        public Object getItem(int position) {
            return iconResIds.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = convertView instanceof ImageView
                    ? (ImageView) convertView
                    : new ImageView(context);

            Drawable icon = ContextCompat.getDrawable(context, iconResIds.get(position));
            if (icon != null) {
                Drawable wrapped = DrawableCompat.wrap(icon);
                DrawableCompat.setTint(wrapped, iconTint);
                imageView.setImageDrawable(wrapped);
            }

            int padding = (int) (16 * context.getResources().getDisplayMetrics().density);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new GridView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));

            return imageView;
        }
    }
}
