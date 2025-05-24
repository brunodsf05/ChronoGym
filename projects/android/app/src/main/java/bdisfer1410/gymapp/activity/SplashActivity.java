package bdisfer1410.gymapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import bdisfer1410.gymapp.R;
import bdisfer1410.gymapp.util.media.Voice;

/**
 * Screen used to initialize background resources while displaying the app logo.
 * Serves as a temporary splash screen before launching the main content.
 */
@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_MIN_TIME_MS = 1500;
    private static final int SPLASH_TOTAL_TASK = 1;

    //region Splash manager
    private long splashStartTime;
    private int splashRemainingTasks = SPLASH_TOTAL_TASK;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);


        splashStartTime = System.currentTimeMillis();

        Voice.init(this, new Voice.OnInitListener() {
            @Override
            public void onInitSuccess() {
                Log.d("Init", "Voice initialized succesfully!");
                resourceLoaded("Voice");
            }

            @Override
            public void onInitFailure() {
                Log.d("Init", "Voice could not initialize!");
                Toast.makeText(SplashActivity.this, "Error al iniciar voz", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void goToNextActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Close this activity
    }

    /**
     * Marks a background resource as finished loading.
     *
     * This method is called each time a resource finishes loading during the splash screen.
     * It keeps track of how many resources remain, and once all are loaded,
     * it ensures that the splash screen remains visible for at least the minimum defined time
     * before proceeding to the next activity.
     *
     * @param name the name or identifier of the resource that has just finished loading

     */
    private void resourceLoaded(String name) {
        Log.d("Init", String.format("%s: loaded successfully!", name));
        splashRemainingTasks--;

        if (splashRemainingTasks > 0) return;

        // If every task finished earlier, we ensure the minimum splash time
        long elapsed = System.currentTimeMillis() - splashStartTime;
        long remaining = SPLASH_MIN_TIME_MS - elapsed;
        long delay = (remaining > 0) ? remaining : 0;

        new Handler(Looper.getMainLooper()).postDelayed(this::goToNextActivity, delay);
    }
}
