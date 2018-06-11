package stogin.com.speeddrill;

import android.content.SharedPreferences;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class ShootingActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, ShotDetector.OnSoundUpdateListener {
    private final String TAG = "Shooting Activity";

    // Runs commands in a delayed manner
    Handler commandHandler;
    int commandCount;
    Set<String> commands;

    ShotDetector mShotDetector;

    StopwatchVew stopwatch;
    TextView currentAmplitude, maxAmplitude, shotTimes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting);

        commandHandler = new Handler();
        myTTS = new TextToSpeech(this, this);

        /* Get relevant arguments via SharedPreferences */
        SharedPreferences preferences = getSharedPreferences(getString(R.string.prefs_name), MODE_PRIVATE);

        commandCount = preferences.getInt(
                getString(R.string.prefs_command_count),
                getResources().getInteger(R.integer.default_command_count));
        commands = preferences.getStringSet(getString(R.string.prefs_commands),
                new HashSet<>(Collections.singletonList("No commands provided.")));

        /* Figure out delay */
        int min_delay = preferences.getInt(
                getString(R.string.prefs_delay_min),
                getResources().getInteger(R.integer.default_min_delay));
        int max_delay = preferences.getInt(
                getString(R.string.prefs_delay_max),
                getResources().getInteger(R.integer.default_max_delay));
        int delay_dif = max_delay-min_delay;


        int delay = new Random().nextInt(delay_dif) + min_delay;

        if (savedInstanceState == null)
            commandHandler.postDelayed(startCommandsRunnable, delay * 1000);

        this.currentAmplitude = findViewById(R.id.text_amplitude);
        this.maxAmplitude = findViewById(R.id.text_amplitude_max);
        this.shotTimes = findViewById(R.id.text_shot_times);
        this.stopwatch = findViewById(R.id.stopwatch);
    }

    private Runnable startCommandsRunnable = new Runnable() {
        @Override
        public void run() {
            for (int i=0; i<commandCount; i++) {
                int index = new Random().nextInt(commands.size());
                sayCommand((String) commands.toArray()[index]);
            }
            try {
                mShotDetector = new ShotDetector();
                mShotDetector.start(ShootingActivity.this, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
            stopwatch.start();
        }
    };

    public void onPauseStopwatch(View view) {
        stopwatch.pause();
    }

    /* ************************** TEXT TO SPEECH **************************************** */

    // Variables for speech
    private TextToSpeech myTTS;
    private boolean canSpeak = false;
    private final String COMMAND_UTTERANCE_ID = "stogin.com.speeddrill.CommandUtterance";

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.ERROR) {
            Log.e(TAG, "Error text to speech-ing");
            canSpeak = false;
        } else if (i == TextToSpeech.SUCCESS) {
            Log.v(TAG, "Successfully initialized text to speech");
            canSpeak = true;
        }
    }

    @Override
    protected void onDestroy() {
        myTTS.shutdown();
        canSpeak = false;
        if (mShotDetector != null) mShotDetector.stop();
        super.onDestroy();
    }

    private void sayCommand(String command) {
        myTTS.speak(command,
                TextToSpeech.QUEUE_ADD,
                new Bundle(),
                COMMAND_UTTERANCE_ID);
    }

    @Override
    public void shotDetected() {
        shotTimes.append("\n" +stopwatch.currentTime() );
    }

    @Override
    public void amplitudeUpdate(double current, double max) {
        this.currentAmplitude.setText(String.format("%.1f",current));
        this.maxAmplitude.setText(String.format("%.1f", max));
    }
}
