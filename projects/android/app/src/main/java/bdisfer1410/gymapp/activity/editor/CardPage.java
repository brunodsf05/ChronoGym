package bdisfer1410.gymapp.activity.editor;

import java.util.List;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;

public class CardPage {
    private final String title;
    private List<ExerciseCard> cards;
    private boolean enableReorder = false;

    public CardPage(String title, List<ExerciseCard> cards) {
        this.title = title;
        this.cards = cards;
    }

    public CardPage(String title, List<ExerciseCard> cards, boolean enableReorder) {
        this(title,cards);
        this.enableReorder = enableReorder;
    }

    public String getTitle() {
        return title;
    }

    public List<ExerciseCard> getCards() {
        return cards;
    }

    public boolean isReorderEnabled() {
        return enableReorder;
    }

    public void setCards(List<ExerciseCard> cards) {
        this.cards = cards;
    }
}
