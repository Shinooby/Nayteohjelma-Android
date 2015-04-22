package siirtyminenfragmenteilla.app.perusfragmentin_alisivut;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.adapterit.CustomListAdapterWorktime;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.apuluokat.workTimeItem;


public class WorkTimeReport extends Activity implements AdapterView.OnItemClickListener {

    private ImageButton newReport;
    private TextView sendReportTextView;

    private String DiscussionOrWorkTime = "Worktime";

    private ProgressDialog pDialog;
    private List<workTimeItem> listOfWorktimes = new ArrayList<workTimeItem>();
    private ListView listView;
    private CustomListAdapterWorktime adapter;

    MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_time_report);
        setTheme(R.style.BlackTheme);
        setCredentialsFromPreferences();

        newReport = (ImageButton) findViewById(R.id.lisaaUusiRaportti);
        sendReportTextView = (TextView) findViewById(R.id.lisaaUusiRaporttiTextView);

        listView = (ListView) findViewById(R.id.workTimeListView);
        adapter = new CustomListAdapterWorktime(this, listOfWorktimes);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listView.setOnItemClickListener(this);

        pDialog = new ProgressDialog(this);
        // Latausdialogi
        pDialog.setMessage("Ladataan tietoja...");
        pDialog.show();

        newReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kutsu dialogia
                Log.d(" Työraportit ", "Painettiin raportin lisäyksen kuvaa");
                callDialogForNewReport();
            }
        });

        sendReportTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // kutsu dialogia
               Log.d(" Työraportit ", "Painettiin raportin lisäys tekstiä");
                callDialogForNewReport();
            }
        });

        new JSONWorktimeGet().execute();
    }

    private void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(mSharedPreferences.getString(PREF_FID, ""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void callDialogForNewReport() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        setTheme(R.style.BlackTheme);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.custom_dialog_tyoajat, null))
                // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        final DatePicker työpäivä = (DatePicker) f.findViewById(R.id.tyoajanDatePicker);
                        final EditText tuntimäärä = (EditText) f.findViewById(R.id.tehtyTuntimaara);
                        final EditText työajanSelite = (EditText) f.findViewById(R.id.tyoajanSelite);

                        int monthToFix = työpäivä.getMonth() + 1;
                        String dateToString = työpäivä.getDayOfMonth() + "." + monthToFix + "." + työpäivä.getYear();

                        String dateToSend = dateToString + " 0:00:00";
                        String hoursToSend = tuntimäärä.getText().toString();
                        String descToSend = työajanSelite.getText().toString();

                        if (!descToSend.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                            Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                    "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                        } else {
                            new JSONWorkTimeInsert().execute(dateToSend, hoursToSend, descToSend);
                        }
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       //Älä tee mitään
                    }
                });
         builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.work_time_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) {

        String date = listOfWorktimes.get(position).getWtDate();
        String time =listOfWorktimes.get(position).getWtTime();
        String info = listOfWorktimes.get(position).getWtInfo();
        String wtID = listOfWorktimes.get(position).getWtID();

        callDialogToModifyReport(date, time, info, wtID);
    }


    private void callDialogToModifyReport(final String dateToParse, String timeToModify, final String descToModify,
                                          final String wtIDforUpdate ) {

        String[] separated = dateToParse.split("\\.");
        String dayToModify = separated[0];
        String monthToModify = separated[1];
        String yearToModify = separated[2];

        int dayForPicker = Integer.parseInt(dayToModify);
        int monthForPickerToMinus = Integer.parseInt(monthToModify);
        int monthForPicker = monthForPickerToMinus - 1;

        int yearForPicker = Integer.parseInt(yearToModify);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

       View view = inflater.inflate(R.layout.custom_dialog_tyoajat, null);

        final DatePicker työpäivä = (DatePicker) view.findViewById(R.id.tyoajanDatePicker);
        final EditText tuntimäärä = (EditText) view.findViewById(R.id.tehtyTuntimaara);
        final EditText työajanSelite = (EditText) view.findViewById(R.id.tyoajanSelite);

        työpäivä.updateDate(yearForPicker, monthForPicker, dayForPicker);
        tuntimäärä.setText(timeToModify);
        työajanSelite.setText(descToModify);

       // this.setTheme(R.style.BlackTheme);
        setTheme(R.style.BlackTheme);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        int monthToFix = työpäivä.getMonth() + 1;
                        String dateToString = työpäivä.getDayOfMonth() + "." + monthToFix + "." + työpäivä.getYear();

                        String dateToSend = dateToString + " 0:00:00";
                        String hoursToSend = tuntimäärä.getText().toString();
                        String descToSend = työajanSelite.getText().toString();

                        new JSONWorkTimeUpdate().execute(dateToSend, hoursToSend, descToSend, wtIDforUpdate);
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                });
        builder.create().show();

    }

    private class JSONWorktimeGet extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            JSONArray jarray = null;
            try {
                jarray = jsonClass.letsGetDiscussionsAndWorkTime(DiscussionOrWorkTime);
             } catch (Exception e) {
                Log.d("Tapahtui virhe JSON-taulukon haussa", e.toString());
                }
            return jarray;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            hidePDialog();
            if (json != null) {

                listOfWorktimes.clear();
            for (int i = 0; i < json.length(); i++) {
                try {
                    JSONObject obj = json.getJSONObject(i);
                    workTimeItem item = new workTimeItem();

                    String dateToModify = obj.getString("itemID");
                    String dateToSet = dateToModify.split("\\s+")[0];
                    item.setWtDate(dateToSet);
                    item.setWtID(obj.getString("itemID"));
                    item.setWtInfo(obj.getString("itemID"));
                    item.setWtTime(obj.getString("itemID"));

                    listOfWorktimes.add(item);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
                adapter.notifyDataSetChanged();
        }

    }
 }

    // Luokka joka lähettää http-haun ja palauttaa sieltä saadun JSONObjectin:
    private class JSONWorkTimeInsert extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());
            String attributes = "Attribute1, Attribute2, Attribute3";

            String dateWeGot = strings[0];
            String hoursWeGot = strings[1];
            String descWeGot = strings[2];

            String valuesToSend = dateWeGot + "', '" + hoursWeGot + "', '" + descWeGot;

            JSONObject json = null;

            try {
            json = jsonClass.letsInsertPRTextItem("tableName",attributes, valuesToSend);
            } catch (Exception e) { e.printStackTrace(); }

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            if (json != null)
            {
                new JSONWorktimeGet().execute();
            }

        }
    }

    // Luokka joka lähettää http-haun ja palauttaa sieltä saadun JSONObjectin:
    private class JSONWorkTimeUpdate extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            ArrayList<String> attributes = new ArrayList<String>();

            attributes.add("attribute1");
            attributes.add("attribute2");
           attributes.add("attribute3");

            String dateWeGot = strings[0];
            String hoursWeGot = strings[1];
            String descWeGot = strings[2];
            String idWeGot = strings[3];

            ArrayList<String> values = new ArrayList<String>();

            values.add(dateWeGot);
            values.add(hoursWeGot);
            values.add(descWeGot);

            String descOfID = "itemID";
            JSONObject json = null;

            try {
                json = jsonClass.letsUpdateWorkTimeReport("tableName", attributes, values, descOfID ,idWeGot);
            } catch (Exception e) { e.printStackTrace(); }

           return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            if (json != null)
            {
                new JSONWorktimeGet().execute();
            }
        }
    }
}
