package siirtyminenfragmenteilla.app.apuluokat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import siirtyminenfragmenteilla.app.R;

/**
 * Created by k1101374 on 23.10.2014.
 */
    public class customListForNewPics extends BaseAdapter {

    private LayoutInflater inflater;
    private final Activity activity;
    private List<ImageAndText> newPicListItems;

    public customListForNewPics(Activity activity, List<ImageAndText> newPicListItems) {
        this.activity = activity;
        this.newPicListItems = newPicListItems;
    }

    @Override
    public int getCount() {
        return newPicListItems.size();
    }

    @Override
    public Object getItem(int location) {
        return newPicListItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null)
            convertView = inflater.inflate(R.layout.listrow_newpicturefromfile, null);

        View rowView = inflater.inflate(R.layout.listrow_newpicturefromfile, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.uudenKuvanKuvaus);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageViewJossaUusiKuva);

        ImageAndText m = newPicListItems.get(position);

        txtTitle.setText(m.getText());

        File imgFile = new File(m.getImagePath());

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imageView.setImageBitmap(myBitmap);
        }

        return rowView;
    }
}