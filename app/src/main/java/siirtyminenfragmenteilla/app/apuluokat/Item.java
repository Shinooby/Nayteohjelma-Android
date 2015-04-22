package siirtyminenfragmenteilla.app.apuluokat;

import android.graphics.Bitmap;

/**
 * Created by k1101374 on 19.9.2014.
 */
public class Item {

    private String itemID, thumbnailUrl;
    private String itemInfo;

    public Item() {
    }

    public Item(String itemID, String thumbnailUrl, String itemInfo) {
        this.itemID = itemID;
        this.thumbnailUrl = thumbnailUrl;
        this.itemInfo = itemInfo;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public void setItemInfo(String itemInfo) {
        this.itemInfo = itemInfo;
    }

}
