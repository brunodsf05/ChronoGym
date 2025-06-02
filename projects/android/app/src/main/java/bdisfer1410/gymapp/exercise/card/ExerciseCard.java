package bdisfer1410.gymapp.exercise.card;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface that defines the data needed to display an exercise card in the RecyclerView.
 */
public interface ExerciseCard {

    /**
     * Returns the resource ID of the card icon.
     *
     * @return Drawable resource ID (e.g., R.drawable.icon), or null if no icon should be shown.
     */
    @Nullable Integer getCardIcon();

    /**
     * Returns the title or main name displayed on the card.
     *
     * @return Name of the exercise or element represented by the card.
     */
    @NonNull String getCardName();

    /**
     * Returns the associated tags or categories.
     *
     * @return Tags as a string, or null if the tag TextView should be hidden.
     */
    @Nullable String getCardTags();

    /**
     * Returns the interval string shown on the card.
     *
     * @return Interval (e.g., "30s", "3x10") as a string.
     */
    @NonNull String getCardInterval();

    /**
     * Returns additional information, if any.
     *
     * @return Extra text as a string, or null if the extra TextView should be hidden.
     */
    @Nullable String getCardExtra();

    /**
     * Applies some style to the card.
     *
     * @return An enum describing the state of the card.
     */
    @NonNull ExerciseCardState getCardState();
}
