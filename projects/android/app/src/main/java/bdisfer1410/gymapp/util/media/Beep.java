package bdisfer1410.gymapp.util.media;

import android.media.AudioManager;
import android.media.ToneGenerator;

import java.util.Map;

/**
 * Just plays beeps.
 */
public class Beep {
    public enum Type {
        NORMAL,
        HIGH,
        WARNING,
    }

    private static final ToneGenerator toneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    private static final Map<Type, Integer> tonesByType = Map.of(
            Type.NORMAL, ToneGenerator.TONE_PROP_BEEP,
            Type.HIGH, ToneGenerator.TONE_CDMA_PIP,
            Type.WARNING, ToneGenerator.TONE_CDMA_ALERT_AUTOREDIAL_LITE
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
