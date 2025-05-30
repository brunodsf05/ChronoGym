package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

import bdisfer1410.gymapp.R;

public class TextDropdownDialog {

    /**
     * Shows a dialog with a dropdown (spinner) and an optional number input.
     *
     * @param context The context in which the dialog should be displayed.
     * @param title The title of the dialog.
     * @param options Array of string options to display in the dropdown.
     * @param withNumberInput If true, show a number input field for positive integers.
     * @param listener Callback to handle the selected string and optional number.
     */
    public static void show(Context context, String title, List<String> options, int numberHint, boolean withNumberInput,
                            OnItemSelectedListener listener) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_with_spinner, null);

        Spinner spinner = dialogView.findViewById(R.id.spinner);
        EditText numberInput = dialogView.findViewById(R.id.number_input);
        if (numberHint > 0) numberInput.setHint(numberHint);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, options);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Show or hide the number input based on the flag
        numberInput.setVisibility(withNumberInput ? View.VISIBLE : View.GONE);

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(dialogView)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    String selected = (String) spinner.getSelectedItem();
                    int number = 0;

                    if (withNumberInput) {
                        String input = numberInput.getText().toString().trim();
                        if (!input.isEmpty()) {
                            try {
                                int value = Integer.parseInt(input);
                                if (value >= 0) {
                                    number = value;
                                }
                            }
                            catch (NumberFormatException ignored) {}
                        }
                    }

                    if (listener != null) {
                        listener.onItemSelected(selected, number);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    /**
     * Callback interface for receiving the selected string and optional number.
     */
    public interface OnItemSelectedListener {
        void onItemSelected(String selectedItem, Integer numberInput);
    }
}
