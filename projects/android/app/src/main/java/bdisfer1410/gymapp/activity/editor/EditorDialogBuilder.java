package bdisfer1410.gymapp.activity.editor;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import bdisfer1410.gymapp.R;

public class EditorDialogBuilder {
    //region
    public static void pose(Context context, @NonNull List<String> blackListIds, EditorDialog.OnFormSubmittedListener listener) {
        pose(context, blackListIds, null, null, -1, listener);
    }

    public static void pose(
            Context context,
            @NonNull List<String> blackListIds,
            @Nullable String defaultId,
            @Nullable String defaultName,
            int defaultIconResId,
            EditorDialog.OnFormSubmittedListener listener
    ) {
        EditorDialog.showEditorDialog(
                context,
                context.getString(R.string.activity_editor_dialog_pose_title),
                "",
                true,
                false,
                true,
                0,
                defaultId,
                defaultName,
                defaultIconResId,
                0,
                blackListIds,
                listener
        );
    }
    //endregion

    //region: Transitions
    public static void transitions(Context context, @NonNull List<String> blackListIds, EditorDialog.OnFormSubmittedListener listener) {
        transitions(context, blackListIds, null, null, listener);
    }

    public static void transitions(
            Context context,
            @NonNull List<String> blackListIds,
            @Nullable String defaultId,
            @Nullable String defaultName,
            EditorDialog.OnFormSubmittedListener listener
    ) {
        EditorDialog.showEditorDialog(
                context,
                context.getString(R.string.activity_editor_dialog_transition_list_title),
                "",
                true,
                false,
                false,
                0,
                defaultId,
                defaultName,
                -1,
                0,
                blackListIds,
                listener
        );
    }
    //endregion

    //region Set: Rest
    public static void setRest(Context context, @NonNull List<String> blackListIds, EditorDialog.OnFormSubmittedListener listener) {
        setRest(context, blackListIds, null, 0, listener);
    }

    public static void setRest(
            Context context,
            @NonNull List<String> blackListIds,
            @Nullable String defaultId,
            int defaultNumber,
            EditorDialog.OnFormSubmittedListener listener
    ) {
        EditorDialog.showEditorDialog(
                context,
                context.getString(R.string.activity_editor_dialog_set_rest_title),
                context.getString(R.string.activity_editor_dialog_set_rest_label_time),
                false,
                true,
                false,
                0,
                defaultId,
                "",
                -1,
                defaultNumber,
                blackListIds,
                listener
        );
    }
    //endregion
}
