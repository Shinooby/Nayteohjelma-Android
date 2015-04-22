package siirtyminenfragmenteilla.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.util.ArrayList;

import siirtyminenfragmenteilla.app.adapterit.MySSLSocketFactory;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;

/**
 * Created by k1101374 on 18.8.2014.
 */
 public class AsyncClass extends AsyncTask<String, Void, JSONArray> {


    public void setName(String name) {
        this.name = name;
    }

    public String name;

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

    private String username;
    private String password;
    private int mPort = 1;

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
    }

    private String fID;

    MyApp app = new MyApp();
    private String hardwareGUID = app.getHardwareGUID();
    private String jsonUrl = app.getJSONURLForData();
    private String mServer = app.getServerPath();

public AsyncClass() {
    }

    @Override
    protected JSONArray doInBackground(String... strings) {

        return null;
    }

    // Sertifikaatteihin luottaminen HUOM! Vain testikäyttöön!
    public static HttpClient _getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }



    private JSONObject methodForHTTPQueryAndJSONParse(String urlToUse, JSONObject objectToSend, String debugString) {

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username,password);
        ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
                new AuthScope(mServer, mPort), creds);

        HttpPost httppost = new HttpPost(urlToUse);
        InputStream inputStream = null;
        String result = null;

        try {
            Log.d(debugString + " POST-data encoding","Aloitus");
            StringEntity jsonEntity = new StringEntity(objectToSend.toString(), HTTP.UTF_8);
            jsonEntity.setContentType("application/json");
            httppost.setEntity(jsonEntity);
        } catch (UnsupportedEncodingException e) {
            Log.d(debugString + " POST-data encoding","Virhe");
            e.printStackTrace();
        }
        Log.d(debugString + " POST-data encoding","lopetus");

        try {
            Log.d(debugString + " HTTPClientin execute","Aloitus");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.d(debugString + "  HTTPClientin execute", "Lopetus");

            result = sb.toString();

            Log.d(debugString + "  StringBuilderin Result:", result);
        } catch (Exception e) {
            Log.e(debugString + " httpresponse", "Exceptioni:" + e.toString());
        } finally {
            try {
                if (inputStream != null) inputStream.close();

            } catch (Exception squish) {
                Log.e(debugString + " Finally", squish.toString());
            }
        }

        String securityString = null;
        JSONObject resultObj = null;
        try {
            // JSON stringistä JSONObject josta voidaan napsia tarvittavat tavarat.
            resultObj = new JSONObject(result);

            securityString = resultObj.getString("d");
            Log.d(debugString + "  JSON Stringistä tietty objekti", securityString);
            // Tämä pitää parsia toisen kerran
        } catch (Exception e) {
            Log.e(debugString + " JSON stringistä tietty objekti", "Virhe: " + e.toString());
        }

        JSONObject realJsonInfo = null;

        try {
            realJsonInfo = new JSONObject(securityString);
            Log.d(debugString + "  JSON string", realJsonInfo.toString());
        } catch (Exception e) {
            Log.e(debugString + " JSON string", e.toString());
        }

        return realJsonInfo;
    }

    public JSONObject letsGetFarmInfo(String fID) {

        String urlToUse = jsonUrl + "getFarmInfo";
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);

            Log.d("JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e("JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

        UsernamePasswordCredentials creds = new UsernamePasswordCredentials(username,password);
        ((AbstractHttpClient) httpclient).getCredentialsProvider().setCredentials(
                new AuthScope(mServer, mPort), creds);

        Log.d("JSON debuggaus", "httpclient luotiin");
        HttpPost httppost = new HttpPost(urlToUse);
        Log.d("JSON debuggaus", "httppost luotiin");
        InputStream inputStream = null;
        Log.d("JSON debuggaus", "inputstream alustettiin");
        String result = null;
        Log.d("JSON debuggaus", "String result alustettiin");

        try {
            Log.d("POST-data encoding","Aloitus");
            StringEntity jsonEntity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            jsonEntity.setContentType("application/json");

            httppost.setEntity(jsonEntity);
        } catch (UnsupportedEncodingException e) {
            // log exception
            Log.d("POST-data encoding","Virhe");
            e.printStackTrace();
        }
        Log.d("POST-data encoding","lopetus");

        try {
            Log.d("HTTPClientin execute","Aloitus");
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Log.d("HTTPClientin execute", "Lopetus");

            result = sb.toString();

            Log.d("StringBuilderin Result:", result);
        } catch (Exception e) {
            Log.e("httpresponse", "Exceptioni:" + e.toString());
        } finally {
            try {
                if (inputStream != null) inputStream.close();

            } catch (Exception squish) {
                Log.e("Finally", squish.toString());
            }
        }

        String farmInfoString = null;

        try {
            // JSON stringistä JSONObject josta voidaan napsia tarvittavat tavarat.
            JSONObject resultObj = new JSONObject(result);

            farmInfoString = resultObj.getString("d");
            // Tämä pitää parsia toisen kerran, jolloin saadaan toimiva JSONObjekti
        } catch (Exception e) {
           e.printStackTrace();
        }

        JSONObject realFarmInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, "secondQuery");

        try {
            realFarmInfo = new JSONObject(farmInfoString);

            Log.d("JSON string", realFarmInfo.toString());

        } catch (Exception e) {
            Log.e("JSON string", e.toString());
        }

        return realFarmInfo;
    }

    public JSONArray letsGetEvaluation() {

        String debugString = "letsGetApprovalStates ie evaluation";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "hardwareguid: " + hardwareGUID + " fID: " + fID
                + " ja username: " + username );
        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("username", username);
            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        JSONArray actualJsonInfo = null;
        try {
            actualJsonInfo = realJsonInfo.getJSONArray("JsonID");

        } catch (Exception e ) {
            Log.d("Tapahtui virhe", e.toString());
        }

        return actualJsonInfo;
    }

    @Override
    protected void onPostExecute(JSONArray object) {
        super.onPostExecute(object);

        JSONArray jarray = null;

        if (object != null) {
            Log.d("Saatu JSON", object.toString());
            try {
                jarray = object;
            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
        }

    }

    public JSONObject letsGetAuth(String receivedUsername, String receivedPassword) {

        String urlToUse = jsonUrl + "rajapinta";
        username = receivedUsername;
        password = receivedPassword;

        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("username", username);
            jsonObj.put("password", password);

            Log.d("JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e("JSONObjectin auth- teko", "Tapahtui virhe: " + e.toString());
        }
        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, "authentication");

        return realJsonInfo;
    }

    public JSONObject letsGetPRTextItemInfoByUsername(String itemID) {

        String debugString = "letsGetPRText...";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();
        Log.d("itemID:n debuggaus", itemID );

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("username", username);
            jsonObj.put("itemID", itemID);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }
        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    public JSONObject letsUpdateFarmSingleAttributeTake2(String attribute, String inCompleteValue) {

        String debugString = "letsUpdateFarmSingleAttributeTake2...";

        String value = "'" + inCompleteValue + "'";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        hardwareGUID = app.getHardwareGUID();

        String fIDtoUse = "'" + fID + "'";
        fID = fIDtoUse;

        mServer = app.getServerPath();

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "Attribute: " + attribute + " value: " + value );

        try {

            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("attribute", attribute);
            jsonObj.put("value", value);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        Log.d("jsonobject ToString", jsonObj.toString() );

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;

    }

    // Alisivujen päivitys

    public JSONObject letsUpdatePR_PracticeAdditionalInfo(String tableName, String attribute, String value,
                                                          String descriptionOfID,  String ID) {

        String updateSql = "UPDATE " + tableName + " SET " + attribute + " = '" + value + "' WHERE " + descriptionOfID
                + " = " + ID;
            Log.d("UpdateSql", updateSql);

        String debugString = "letsUpdatePR_PracticeAdditionalInfo...";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        String modifiedfID = "'" + fID + "'";
        fID = modifiedfID;

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "Attribute: " + attribute + " value: " + value );

        try {

            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("updateSql", updateSql);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    public JSONArray letsGetDiscussionsAndWorkTime(String whichOneIsIt) {

        String debugString = "letsGetDiscussionsAndWorkTime";

        String urlToUse = "";
        String arrayToGet = "";

        if (whichOneIsIt.equals("Worktime"))
        {
            arrayToGet = "WorkHours";
            urlToUse = jsonUrl + "rajapinta1";
        }
        else {
            Log.d(debugString, "DiscussionAndWorktime ei ollut 'worktime', joten ollaan Discussionissa");
            urlToUse = jsonUrl + "rajapinta2";
            arrayToGet = "arrayName";
        }
        Log.d("urlToUse", urlToUse);

        String fIDtoUse = "'" + fID + "'";
        fID = fIDtoUse;

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("username", username);
            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }
        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        JSONArray actualJsonInfo = null;

        try {
            actualJsonInfo = realJsonInfo.getJSONArray(arrayToGet);

        } catch (Exception e ) {
            Log.d("Tapahtui virhe", e.toString());
        }
        return actualJsonInfo;
    }

    public JSONObject letsInsertPRTextItem(String tableName, String attributes, String value)
    {

        String valueWithName = "";
        // Lisätään täpät valuesiin
        if (tableName.equals("JSON_Table")) {
            valueWithName = "[" + name + "] " + value;
            value = valueWithName;
        }
        String values = "'" + value + "'";

        String insertSql = "INSERT INTO " + tableName + " (ID, User, " + attributes + ") VALUES ('" + fID + "', '" + username +
                "', " + values + ")";
        Log.d("InsertSql", insertSql);

        String debugString = "letsInsertPRTextItem...";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "Attributes: " + attributes + " value: " + value );

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("insertSql", insertSql);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    public JSONObject letsUpdateWorkTimeReport(String tableName, ArrayList<String> attributes, ArrayList<String> values,
                                              String descriptionOfID, String ID) {

        String updateSql = "UPDATE " + tableName + " SET";

        for (int i = 0; i < attributes.size(); i++) {
            updateSql += " " + attributes.get(i) + " = '" + values.get(i) + "',";
        }
        updateSql = updateSql.substring(0, updateSql.length() - 1);

        updateSql += " WHERE " + descriptionOfID + " = " + ID;

        Log.d("UpdateSql", updateSql);


        String debugString = "letsUpdateWorkTimeReport...";

        String urlToUse = jsonUrl + "rajapinta";
        Log.d("urlToUse", urlToUse);

        String fIDtoUse = "'"+ fID + "'";
        fID = fIDtoUse;

        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("updateSql", updateSql);

            //jsonObj.put("fID", fID);
            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    // Kuvien tietojen haku
    public JSONArray letsGetPRImageItemInfo(String itemID) {

        String debugString = "letsGetPRImageItemInfo...";

        String urlToUse = jsonUrl + "rajapinta_kuville";
        Log.d("urlToUse", urlToUse);

        String fIDtoUse = "'"+ fID + "'";
        fID = fIDtoUse;

        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("username", username);
            jsonObj.put("picID", itemID);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        Log.d("jsonobject ToString", jsonObj.toString() );

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        JSONArray actualJsonInfo = null;

        try {
            actualJsonInfo = realJsonInfo.getJSONArray("Items");

        } catch (Exception e ) {
            Log.d("Tapahtui virhe", e.toString());
        }

        return actualJsonInfo;
    }

    // Kuvan lisäyskoodi:
    public JSONObject letsInsertPRImageAndInfo(String itemID, String descOfImage, String encodedImage)
    {
        String debugString = "letsInsertPRImageAndInfo...";

        String urlToUse = jsonUrl + "rajapinta_kuville";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("User", username);
            jsonObj.put("ID", itemID);
            jsonObj.put("desc", descOfImage);
            jsonObj.put("base64image", encodedImage);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        Log.d("jsonobject ToString", jsonObj.toString() );

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    // Valmiin kuvan tietojen päivitys

    public JSONObject letsUpdatePRImageItemInfo(String IDofPicture, String desiredAttribute,
                                                String value, String ItemIDForQuery) {

        String debugString = "letsUpdatePRImageItemInfo...";

        String urlToUse = jsonUrl + "rajapinta_kuville";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();

        String valueToModify = "'" + value + "'";
        value = valueToModify;

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("ID", IDofPicture);
            jsonObj.put("attribute", desiredAttribute);
            jsonObj.put("value", value);
            jsonObj.put("itemID", ItemIDForQuery);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        Log.d("jsonobject ToString", jsonObj.toString() );

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }

    // Poistetaan kuva ja sen muut tiedot
    public JSONObject letsDeletePRImageItem(String IDofPicture, String ItemIDForQuery) {

        String debugString = "letsDeletePRImageItem...";

        String urlToUse = jsonUrl + "rajapinta_kuville";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "fID: " + fID + " username: " + username );

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("ID", IDofPicture);
            jsonObj.put("itemID", ItemIDForQuery);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }


    // Muiden kuvien lisäyskoodi

    public JSONObject letsUpdateFarmImage(String encodedImage)
    {
        String debugString = "letsUpdateFarmImage...";

        String urlToUse = jsonUrl + "rajapinta_kuville";
        Log.d("urlToUse", urlToUse);

        JSONObject jsonObj = new JSONObject();
        Log.d("Parametrien debuggaus", "fID: " + fID + " username: " + username );
        Log.d("Parametrien debuggaus", "encodedimage: " + encodedImage);

        try {
            jsonObj.put("hardwareGUID", hardwareGUID);
            jsonObj.put("fID", fID);
            jsonObj.put("Image", encodedImage);

            Log.d(debugString + " JSON objectin teko", "Valmis");
        } catch (Exception e) {
            Log.e(debugString + " JSONObjectin teko", "Tapahtui virhe: " + e.toString());
        }
        Log.d("jsonobject ToString", jsonObj.toString() );

        JSONObject realJsonInfo = methodForHTTPQueryAndJSONParse(urlToUse, jsonObj, debugString);

        return realJsonInfo;
    }
}