package bdisfer1410.gymapp.exercise.card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import bdisfer1410.gymapp.R;

/**
 * Adapter class for displaying a list of exercise cards in a RecyclerView.
 * Each item in the list must implement the {@link ExerciseCard} interface.
 */
public class ExerciseCardAdapter extends RecyclerView.Adapter<ExerciseCardAdapter.ViewHolder> {

    private final List<ExerciseCard> cards;
    private final OnItemClickListener listener;

    /**
     * Interface for handling card click events.
     */
    public interface OnItemClickListener {
        /**
         * Called when a card is clicked.
         *
         * @param card The clicked {@link ExerciseCard} item.
         */
        void onItemClick(ExerciseCard card);
    }

    /**
     * Adapter constructor.
     *
     * @param cards    List of items implementing {@link ExerciseCard}.
     * @param listener Listener for handling item click events.
     */
    public ExerciseCardAdapter(List<ExerciseCard> cards, OnItemClickListener listener) {
        this.cards = cards;
        this.listener = listener;
    }

    /**
     * ViewHolder class for exercise cards.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, tags, interval, extra;
        ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(bdisfer1410.gymapp.R.id.name);
            tags = itemView.findViewById(R.id.tags);
            interval = itemView.findViewById(R.id.interval);
            extra = itemView.findViewById(R.id.extra);
            icon = itemView.findViewById(R.id.icon);
        }

        /**
         * Binds the {@link ExerciseCard} data to the views in the layout.
         *
         * @param card     The card data to bind.
         * @param listener Click listener.
         */
        public void bind(final ExerciseCard card, final OnItemClickListener listener) {
            name.setText(card.getCardName());
            interval.setText(card.getCardInterval());

            // Handle tags visibility
            if (card.getCardTags() == null) {
                tags.setVisibility(View.GONE);
            }
            else {
                tags.setVisibility(View.VISIBLE);
                tags.setText(card.getCardTags());
            }

            // Handle extra info visibility
            if (card.getCardExtra() == null) {
                extra.setVisibility(View.GONE);
            }
            else {
                extra.setVisibility(View.VISIBLE);
                extra.setText(card.getCardExtra());
            }

            // Handle icon drawable resource
            icon.setImageResource(
                    Objects.requireNonNullElse(card.getCardIcon(), R.drawable.ic_exercise_default)
            );

            itemView.setOnClickListener(v -> listener.onItemClick(card));
        }
    }

    @NonNull
    @Override
    public ExerciseCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_exercise, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseCardAdapter.ViewHolder holder, int position) {
        holder.bind(cards.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    /**
     * Swaps two items in the list. Useful for reordering.
     *
     * @param fromPosition Index of the item being moved.
     * @param toPosition   Index of the target position.
     */
    public void swapItems(int fromPosition, int toPosition) {
        Collections.swap(cards, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }
}
