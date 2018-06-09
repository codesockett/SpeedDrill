package stogin.com.speeddrill;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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


    }

    public void onStartClicked(View view) {
        Intent startShootingIntent = new Intent(this, ShootingActivity.class);

        startActivity(startShootingIntent);
    }


}


