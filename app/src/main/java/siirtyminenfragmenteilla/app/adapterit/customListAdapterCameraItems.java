package siirtyminenfragmenteilla.app.adapterit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.Item;

/**
 * Created by k1101374 on 1.10.2014.
 */
public class customListAdapterCameraItems extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Item> cameraItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public customListAdapterCameraItems(Activity activity, List<Item> cameraItems) {
        this.activity = activity;
        this.cameraItems = cameraItems;
    }

    @Override
    public int getCount() {
        return cameraItems.size();
    }

    @Override
    public Object getItem(int location) {
        return cameraItems.get(location);
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
            convertView = inflater.inflate(R.layout.list_row_cameralistitem, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbnail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);

        TextView description = (TextView) convertView.findViewById(R.id.kameranKuvanKuvaus);
        TextView itemIDhidden = (TextView) convertView.findViewById(R.id.itemIDpiilotettu);

        Item m = cameraItems.get(position);
        //Kuvan haku
        thumbnail.setImageUrl(m.getThumbnailUrl(), imageLoader);
        // itemID:n asettaminen
        itemIDhidden.setText(m.getItemID());
        // Kuvauksen asettaminen
        description.setText(m.getItemInfo());

        return convertView;
    }
}
