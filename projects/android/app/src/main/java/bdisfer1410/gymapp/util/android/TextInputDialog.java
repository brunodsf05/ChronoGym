package bdisfer1410.gymapp.util.android;

import android.content.Context;
import android.text.InputType;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import bdisfer1410.gymapp.R;

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
        input.setSelection(input.getText().length());

        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setView(input)
                .setNegativeButton(R.string.activity_any_deny, null)
                .setPositiveButton(R.string.activity_any_accept, (dialog, which) -> {
                    String text = input.getText().toString();
                    callback.onTextEntered(text);
                })
                .show();
    }
}
