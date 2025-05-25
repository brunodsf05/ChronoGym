package bdisfer1410.gymapp.util.java;

import java.io.Serializable;

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
}
