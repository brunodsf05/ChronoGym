package bdisfer1410.gymapp.util.java;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Identifiable implements Serializable {
    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrettierId() {
        return String.format("#%s", id);
    }

    public static List<String> getIds(@NonNull List<Identifiable> identifiables) {
        return identifiables.stream().map(Identifiable::getId).collect(Collectors.toList());
    }

    public Identifiable withId(String id) {
        this.id = id;
        return this;
    }
}
