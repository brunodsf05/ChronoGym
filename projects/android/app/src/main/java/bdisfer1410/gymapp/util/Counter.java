package bdisfer1410.gymapp.util;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;

import java.io.Serializable;

/**
 * Stores the pair: Value, Max.
 */
public class Counter implements Serializable {
    public int value, max;

    public Counter(int max) {
        this.max = max;
        this.value = 0;
    }

    public void add(int addedToValue) {
        this.value += addedToValue;
    }

    @Override
    @NonNull
    @SuppressLint("DefaultLocale")
    public String toString() {
        return String.format("%d/%d", value, max);
    }
}
