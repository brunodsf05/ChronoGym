package bdisfer1410.gymapp.exercise.card;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import bdisfer1410.gymapp.R;

/**
 * Adapter for displaying a list of ExerciseCard items.
 */
public class ExerciseCardAdapter extends RecyclerView.Adapter<ExerciseCardAdapter.ViewHolder> {

    private final List<ExerciseCard> items;
    private final Consumer<ExerciseCard> onClick;
    private final BiConsumer<View, Integer> onLongClick; // May be null if no long click needed
    private Consumer<Integer> retrieveLastPos = null;

    /**
     * Constructor.
     * @param items List of ExerciseCard objects to display.
     * @param onClick Callback for item click.
     * @param onLongClick Callback for item long click, or null if not used.
     */
    public ExerciseCardAdapter(List<ExerciseCard> items, Consumer<ExerciseCard> onClick, BiConsumer<View, Integer> onLongClick) {
        this.items = items;
        this.onClick = onClick;
        this.onLongClick = onLongClick;
    }

    public ExerciseCardAdapter(List<ExerciseCard> items, Consumer<ExerciseCard> onClick, BiConsumer<View, Integer> onLongClick, Consumer<Integer> retrieveLastPos) {
        this.items = items;
        this.onClick = onClick;
        this.onLongClick = onLongClick;
        this.retrieveLastPos = retrieveLastPos;
    }

    @NonNull
    @Override
    public ExerciseCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_exercise, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseCardAdapter.ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    /**
     * Removes an item from the list and notifies adapter.
     * @param position Position of item to remove.
     */
    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public void bind(ExerciseCard card) {
            itemView.setOnClickListener(v -> {
                onClick.accept(card);
                if (retrieveLastPos != null)
                    retrieveLastPos.accept(getAdapterPosition());
            });

            if (onLongClick != null) {
                itemView.setOnLongClickListener(v -> {
                    onLongClick.accept(v, getAdapterPosition());
                    return true;
                });
            }
            else {
                itemView.setOnLongClickListener(null);
            }
            // TODO: Bind your views with card data here
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

        }
    }
}
