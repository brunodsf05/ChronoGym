package bdisfer1410.gymapp.exercise.timer.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.io.Serializable;

import bdisfer1410.gymapp.R;

public class TimerFragment extends Fragment {
    //region Views
    private CircularProgressIndicator exerciseProgress;
    private ImageView exerciseIcon;
    private int exerciseIconResId = 0;
    private TextView exerciseCounter, exerciseName, setCounter;
    //endregion

    public TimerFragment() {
        super();
    }

    //region Initialization
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        exerciseProgress = view.findViewById(R.id.exerciseProgress);
        exerciseIcon = view.findViewById(R.id.exerciseIcon);
        exerciseCounter = view.findViewById(R.id.exerciseCounter);
        exerciseName = view.findViewById(R.id.exerciseName);
        setCounter = view.findViewById(R.id.setCounter);

        View containerView = view.findViewById(R.id.timerFragmentContainer);

        containerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                containerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int size = Math.min(containerView.getWidth(), containerView.getHeight());
                exerciseProgress.setIndicatorSize(size);
            }
        });
    }
    //endregion

    //region Visual API: Setters
    public void setExerciseProgressMax(int max) {
        exerciseProgress.setMax(max);
    }

    public void setExerciseProgressValue(int value) {
        exerciseProgress.setProgress(value);
    }

    public void setExerciseIconImage(int resId) {
        exerciseIconResId = resId;
        exerciseIcon.setImageResource(resId);
    }

    public void setExerciseCounterText(String text) {
        exerciseCounter.setText(text);
    }

    public void setExerciseCounterText(int resId) {
        exerciseCounter.setText(resId);
    }

    public void setExerciseNameText(String text) {
        exerciseName.setText(text);
    }

    public void setExerciseNameText(int resId) {
        exerciseName.setText(resId);
    }

    public void setSetCounterText(String text) {
        setCounter.setText(text);
    }

    public void setSetCounterText(int resId) {
        setCounter.setText(resId);
    }
    //endregion

    //region Visual API: Getters
    public String getExerciseNameText() {
        return this.exerciseName.getText().toString();
    }
    //endregion

    //region Serializable
    public TimerFragmentSerializable toSerializable() {
        TimerFragmentSerializable data = new TimerFragmentSerializable();
        data.exerciseProgressValue = exerciseProgress.getProgress();
        data.exerciseProgressMax = exerciseProgress.getMax();
        data.exerciseCounterText = exerciseCounter.getText().toString();
        data.exerciseNameText = exerciseName.getText().toString();
        data.setCounterText = setCounter.getText().toString();
        data.exerciseIconResId = exerciseIconResId;
        return data;
    }

    public void setDisplayedFromSerializable(TimerFragmentSerializable data) {
        if (data == null) return;
        exerciseProgress.setProgress(data.exerciseProgressValue);
        exerciseProgress.setMax(data.exerciseProgressMax);
        exerciseCounter.setText(data.exerciseCounterText);
        exerciseName.setText(data.exerciseNameText);
        setCounter.setText(data.setCounterText);
        setExerciseIconImage(data.exerciseIconResId);
    }

    public static class TimerFragmentSerializable implements Serializable {
        public int exerciseProgressValue;
        public int exerciseProgressMax;
        public int exerciseIconResId;
        public String exerciseCounterText;
        public String exerciseNameText;
        public String setCounterText;
    }
    //endregion
}
