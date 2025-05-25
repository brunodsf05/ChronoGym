package bdisfer1410.gymapp.activity.editor;

import java.util.List;

import bdisfer1410.gymapp.exercise.card.ExerciseCard;

public class CardPage {
    private final String name;
    private List<ExerciseCard> cards;

    public CardPage(String name, List<ExerciseCard> cards) {
        this.name = name;
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public List<ExerciseCard> getCards() {
        return cards;
    }

    public void setCards(List<ExerciseCard> cards) {
        this.cards = cards;
    }
}
