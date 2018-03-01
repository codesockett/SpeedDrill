package stogin.com.speeddrill;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {

    private final String TAG = "MainActivity";
    private TextToSpeech myTTS;
    private CommandListAdapter commandListAdapter = new CommandListAdapter();
    private boolean canSpeak = false;
    private int currentFragment = 0;
    android.support.v4.app.FragmentManager fragmentManager;
    OptionsFragment optionsFragment;
    CommandListFragment commandListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myTTS = new TextToSpeech(this, this);

        fragmentManager = getSupportFragmentManager();

        commandListFragment = CommandListFragment.newInstance("","");
        optionsFragment = OptionsFragment.newInstance("", "");

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragment_container, optionsFragment);
        transaction.commit();

    }

    @Override
    protected void onDestroy() {
        myTTS.shutdown();
        canSpeak = false;
        super.onDestroy();
    }

    public void onStartClicked(View view) {
        if (canSpeak) {
            myTTS.speak("Start button clicked.", TextToSpeech.QUEUE_ADD, new Bundle(), "StartUtterance");
        }
    }

    public void onToggleOptionsClicked(View view) {
        FragmentTransaction transaction;
        // Toggle fragment
        if (currentFragment == 0) {
            transaction = fragmentManager.beginTransaction();
            transaction.remove(optionsFragment);
            transaction.add(R.id.fragment_container, commandListFragment);
            transaction.commit();

        } else {
            transaction = fragmentManager.beginTransaction();
            transaction.remove(commandListFragment);
            transaction.add(R.id.fragment_container, optionsFragment);
            transaction.commit();
        }

        currentFragment = ~currentFragment;
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.ERROR) {
            Log.e(TAG, "Error text to speech-ing");
        } else if (i == TextToSpeech.SUCCESS) {
            Log.v(TAG, "Successfully initialized text to speech");
            canSpeak = true;
            myTTS.speak("Hello, World!", TextToSpeech.QUEUE_FLUSH, new Bundle(), "HelloWorldUtterance");
        }
    }
}


