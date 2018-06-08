package stogin.com.speeddrill;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by William on 3/1/2018.
 */

public class CommandListAdapter implements ListAdapter {
    private List<String> options = new LinkedList<>();
    private List<DataSetObserver> observers = new LinkedList<>();
    private SharedPreferences preferences;
    private Context context;

    public CommandListAdapter(Context context, SharedPreferences preferences) {
        super();

        this.context = context;
        this.preferences = preferences;


        options.addAll(
                preferences.getStringSet(context.getString(R.string.prefs_commands), new HashSet<String>())
        );
    }

    public void addItem(String item) {
        if (!options.contains(item)) {
            options.add(item);
            notifyDatasetChanged();

            preferences
                    .edit().putStringSet(
                    context.getString(R.string.prefs_commands),
                    new HashSet<>(options)
            ).apply();
        }
    }

    public void removeItem(String item) {
        if (options.remove(item)) {
            notifyDatasetChanged();

            preferences
                    .edit().putStringSet(
                    context.getString(R.string.prefs_commands),
                    new HashSet<>(options)
            ).apply();
        }
    }

    private void notifyDatasetChanged() {
        for (DataSetObserver observer : observers)
            observer.onChanged();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        observers.add(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        observers.remove(dataSetObserver);
    }

    @Override
    public int getCount() {
        return options.size();
    }

    @Override
    public String getItem(int i) {
        return options.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.command_item, viewGroup, false);
        }

        ((TextView) view.findViewById(R.id.text_command)).setText(options.get(i));

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return options.size() == 0;
    }
}
