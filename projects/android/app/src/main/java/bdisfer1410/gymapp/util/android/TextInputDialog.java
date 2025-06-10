package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import bdisfer1410.gymapp.R;

public class TextInputDialog {

    public interface TextInputCallback {
        void onTextEntered(@NonNull String text);
    }

    public static void show(@NonNull Context context,
                            @NonNull String title,
                            @NonNull String hint,
                            String initialText,
                            @NonNull TextInputCallback callback) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_text_input, null);
        TextInputLayout inputLayout = view.findViewById(R.id.inputLayout);
        TextInputEditText inputEditText = view.findViewById(R.id.editText);

        inputLayout.setHint(hint);
        inputEditText.setText(initialText != null ? initialText : "");
        inputEditText.setSelection(Objects.requireNonNull(inputEditText.getText()).length());

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(view)
                .setNegativeButton(R.string.activity_any_deny, null)
                .setPositiveButton(R.string.activity_any_accept, (dialog, which) -> {
                    String text = inputEditText.getText() != null ? inputEditText.getText().toString() : "";
                    callback.onTextEntered(text);
                })
                .show();
    }
}
