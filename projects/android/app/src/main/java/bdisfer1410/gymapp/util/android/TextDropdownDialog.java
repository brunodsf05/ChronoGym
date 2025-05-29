package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import bdisfer1410.gymapp.R;

public class TextDropdownDialog {

    /**
     * Shows a dialog with a dropdown (spinner) to select a string option.
     *
     * @param context The context in which the dialog should be displayed.
     * @param title The title of the dialog.
     * @param options Array of string options to display in the dropdown.
     * @param listener Callback to handle the selected string.
     */
    public static void show(Context context, String title, List<String> options, OnItemSelectedListener listener) {
        // Inflate custom layout with a Spinner
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinner, null);

        // Find the spinner in the layout
        Spinner spinner = dialogView.findViewById(R.id.spinner);

        // Set up adapter for spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Build and show the dialog
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    // Get selected item and notify listener
                    String selected = (String) spinner.getSelectedItem();
                    if (listener != null) {
                        listener.onItemSelected(selected);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /**
     * Callback interface for receiving the selected string from the dropdown.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(String selectedItem);
    }
}
