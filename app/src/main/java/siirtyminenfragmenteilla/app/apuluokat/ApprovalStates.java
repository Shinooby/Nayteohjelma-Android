package siirtyminenfragmenteilla.app.apuluokat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by k1101374 on 11.9.2014.
 */
public class ApprovalStates {

    private String saStatus;
    private String saInfo;
    private String saSection;
    private String saID;

    public String getSaInfo() {
        return saInfo;
    }

    public void setSaInfo(String saInfo) {
        this.saInfo = saInfo;
    }

    public String getSaStatus() {
        return saStatus;
    }

    public void setSaStatus(String saStatus) {
        this.saStatus = saStatus;
    }

    public String getSaSection() {
        return saSection;
    }

    public void setSaSection(String saSection) {
        this.saSection = saSection;
    }

    public String getSaID() {
        return saID;
    }

    public void setSaID(String saID) {
        this.saID = saID;
    }
}

