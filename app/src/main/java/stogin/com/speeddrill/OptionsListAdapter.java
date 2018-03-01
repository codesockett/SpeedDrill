package stogin.com.speeddrill;

import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by William on 3/1/2018.
 */

public class OptionsListAdapter implements ListAdapter {
    private List<String> options = new LinkedList<>();
    private List<DataSetObserver> observers = new LinkedList<>();

    public void addItem(String item) {
        options.add(item);
        notifyDatasetChanged();
    }

    public void removeItem (String item) {
        if (options.remove(item))
            notifyDatasetChanged();
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.option_item, viewGroup, false);
        }

        ((TextView) view.findViewById(R.id.text_option)).setText(options.get(i));

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
