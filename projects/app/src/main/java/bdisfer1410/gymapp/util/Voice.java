package bdisfer1410.gymapp.util;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import java.util.Locale;

public class Voice {

    public interface OnInitListener {
        void onInitSuccess();
        void onInitFailure();
    }

    private static Voice instance;
    private TextToSpeech tts;
    private boolean isInitialized = false;

    private Voice(Context context, OnInitListener listener) {
        tts = new TextToSpeech(context.getApplicationContext(), status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(new Locale("es", "ES"));
                isInitialized = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED;
                if (isInitialized) {
                    listener.onInitSuccess();
                } else {
                    listener.onInitFailure();
                }
            } else {
                listener.onInitFailure();
            }
        });
    }

    public static void init(Context context, OnInitListener listener) {
        if (instance == null) {
            instance = new Voice(context, listener);
        } else {
            if (instance.isInitialized) {
                listener.onInitSuccess();
            } else {
                listener.onInitFailure();
            }
        }
    }

    public static Voice get() {
        return instance;
    }

    public void say(String text) {
        if (isInitialized) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void shutdown() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            isInitialized = false;
            instance = null;
        }
    }
}
