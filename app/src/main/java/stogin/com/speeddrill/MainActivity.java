package stogin.com.speeddrill;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    android.support.v4.app.FragmentManager fragmentManager;
    OptionsFragment optionsFragment;
    CommandListFragment commandListFragment;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        commandListFragment = CommandListFragment.newInstance("", "");
        optionsFragment = OptionsFragment.newInstance("", "");

        mViewPager = findViewById(R.id.pager_fragment);
        mViewPager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        checkPermissions();

    }

    public void onStartClicked(View view) {
        Intent startShootingIntent = new Intent(this, ShootingActivity.class);

        startActivity(startShootingIntent);
    }


    private static final int PERMISSIONS_ALL = 1;
    private void checkPermissions() {
        // Check android permissions
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    PERMISSIONS_ALL
            );
        }
    }

}


