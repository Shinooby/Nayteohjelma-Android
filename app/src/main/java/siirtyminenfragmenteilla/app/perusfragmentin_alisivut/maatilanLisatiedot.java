package siirtyminenfragmenteilla.app.perusfragmentin_alisivut;

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

public class maatilanLisatiedot extends ActionBarActivity {

    EditText yrittäjäJaPerhe;
    EditText tilanYhtiömuoto;
    EditText tilanTyövoima;
    EditText tilanYhteisKoneet;
    EditText tilanMuutKoneet;
    EditText keskimääräinenHehtaarisato;

    private String tableName ="tableName";

    private String IDvalue;

    private String ID = "ID";
    private String IDnr1 = "IDnr1";
    private String IDnr2 = "IDnr2";
    private String IDnr3 = "IDnr3";
    private String IDnr4 = "IDnr4";
    private String IDnr5 = "IDnr5";
    private String IDnr6 = "IDnr6";

    String debugString = "maatilanLisätiedot";

    private MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    private static final String PREF_NAME = "name";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_FID = "fid";
    private SharedPreferences mSharedPreferences;

    private String saveState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setTheme(R.style.BlackTheme);
        setContentView(R.layout.activity_maatilan_lisatiedot);

        getGlobalVariables();

        yrittäjäJaPerhe = (EditText)findViewById(R.id.yrittäjäJaPerheLayout);
        tilanYhtiömuoto = (EditText)findViewById(R.id.tilanyhtiomuotoTextBox);
        tilanTyövoima = (EditText)findViewById(R.id.tilantyovoimaTextBox);
        tilanYhteisKoneet = (EditText)findViewById(R.id.tilanyhteiskoneetjaurakointipalvelutTextBox);
        tilanMuutKoneet = (EditText)findViewById(R.id.tilanmuutkoneetTextBox);
        keskimääräinenHehtaarisato = (EditText)findViewById(R.id.keskimääräinenHehtaarisatoTextBox);

        new JSONParse().execute();

        yrittäjäJaPerhe.setOnClickListener(onClickListener);
        tilanYhtiömuoto.setOnClickListener(onClickListener);
        tilanTyövoima.setOnClickListener(onClickListener);
        tilanYhteisKoneet.setOnClickListener(onClickListener);
        tilanMuutKoneet.setOnClickListener(onClickListener);
        keskimääräinenHehtaarisato.setOnClickListener(onClickListener);

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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String currentValue;
            String attribute;
            switch (v.getId()) {

                case R.id.yrittäjäJaPerheLayout:
                    Log.d(debugString, "'Painoit yrittäjäJaPerheLayout");
                    currentValue = yrittäjäJaPerhe.getText().toString();
                    attribute = IDnr6;
                    callDialogForInput("Yrittäjä ja yrittäjäperhe",
                            currentValue, yrittäjäJaPerhe, attribute, 1000);
                    break;
                case R.id.tilanyhtiomuotoTextBox:
                    Log.d(debugString, "Painoit tilanyhtiomuotoTextBox");
                    currentValue = tilanYhtiömuoto.getText().toString();
                    attribute = IDnr5;
                    callDialogForInput("Tilan yhtiömuoto",
                            currentValue, tilanYhtiömuoto, attribute, 1000);
                    break;
                case R.id.tilantyovoimaTextBox:
                    Log.d(debugString, "'Painoit tilantyovoimaTextBox:");
                    currentValue = tilanTyövoima.getText().toString();
                    attribute = IDnr4;
                    callDialogForInput("Tilan työvoima (oma/palkattu)",
                            currentValue, tilanTyövoima, attribute, 1000);
                    break;
                case R.id.tilanyhteiskoneetjaurakointipalvelutTextBox:
                    Log.d(debugString, "'Painoit tilanyhteiskoneetjaurakointipalvelutTextBox");
                    currentValue = tilanYhteisKoneet.getText().toString();
                    attribute = IDnr3;
                    callDialogForInput("Tilan yhteiskoneet ja urakointipalvelut",
                            currentValue, tilanYhteisKoneet, attribute, 1000);
                    break;
                case R.id.tilanmuutkoneetTextBox:
                    Log.d(debugString, "'Painoit tilanmuutkoneetTextBox");
                    currentValue = tilanMuutKoneet.getText().toString();
                    attribute = IDnr2;
                    callDialogForInput("Tilan muut koneet",
                            currentValue, tilanMuutKoneet, attribute, 1000);
                    break;
                case R.id.keskimääräinenHehtaarisatoTextBox:
                    Log.d(debugString, "'Painoit keskimääräinenHehtaarisatoTextBox");
                    currentValue = keskimääräinenHehtaarisato.getText().toString();
                    attribute = IDnr1;
                    callDialogForInput("Keskimääräinen hehtaarisato",
                            currentValue, keskimääräinenHehtaarisato, attribute, 1000);
                    break;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.maatilan_lisatiedot, menu);
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

            setContentToViews(json);
        }
    }

    private void setContentToViews(JSONObject json) {

        if (json != null) {
            try {
                IDvalue = json.getString(ID);
                saveState = "update";
                yrittäjäJaPerhe.setText(json.getString(IDnr6));
                tilanYhtiömuoto.setText(json.getString(IDnr5));
                tilanTyövoima.setText(json.getString(IDnr4));
                tilanYhteisKoneet.setText(json.getString(IDnr3));
                tilanMuutKoneet.setText(json.getString(IDnr2));
                keskimääräinenHehtaarisato.setText(json.getString(IDnr1));

            } catch (Exception e) {
                Log.d("Tapahtui virhe", e.toString());
                saveState = "insert";
            }
        }
        else {
            saveState = "insert";
        }
    }

    private String callDialogForInput(String description, final String originalValue,
                                      final EditText viewToChange, final String attribute, int maxChars) {

        final EditText input = new EditText(this);
        input.setText(originalValue);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(maxChars);
        input.setFilters(FilterArray);

        AlertDialog.Builder builder =  new AlertDialog.Builder(this)
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

                    if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                        Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                    }
                    else {
                        if (saveState.equals("update")) {
                            new JSONUpdate().execute(attribute, value, ID, IDvalue);
                            viewToChange.setText(value);
                            wantToCloseDialog = true;
                        }
                        if (saveState.equals("insert")) {
                            new JSONInsertInfo().execute(attribute, value);
                            viewToChange.setText(value);
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

            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            // Päivitysmetodi
            String attribute = strings[0];
            String value = strings[1];
            String descriptionOfID = strings[2];
            String ID = strings[3];
            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value + " ja ID: " + IDvalue);

            jsonClass.letsUpdatePR_PracticeAdditionalInfo(tableName,
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

}
