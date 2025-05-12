package bdisfer1410.gymapp.util;

/**
 * Stores the pair: Value, Max.
 */
public class Counter {
    public int value, max;

    public Counter(int max) {
        this.max = max;
        this.value = 0;
    }

    public void add(int addedToValue) {
        this.value += addedToValue;
    }
}
