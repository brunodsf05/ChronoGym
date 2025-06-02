package bdisfer1410.gymapp.exercise.card;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.google.android.material.card.MaterialCardView;

import java.util.stream.Stream;

import bdisfer1410.gymapp.R;

public enum ExerciseCardState {
    NORMAL,
    SELECTED,
    DISABLED;

    // Método para aplicar colores según el estado
    public void applyStyleToMaterialCardView(MaterialCardView cardView) {
        Context context = cardView.getContext();

        int bg, ou, fg, hg;

        switch (this) {
            case SELECTED:
                bg = R.color.cardSelectedBg;
                ou = R.color.cardSelectedOu;
                fg = R.color.cardSelectedFg;
                hg = R.color.cardSelectedHg;
                break;

            case DISABLED:
                bg = R.color.cardDisabledBg;
                ou = R.color.cardDisabledOu;
                fg = R.color.cardDisabledFg;
                hg = R.color.cardDisabledHg;
                break;

            default:
                bg = R.color.cardNormalBg;
                ou = R.color.cardNormalOu;
                fg = R.color.cardNormalFg;
                hg = R.color.cardNormalHg;
        }

        cardView.setCardBackgroundColor(ContextCompat.getColor(context, bg));
        cardView.setStrokeColor(ContextCompat.getColor(context, ou));
        ((ImageView)cardView.findViewById(R.id.icon)).setColorFilter(ContextCompat.getColor(context, fg));

        Stream.of(R.id.name, R.id.interval).forEach(textViewId -> updateTextColor(cardView, textViewId, fg));
        Stream.of(R.id.tags, R.id.extra).forEach(textViewId -> updateTextColor(cardView, textViewId, hg));
    }

    private void updateTextColor(MaterialCardView cardView, int textViewId, int colorId) {
        TextView tv = cardView.findViewById(textViewId);
        if (tv != null) tv.setTextColor(ContextCompat.getColor(cardView.getContext(), colorId));
    }
}
