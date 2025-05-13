package bdisfer1410.gymapp.util;

import android.media.AudioManager;
import android.media.ToneGenerator;

import java.util.Map;

/**
 * Just plays beeps.
 */
public class BeepGenerator {
    public enum Type {
        NORMAL,
    }

    private static final ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private static final Map<Type, Integer> tonesByType = Map.of(
            Type.NORMAL, ToneGenerator.TONE_PROP_BEEP
    );

    /**
     * Plays a beep sound based on {@link Type}.
     * @param type Selects the kind of beep to emit.
     */
    public static void emit(Type type) {
        Integer toneValue = tonesByType.get(type);
        int tone = toneValue != null ? toneValue : ToneGenerator.TONE_PROP_BEEP;

        toneGen.startTone(tone);
    }
}
