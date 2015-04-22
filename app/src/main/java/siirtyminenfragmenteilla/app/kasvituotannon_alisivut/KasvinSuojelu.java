package siirtyminenfragmenteilla.app.kasvituotannon_alisivut;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.CAMperustiedot;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.WorkTimeReport;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.discussionActivity;


public class KasvinSuojelu extends ActionBarActivity {

    ImageButton rikkaKasvit;
    ImageButton kasviTaudit;
    ImageButton tuholaiset;
    ImageButton kasvuSaateet;
    ImageButton ruiskutustenOnnistuminen;
    ImageButton euVaatimukset;

    MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    private String itemID;

    private JSONArray arrayToReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setTheme(R.style.BlackTheme);
        setContentView(R.layout.activity_kasvin_suojelu);

        rikkaKasvit = (ImageButton)findViewById(R.id.rikkakasvitButton);
        kasviTaudit = (ImageButton)findViewById(R.id.kasviTauditButton);
        tuholaiset = (ImageButton)findViewById(R.id.tuholaisetButton);
        kasvuSaateet = (ImageButton)findViewById(R.id.kasvusääteetButton);
        ruiskutustenOnnistuminen  = (ImageButton)findViewById(R.id.ruiskutustenOnnistuminenButton);
        euVaatimukset = (ImageButton)findViewById(R.id.euvaatimuksetButton);


        rikkaKasvit.setOnClickListener(onClickListener);
        kasviTaudit.setOnClickListener(onClickListener);
        tuholaiset.setOnClickListener(onClickListener);
        kasvuSaateet.setOnClickListener(onClickListener);
        ruiskutustenOnnistuminen.setOnClickListener(onClickListener);
        euVaatimukset.setOnClickListener(onClickListener);

        setCredentialsFromPreferences();
    }

    private void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");
        String fIDfromPref = mSharedPreferences.getString(PREF_FID, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(fIDfromPref);

    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONGetPRImageInfo task;

            switch (v.getId()) {
                case R.id.rikkakasvitButton:
                    Log.d("Kasvinsuojelu", "Rikkakasvit button");
                    itemID = "individualID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.kasviTauditButton:
                    Log.d("Kasvinsuojelu", "Kasvitaudit button");
                    itemID = "individualID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);

                    break;
                case R.id.tuholaisetButton:
                    Log.d("Kasvinsuojelu", "Tuholaiset button");
                    itemID = "individualID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);

                    break;
                case R.id.kasvusääteetButton:
                    Log.d("Kasvinsuojelu", "kasvusääteet button");
                    itemID = "individualID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);

                    break;
                case R.id.ruiskutustenOnnistuminenButton:
                    Log.d("Kasvinsuojelu", "Ruiskutusten onnistuminen button");
                    itemID = "individualID";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);

                    break;
                case R.id.euvaatimuksetButton:
                    Log.d("Kasvinsuojelu", "Eu-vaatimukset button");
                    Intent intentEuVaatimukset = new Intent(getApplicationContext(), euVaatimuksetAlisivu.class);
                    startActivity(intentEuVaatimukset);
                    break;
            }
        }
    };


    private class JSONGetPRImageInfo extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();

            JSONArray json = null;
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            //Tämä lisätään vasta kun on omia kuvia.
            jsonClass.setfID(app.getfID());
            try {
                json = jsonClass.letsGetPRImageItemInfo(itemID);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json;
            // return null;
        }

        @Override
        protected void onPostExecute(JSONArray json) {
            super.onPostExecute(json);
            Log.d("Tietojen haun asynctask", "onPostExecute");
            arrayToReceive = json;
            Intent intentForCameraActivities = new Intent(getApplicationContext(), CAMperustiedot.class);
            intentForCameraActivities.putExtra("ID_TO_GET", itemID);
            intentForCameraActivities.putExtra("jsonarray", arrayToReceive.toString());
            startActivity(intentForCameraActivities);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kasvin_suojelu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.discussion_logo:
                Log.d("Painoit", "Keskustelua");
                Intent intent = new Intent(this,discussionActivity.class);
                startActivity(intent);
                return true;
            case R.id.worktime_logo:
                Log.d("Painoit", "Työraportteja");
                Intent intent2 = new Intent(this,WorkTimeReport.class);
                startActivity(intent2);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
