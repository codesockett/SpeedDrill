package stogin.com.speeddrill;
//import com.stogin.example.test.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("stogin.com.speeddrill", appContext.getPackageName());
    }

    @Test
    public void addCommandModifiesSharedPreferences() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        final String testSharedPrefsName = appContext.getPackageName() + "testsharedprefs";


        SharedPreferences prefs = appContext.getSharedPreferences(testSharedPrefsName, Context.MODE_PRIVATE);
        CommandListAdapter listAdapter = new CommandListAdapter(appContext, prefs);

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


}
