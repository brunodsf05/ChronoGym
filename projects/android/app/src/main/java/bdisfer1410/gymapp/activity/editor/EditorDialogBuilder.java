package bdisfer1410.gymapp.activity.editor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EditorDialogBuilder {
    public static void pose(Context context, @NonNull List<String> blackListIds, EditorDialog.OnFormSubmittedListener listener) {
        pose(context, blackListIds, null, null, -1, null, listener);
    }

    public static void pose(
            Context context,
            @NonNull List<String> blackListIds,
            @Nullable String defaultId,
            @Nullable String defaultName,
            int defaultIconResId,
            @Nullable Integer defaultNumber,
            EditorDialog.OnFormSubmittedListener listener
    ) {
        EditorDialog.showEditorDialog(
                context,
                "ID",
                "Name",
                "Number",
                "Icon",
                true,
                0,
                null,
                null,
                -1,
                null,
                blackListIds,
                listener
        );
    }
}
