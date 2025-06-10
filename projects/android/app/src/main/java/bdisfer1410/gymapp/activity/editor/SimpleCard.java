package bdisfer1410.gymapp.activity.editor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardState;
import bdisfer1410.gymapp.util.java.Identifiable;

/**
 * Simple card with only icon, text and subtext in order to create buttons inside a page.
 * It comes bundled with {@link Identifiable} so it is that.
 * The advantage of creating buttons with this, is that you can avoid fragments.
 */
public class SimpleCard extends Identifiable implements ExerciseCard {

    @Nullable
    protected Integer icon;

    @NonNull
    protected String text;

    @Nullable
    protected String subtext;

    public SimpleCard(@NonNull String id, @Nullable Integer icon, @NonNull String text, @Nullable String subtext) {
        this.id = id;
        this.icon = icon;
        this.text = text;
        this.subtext = subtext;
    }

    public void setText(@NonNull String text) {
        this.text = text;
    }

    public void setIcon(@NonNull Integer icon) {
        this.icon = icon;
    }

    public void setSubtext(@Nullable String subtext) {
        this.subtext = subtext;
    }

    //region ExerciseCard
    @Nullable
    @Override
    public Integer getCardIcon() {
        return this.icon;
    }

    @NonNull
    @Override
    public String getCardName() {
        return this.text;
    }

    @Nullable
    @Override
    public String getCardTags() {
        return this.subtext;
    }

    @NonNull
    @Override
    public String getCardInterval() {
        return "";
    }

    @Nullable
    @Override
    public String getCardExtra() {
        return "";
    }

    @NonNull
    @Override
    public ExerciseCardState getCardState() {
        return ExerciseCardState.NORMAL;
    }
    //endregion
}
