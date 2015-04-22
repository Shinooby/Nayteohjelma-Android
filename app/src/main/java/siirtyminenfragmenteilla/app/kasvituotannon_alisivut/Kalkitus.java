package siirtyminenfragmenteilla.app.kasvituotannon_alisivut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.WorkTimeReport;
import siirtyminenfragmenteilla.app.perusfragmentin_alisivut.discussionActivity;


public class Kalkitus extends ActionBarActivity {

    private String tableName = "";

    private String lID = "lID";
    private String lIDValue;
    EditText kalkitusRutiinit;


     MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    private String saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setTheme(R.style.BlackTheme);
        setContentView(R.layout.activity_kalkitus);

        getGlobalVariables();

        kalkitusRutiinit = (EditText)findViewById(R.id.kalkitusrutiinitLayout);

        new JSONParse().execute();

        kalkitusRutiinit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Kalkitus", "Painoit kalkituksen EditTextiä");
            String currentValue = kalkitusRutiinit.getText().toString();
                String attribute = "lLimingRoutines";

                callDialogForInput("Kerro tilan kalkitusrutiineista",
                        currentValue,kalkitusRutiinit , attribute, 1000);

            }
        });
    }

    private void getGlobalVariables() {
        // avataan yhteys preferensseihin
        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");
        String fIDfromPref = mSharedPreferences.getString(PREF_FID, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(fIDfromPref);
    }

    private String callDialogForInput(String description, final String originalValue,
                                      final EditText viewToChange, final String attribute, int maxChars) {

        final EditText input = new EditText(this);
        input.setText(originalValue);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxChars);
        input.setFilters(FilterArray);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(description)
                .setView(input)
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                    }
                });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean wantToCloseDialog = false;

                String value = input.getText().toString();
                if (!value.equals(originalValue)) {
                    //eri arvo ==> Tallenna editTextiin
                    viewToChange.setText(value);

                    if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                        Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                    } else {
                        if (saveState.equals("update")) {
                            new JSONUpdate().execute(attribute, value, lID, lIDValue);
                            wantToCloseDialog = true;
                        }
                        if (saveState.equals("insert")) {
                            new JSONInsertInfo().execute(attribute, value);
                            wantToCloseDialog = true;
                        }
                    }
                }
                else {
                    wantToCloseDialog = true;
                }

                if (wantToCloseDialog)
                    dialog.dismiss();
            }
        });



        return null;
    }

    private class JSONUpdate extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();

            String tyoharjoittelunTiedotString = "practiceadditionalinfo";
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            // Päivitysmetodi
            String attribute = strings[0];
            String value = strings[1];
            String descriptionOfID = strings[2];
            String ID = strings[3];
            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value + " ja ID: " + lIDValue);

            JSONObject json = jsonClass.letsUpdatePR_PracticeAdditionalInfo(tableName,
                    attribute, value, descriptionOfID, ID);

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

        }
    }

    private class JSONInsertInfo extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String attribute = strings[0];
            String value = strings[1];

            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value);

            jsonClass.letsInsertPRTextItem(tableName, attribute, value);

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            new JSONParse().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kalkitus, menu);
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

    // Luokka joka lähettää http-haun ja palauttaa sieltä saadun JSONObjectin:
    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            // Maatilan lisätiedot
            String itemIDforJSON = "liming";

            try {
                JSONObject json = jsonClass.letsGetPRTextItemInfoByUsername(itemIDforJSON);
                return json;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);
            // Debugataan aluksi ennenkuin tehdään suurempia operaatioita.
            // Eli ensin logiin stringi
            // Toimii
            if (json != null) {
                try {
                lIDValue = json.getString(lID);
                    saveState = "update";
                    if (json.getString("JsonID") == null) {
                        kalkitusRutiinit.setText("");
                    } else
                        kalkitusRutiinit.setText(json.getString("JsonID"));

                } catch (Exception e) {

                }
            }
            else {
                saveState = "insert";
            }
        }
    }



}
