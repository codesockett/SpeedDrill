package stogin.com.speeddrill;

import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ShootingActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private final String TAG = "Shooting Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting);

        myTTS = new TextToSpeech(this, this);

        new Handler().postDelayed(startCommandsRunnable, 5000);

    }

    private Runnable startCommandsRunnable = new Runnable() {
        @Override
        public void run() {
            sayCommand("This is a command!");
        }
    };


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
        super.onDestroy();
    }

    private void sayCommand(String command) {
        myTTS.speak(command,
                TextToSpeech.QUEUE_FLUSH,
                new Bundle(),
                COMMAND_UTTERANCE_ID);
    }
}
