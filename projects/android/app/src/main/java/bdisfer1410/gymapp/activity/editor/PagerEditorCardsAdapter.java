package bdisfer1410.gymapp.activity.editor;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.exercise.card.ExerciseCard;
import bdisfer1410.gymapp.exercise.card.ExerciseCardAdapter;

public class PagerEditorCardsAdapter extends RecyclerView.Adapter<PagerEditorCardsAdapter.PageViewHolder> {

    private List<CardPage> pages;
    private final Consumer<ExerciseCard> onClick;

    public PagerEditorCardsAdapter(
            List<CardPage> pages,
            Consumer<ExerciseCard> onClick
    ) {
        this.pages = pages;
        this.onClick = onClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPages(List<CardPage> pages) {
        this.pages = pages;
        notifyDataSetChanged();
    }

    public List<CardPage> getPages() {
        return this.pages;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PageViewHolder(
                LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.page_editor_card_exercise_list, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        CardPage page = pages.get(position);
        ExerciseCardAdapter adapter = new ExerciseCardAdapter(page.getCards(), onClick, null);
        holder.title.setText(page.getTitle());
        holder.list.setLayoutManager(new LinearLayoutManager(holder.list.getContext()));
        holder.list.setAdapter(adapter);

        if (!page.isReorderEnabled()) return;

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                int from = viewHolder.getAdapterPosition();
                int to = target.getAdapterPosition();
                if (from < 0 || to < 0 || from == to) return false;
                List<ExerciseCard> cards = page.getCards();
                ExerciseCard moved = cards.remove(from);
                cards.add(to, moved);
                adapter.notifyItemMoved(from, to);
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // No swipe actions
            }
        });

        helper.attachToRecyclerView(holder.list);
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    static class PageViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public RecyclerView list;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            list = itemView.findViewById(R.id.list);
        }
    }
}
