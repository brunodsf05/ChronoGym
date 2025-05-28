package bdisfer1410.gymapp.activity.editor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bdisfer1410.gymapp.R;

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
                context.getString(R.string.activity_editor_dialog_pose_title),
                "",
                false,
                0,
                defaultId,
                defaultName,
                defaultIconResId,
                defaultNumber,
                blackListIds,
                listener
        );
    }
}
