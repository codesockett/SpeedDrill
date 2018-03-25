package stogin.com.speeddrill;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

/**
 * Created by William on 3/25/2018.
 */

public class CustomPagerAdapter extends FragmentPagerAdapter {
    private static final String TAG = "CustomPagerAdapter";

    private CommandListFragment commandListFragment;
    private OptionsFragment optionsFragment;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (optionsFragment == null)
                    optionsFragment = OptionsFragment.newInstance("TOOD", "TODO");
                return optionsFragment;
            case 1:
                if (commandListFragment == null)
                    commandListFragment = CommandListFragment.newInstance("TODO", "TODO");
                return commandListFragment;
            default:
                Log.e(TAG, "GeatItem on non-existant index");
                throw new UnsupportedOperationException();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Options";
            case 1:
                return "Command List";
            default:
                return "Unknown";
        }
    }
}
