package siirtyminenfragmenteilla.app.apuluokat;

/**
 * Created by k1101374 on 16.9.2014.
 */

public class workTimeItem {
    public workTimeItem(String wtID, String wtDate, String wtTime, String wtInfo) {
        this.wtID = wtID;
        this.wtDate = wtDate;
        this.wtTime = wtTime;
        this.wtInfo = wtInfo;
    }

    public workTimeItem() {
    }

    public String getWtID() {
        return wtID;
    }

    public void setWtID(String wtID) {
        this.wtID = wtID;
    }

    public String getWtDate() {
        return wtDate;
    }

    public void setWtDate(String wtDate) {
        this.wtDate = wtDate;
    }

    public String getWtTime() {
        return wtTime;
    }

    public void setWtTime(String wtTime) {
        this.wtTime = wtTime;
    }

    public String getWtInfo() {
        return wtInfo;
    }

    public void setWtInfo(String wtInfo) {
        this.wtInfo = wtInfo;
    }

    public String wtID;
    public String wtDate;
    public String wtTime;
    public String wtInfo;

}
