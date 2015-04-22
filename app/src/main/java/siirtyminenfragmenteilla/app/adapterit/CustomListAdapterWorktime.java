package siirtyminenfragmenteilla.app.adapterit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.workTimeItem;

/**
 * Created by k1101374 on 17.9.2014.
 */
public class CustomListAdapterWorktime extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<workTimeItem> workTimeItems;

    public CustomListAdapterWorktime(Activity activity, List<workTimeItem> workTimeItems) {
        this.activity = activity;
        this.workTimeItems = workTimeItems;
    }


    @Override
    public int getCount() {
        return workTimeItems.size();
    }

    @Override
    public Object getItem(int location) {
        return workTimeItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_worktime, null);

        TextView päivämäärä = (TextView) convertView.findViewById(R.id.listItemPvm);
        TextView tuntiMäärä = (TextView) convertView.findViewById(R.id.listItemTuntimäärä);
        TextView selite = (TextView) convertView.findViewById(R.id.työajanseliteLayout);
        TextView wtID = (TextView) convertView.findViewById(R.id.tyoajanIDpiilotettu);

        workTimeItem m = workTimeItems.get(position);

        päivämäärä.setText(m.getWtDate());
        tuntiMäärä.setText(m.getWtTime());
        selite.setText(m.getWtInfo());
        wtID.setText(m.getWtID());

        return convertView;
    }
}
