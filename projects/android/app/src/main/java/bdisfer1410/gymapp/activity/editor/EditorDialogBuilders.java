package bdisfer1410.gymapp.activity.editor;

import android.content.Context;

import androidx.annotation.Nullable;

public class EditorDialogBuilders {
    public static void pose(
            Context context,
            @Nullable String defaultId,
            @Nullable String defaultName,
            int defaultIconResId,
            @Nullable Integer defaultNumber,
            EditorDialog.OnFormSubmittedListener listener
    ) {
        EditorDialog.showEditorDialog(
                context,                           // Context
                "ID",                              // Label for ID field
                "Name",                            // Label for Name field
                "Icon",                            // Label for Icon spinner
                "Number",                          // Label for Number field
                false,                             // Don't show number field
                null,                              // Default ID
                null,                              // Default Name
                -1,                                // Default Icon
                null,                              // Default Number
                listener
        );
    }
}
