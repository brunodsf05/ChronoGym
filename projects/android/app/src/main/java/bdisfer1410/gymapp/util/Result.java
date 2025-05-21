package bdisfer1410.gymapp.util;

import androidx.annotation.NonNull;

/**
 * A generic class that represents the result of an operation, which can either be a success (Ok)
 * containing a value of type T, or a failure (Err) containing an error of type E.
 *
 * @param <T> The type of the value in case of success.
 * @param <E> The type of the error in case of failure.
 */
public class Result<T, E> {
    private final T value;
    private final E error;
    private final boolean isOk;

    private Result(T value, E error, boolean isOk) {
        this.value = value;
        this.error = error;
        this.isOk = isOk;
    }

    public static <T, E> Result<T, E> ok(T value) {
        return new Result<>(value, null, true);
    }

    public static <T, E> Result<T, E> err(E error) {
        return new Result<>(null, error, false);
    }

    public boolean isOk() {
        return isOk;
    }

    public boolean isErr() {
        return !isOk;
    }

    public T getValue() {
        return isOk ? value : null;
    }

    public E getError() {
        return isOk ? null : error;
    }

    @NonNull
    @Override
    public String toString() {
        return isOk
                ? String.format("Ok(%s)", value)
                : String.format("Err(%s)", error);
    }
}
