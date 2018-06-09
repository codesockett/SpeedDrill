package stogin.com.speeddrill;
//import com.stogin.example.test.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

;

/**
 * Instrumented tests that test functionality of {@link stogin.com.speeddrill.CommandListAdapter}
 */
@RunWith(AndroidJUnit4.class)
public class CommandListAdapterTests {

    private Context appContextFactory() {
        return InstrumentationRegistry.getTargetContext();
    }

    /**
     * Creates shared preferences that are empty for use in testing.
     */
    private SharedPreferences testPreferencesFactory() {
        Context appContext = appContextFactory();
        final String testSharedPrefsName = appContext.getPackageName() + "testsharedprefs";


        SharedPreferences preferences = appContext.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        return preferences;
    }

    private CommandListAdapter commandListAdapterFactory() {
        return new CommandListAdapter(appContextFactory(), testPreferencesFactory());
    }

    @Test
    public void addCommandModifiesSharedPreferences() {
        CommandListAdapter listAdapter = commandListAdapterFactory();
        SharedPreferences prefs = testPreferencesFactory();
        Context appContext = appContextFactory();

        Set<String> commands =
                prefs.getStringSet(appContext.getString(R.string.prefs_commands), new HashSet<String>());


        assertEquals(0, commands.size());
        listAdapter.addItem("Hello");
        listAdapter.addItem("World");

        commands =
                prefs.getStringSet(appContext.getString(R.string.prefs_commands), new HashSet<String>());
        assertEquals(
                2,
                commands.size()
        );

        listAdapter.removeItem("Hello");

        commands =
                prefs.getStringSet(appContext.getString(R.string.prefs_commands), new HashSet<String>());
        assertEquals(
                1,
                commands.size()
        );

        prefs.getStringSet(appContext.getString(R.string.prefs_commands), new HashSet<String>());
    }

    /**
     * DataSetObserver that keeps track of whether it was notified.
     */
    private class TestObserver extends DataSetObserver {
        private boolean wasNotified = false;

        @Override
        public void onChanged() {
            wasNotified = true;
        }

        /**
         * Resets the observer to thinking it has not yet been notified.
         */
        public void reset() {
            wasNotified = false;
        }

        /**
         * @return whether was notified since last reset.
         */
        public boolean wasNotified() {
            return wasNotified;
        }
    }

    @Test
    public void adapterNotifiesListenersWhenModified() {
        CommandListAdapter adapter = commandListAdapterFactory();

        TestObserver testObserver = new TestObserver();
        adapter.registerDataSetObserver(testObserver);

        adapter.addItem("Hello World");
        Assert.assertTrue(testObserver.wasNotified());

        testObserver.reset();

        adapter.removeItem("Hello World");
        Assert.assertTrue(testObserver.wasNotified());
    }

    @Test
    public void adapterCreatesViewsWhoseDeleteButtonsHaveTags() {
        CommandListAdapter adapter = commandListAdapterFactory();

        final String itemId = "Test item";

        adapter.addItem(itemId);
        View view = adapter.getView(0, null, null);

        Assert.assertEquals(
                view.findViewById(R.id.button_delete_command).getTag(),
                itemId);

    }

}
