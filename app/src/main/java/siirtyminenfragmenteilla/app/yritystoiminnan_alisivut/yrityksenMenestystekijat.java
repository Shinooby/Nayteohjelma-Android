package siirtyminenfragmenteilla.app.yritystoiminnan_alisivut;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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


public class yrityksenMenestystekijat extends ActionBarActivity {

    EditText yrityksenVahvuudetJaHeikkoudet;
    EditText toimintaympäristönMahdollisuudet;
    EditText yrityksenVahvuudetMuihinVerrattuna;

    private String tableName = "tableName";
    private String ID = "ID";
    private String IDValue;

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
        setContentView(R.layout.activity_yrityksen_menestystekijat);

        setCredentialsFromPreferences();

        yrityksenVahvuudetJaHeikkoudet = (EditText)findViewById(R.id.yrityksenVahvuudetJaHeikkoudetLayout);
        toimintaympäristönMahdollisuudet = (EditText)findViewById(R.id.toimintaYmparistonMahdollisuudetLayout);
        yrityksenVahvuudetMuihinVerrattuna = (EditText)findViewById(R.id.vertaaYrityksenVahvuuksiaLayout);

        new JSONParse().execute();

        yrityksenVahvuudetJaHeikkoudet.setOnClickListener(onClickListener);
        toimintaympäristönMahdollisuudet.setOnClickListener(onClickListener);
        yrityksenVahvuudetMuihinVerrattuna.setOnClickListener(onClickListener);
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
    public void onClick(View view) {
        String attribute;
        String currentValue;

        switch (view.getId()) {

            case (R.id.yrityksenVahvuudetJaHeikkoudetLayout):
                attribute = "currentAttribute";
                currentValue = yrityksenVahvuudetJaHeikkoudet.getText().toString();
                callDialogForInput("Yrityksen vahvuudet ja heikkoudet",
                        currentValue, yrityksenVahvuudetJaHeikkoudet, attribute, 1000);
                break;
            case (R.id.toimintaYmparistonMahdollisuudetLayout):
                attribute = "currentAttribute";
                currentValue = toimintaympäristönMahdollisuudet.getText().toString();
                callDialogForInput("Toimintaympäristön mahdollisuudet ja tulevat uhat",
                        currentValue, toimintaympäristönMahdollisuudet, attribute, 1000);
                break;
            case (R.id.vertaaYrityksenVahvuuksiaLayout):
                attribute = "currentAttribute";
                currentValue = yrityksenVahvuudetMuihinVerrattuna.getText().toString();
                callDialogForInput("Mitkä ovat yrityksen vahvuudet muihin tiloihin verrattuna?",
                        currentValue, yrityksenVahvuudetMuihinVerrattuna, attribute, 10);
                break;
        }

    }
};

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
                if (!value.equals(originalValue))
                {
                    if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                        Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                    }
                    else {

                        if (saveState.equals("update")) {
                            wantToCloseDialog = true;
                            new JSONUpdate().execute(attribute, value, ID, IDValue);
                            viewToChange.setText(value);
                        }
                        if (saveState.equals("insert")) {
                            wantToCloseDialog = true;
                            new JSONInsertInfo().execute(attribute, value);
                            viewToChange.setText(value);
                        }
                    }
                }
                else wantToCloseDialog = true;

                if (wantToCloseDialog)
                    dialog.dismiss();
            }
        });

        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.yrityksen_menestystekijat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.discussion_logo:
                Log.d("Painoit", "Keskustelua");
                Intent intent = new Intent(this, discussionActivity.class);
                startActivity(intent);
                return true;
            case R.id.worktime_logo:
                Log.d("Painoit", "Työraportteja");
                Intent intent2 = new Intent(this, WorkTimeReport.class);
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

            String itemIDforJSON = "itemID";
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

            try {
                if (json != null) {
                    Log.d("json ei ole tyhjä", json.toString());

                    IDValue = json.getString(ID);
                    saveState = "update";
                    yrityksenVahvuudetJaHeikkoudet.setText(json.getString("swStrengthWeak"));
                    toimintaympäristönMahdollisuudet.setText(json.getString("swBusEnvironment"));
                    yrityksenVahvuudetMuihinVerrattuna.setText(json.getString("swFarmStrengths"));
                }
                else {
                    saveState = "insert";
                    Log.d("Json on tyhjä", "...");
                }
            } catch (Exception e) {
                saveState = "insert";
                Log.d("Tapahtui virhe", e.toString());
            }
        }
    }

    private class JSONUpdate extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String attribute = strings[0];
            String value = strings[1];
            String descriptionOfID = strings[2];
            String ID = strings[3];

            JSONObject json = jsonClass.letsUpdatePR_PracticeAdditionalInfo(tableName,
                    attribute, value, descriptionOfID, ID);

            return null;
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

}
