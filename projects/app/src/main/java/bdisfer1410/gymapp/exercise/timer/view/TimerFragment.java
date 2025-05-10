package bdisfer1410.gymapp.exercise.timer.view;

import android.content.Context;
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

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.util.OnFragmentReadyListener;

public class TimerFragment extends Fragment {
    //region Views
    private CircularProgressIndicator exerciseProgress;
    private ImageView exerciseIcon;
    private TextView exerciseCounter, exerciseName, setCounter;
    //endregion

    private OnFragmentReadyListener listener;

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

        // Adjust the size dynamically when this view is ready
        containerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                containerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int size = Math.min(containerView.getWidth(), containerView.getHeight());
                exerciseProgress.setIndicatorSize(size);
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentReadyListener) {
            listener = (OnFragmentReadyListener) context;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (listener != null) {
            listener.onFragmentReady();
        }
    }
    //endregion

    //region API: Basic
    public void setExerciseProgressMax(int max) {
        exerciseProgress.setMax(max);
    }

    public void setExerciseProgressValue(int value) {
        exerciseProgress.setProgress(value);
    }

    public void setExerciseIconImage(int resId) {
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
}