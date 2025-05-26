package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class NumberInputDialog {

    public interface NumberInputCallback {
        void onNumberEntered(int number);
    }

    public static void show(@NonNull Context context,
                            @NonNull String title,
                            Integer initialValue,
                            @NonNull NumberInputCallback callback) {

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setText(initialValue != null ? String.valueOf(initialValue) : "");
        input.setSelection(input.getText().length());

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(input)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    try {
                        int value = Integer.parseInt(input.getText().toString());
                        callback.onNumberEntered(value);
                    } catch (NumberFormatException e) {
                        input.setError("Introduce un número válido");
                    }
                })
                .show();
    }
}
