package bdisfer1410.gymapp.util.media;

import android.content.Context;
import android.graphics.Color;

/**
 * Generates colors based on others.
 * <ol>
 *     <li>Load one color with {@code from{Source}()}</li>
 *     <li>Apply filters using {@code with{Filter}()}</li>
 *     <li>Get the final result with {@link ColorTools#get()}</li>
 * </ol>
 */
public class ColorTools {
    private int color;
    private int opacity;

    private ColorTools() {
        this.color = Color.TRANSPARENT;
    }

    public static ColorTools fromResId(Context context, int colorResId) {
        ColorTools builder = new ColorTools();
        builder.color = context.getResources().getColor(colorResId, null);
        return builder;
    }

    /**
     * @param opacity Must an hexadecimal integer. I.e. 0x80 = 50%
     */
    public ColorTools withOpacity(int opacity) {
        this.opacity = Math.max(0, Math.min(255, opacity));
        return this;
    }

    public int get() {
        return (opacity << 24) | (color & 0x00FFFFFF);
    }
}