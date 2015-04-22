package siirtyminenfragmenteilla.app.apuluokat;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by k1101374 on 25.9.2014.
 */
public class MyApp extends Application {

    public String getHardwareGUID() {
        return hardwareGUID;
    }

    private String hardwareGUID = "GUIDforAuthentication";

    public String getJSONURLForData() {
        return JSONURLForData;
    }

    public String getJSONURLForImages() {
        return JSONURLForImages;
    }

    public String getServerPath() {
        return serverPath;
    }

    private String JSONURLForData = "URL josta haetaan tiedot";
    private String JSONURLForImages = "URL josta saadaan kuvat";
    private String serverPath = "Palvelimen IP";
    private String fID;
    private String username;
    private String password;

    public String getRegexForCharacters() {
        return regexForCharacters;
    }

    private String regexForCharacters = "[a-zA-Z0-9.,/-?: ]*";

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    private String realname;



    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkIfSpecial(String stringToCheck) {
        Pattern p = Pattern.compile("[a-zA-Z0-9.? ]*", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(stringToCheck);
        boolean b = m.find();

        if (b) {
            return true;
        }
        else {
            return false;
        }
    }



}
