package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class TextInputDialog {

    public interface TextInputCallback {
        void onTextEntered(@NonNull String text);
    }

    public static void show(@NonNull Context context,
                            @NonNull String title,
                            String initialText,
                            @NonNull TextInputCallback callback) {

        final EditText input = new EditText(context);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(initialText != null ? initialText : "");
        input.setSelection(input.getText().length()); // Cursor al final

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(input)
                .setNegativeButton("Cancelar", null)
                .setPositiveButton("Aceptar", (dialog, which) -> {
                    String text = input.getText().toString();
                    callback.onTextEntered(text);
                })
                .show();
    }
}
