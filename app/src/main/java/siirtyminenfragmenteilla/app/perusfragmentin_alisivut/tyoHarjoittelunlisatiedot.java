package siirtyminenfragmenteilla.app.perusfragmentin_alisivut;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;

public class tyoHarjoittelunlisatiedot extends ActionBarActivity {

    EditText beginDate;
    EditText endDate;

    EditText harjPituusTyötunteina;
    EditText harjValvoja;
    EditText harjVavojanPuhelinnro;
    EditText harjValvojanSähköposti;
    EditText harjHenkilöturvatunnus;
    EditText harjOsoite;
    EditText harjPostiNumero;
    EditText harjKotikunta;
    EditText harjoittelijanSähköposti;
    EditText lisätietoa;
    EditText muutOpinnot;

    private String ID = "ID";
    private String IDValue = "";
    private String IDnr1 ="IDnr1";
    private String IDnr2 ="IDnr2";
    private String IDnr3 ="IDnr3";
    private String IDnr4 = "IDnr4";
    private String IDnr4Phone ="IDnr4Phone";
    private String IDnr4Email = "IDnr4Email";
    private String IDnr5HETU = "IDnr5HETU";
    private String IDnr5Address = "IDnr5Address";
    private String IDnr5Postalcode ="IDnr5Postalcode";
    private String IDnr5City =  "IDnr5City";
    private String IDnr5Email ="IDnr5Email";
    private String IDnr6 = "IDnr6";
    private String IDnr7 = "IDnr7";

    private String tableName = "PR_PracticeAdditionalInfo";

    private MyApp app = new MyApp();

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
       // setTheme(R.style.BlackTheme);

    //    getActionBar().setDisplayShowTitleEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(false);
    //    getActionBar().setDisplayShowHomeEnabled(false);
        setContentView(R.layout.activity_tyo_harjoittelunlisatiedot);

        getGlobalVariables();

         beginDate = (EditText) findViewById(R.id.beginDateLayout);
         endDate = (EditText) findViewById(R.id.endDateLayout);

         harjPituusTyötunteina = (EditText) findViewById(R.id.harjoittelunPituusEditText);
         harjValvoja = (EditText) findViewById(R.id.HarjoittelunValvojaEditText);
         harjVavojanPuhelinnro = (EditText) findViewById(R.id.HarjoittelunValvojanPuhelinnroEditText);
         harjValvojanSähköposti = (EditText) findViewById(R.id.HarjoittelunvalvojansähköpostiosoiteEditText);
         harjHenkilöturvatunnus = (EditText) findViewById(R.id.HarjoittelijanHenkiloturvatunnusEditText);
         harjOsoite = (EditText) findViewById(R.id.HarjoittelijanOsoiteEditText);
         harjPostiNumero = (EditText) findViewById(R.id.harjoittelijanPostinumeroEditText);
         harjKotikunta = (EditText) findViewById(R.id.harjoittelijanKotikuntaEditText);
         harjoittelijanSähköposti = (EditText) findViewById(R.id.harjoittelijanSähköpostiOsoiteEdittext);
         lisätietoa = (EditText) findViewById(R.id.lisätietoa_harjoittelussa_huomioitavaaEditxt);
         muutOpinnot = (EditText) findViewById(R.id.muutOpinnotEdittxt);

        // Kutsutaan asyncClassia:
        new JSONParse().execute();

        beginDate.setOnClickListener(onClickListener);
        endDate.setOnClickListener(onClickListener);

        harjPituusTyötunteina.setOnClickListener(onClickListener);
        harjValvoja.setOnClickListener(onClickListener);
        harjVavojanPuhelinnro.setOnClickListener(onClickListener);
        harjValvojanSähköposti.setOnClickListener(onClickListener);
        harjHenkilöturvatunnus.setOnClickListener(onClickListener);
        harjOsoite.setOnClickListener(onClickListener);
        harjPostiNumero.setOnClickListener(onClickListener);
        harjKotikunta.setOnClickListener(onClickListener);
        harjoittelijanSähköposti.setOnClickListener(onClickListener);
        lisätietoa.setOnClickListener(onClickListener);
        muutOpinnot.setOnClickListener(onClickListener);
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
                case R.id.beginDateLayout:
                    attribute = "IDnr1";
                    // Kalenterikoodi
                    currentValue = beginDate.getText().toString();
                    callDialogForDate(0,
                            currentValue, beginDate, attribute);
                    break;
                case R.id.endDateLayout:
                    attribute = "IDnr2";
                    // Kalenterikoodi
                    currentValue = endDate.getText().toString();
                    callDialogForDate(1,
                            currentValue, endDate, attribute);
                    break;
                case R.id.harjoittelunPituusEditText:
                    // Kutsutaan metodia joka palauttaa Stringin joka on muutettu arvo nykyisestä
                    // ja joka laitetaan lähetyskoodiin tämän jälkeen.
                     attribute = "IDnr3";
                     currentValue = harjPituusTyötunteina.getText().toString();
                     callDialogForInput("Harjoittelun pituus työtunteina",
                            currentValue, harjPituusTyötunteina, attribute, 50);
                    break;
                case R.id.HarjoittelunValvojaEditText:
                    currentValue = harjValvoja.getText().toString();
                    attribute = "IDnr4";

                    callDialogForInput("Harjoittelun valvoja",
                            currentValue, harjValvoja, attribute, 50);

                    break;
                case R.id.HarjoittelunValvojanPuhelinnroEditText:
                    currentValue = harjVavojanPuhelinnro.getText().toString();
                    attribute = IDnr4Phone;
                    callDialogForInput("Harjoittelun valvojan puhelinnumero",
                            currentValue, harjVavojanPuhelinnro, attribute, 50);
                    break;
                case R.id.HarjoittelunvalvojansähköpostiosoiteEditText:
                    currentValue = harjValvojanSähköposti.getText().toString();
                    attribute = IDnr4Email;
                    callDialogForInput("Harjoittelun valvojan sähköpostiosoite",
                            currentValue, harjValvojanSähköposti, attribute, 50);

                    break;
                case R.id.HarjoittelijanHenkiloturvatunnusEditText:
                    currentValue = harjHenkilöturvatunnus.getText().toString();
                    attribute = IDnr5HETU;
                    callDialogForInput("Harjoittelijan henkilöturvatunnus",
                            currentValue, harjHenkilöturvatunnus, attribute, 50);
                    break;
                case R.id.HarjoittelijanOsoiteEditText:
                    currentValue = harjOsoite.getText().toString();
                    attribute = IDnr5Address;
                    callDialogForInput("Harjoittelun osoite",
                            currentValue, harjOsoite, attribute, 50);

                    break;
                case R.id.harjoittelijanPostinumeroEditText:
                    currentValue = harjPostiNumero.getText().toString();
                    attribute = IDnr5Postalcode;
                    callDialogForInput("Harjoittelijan postinumero",
                            currentValue, harjPostiNumero, attribute, 50);

                    break;
                case R.id.harjoittelijanKotikuntaEditText:
                    currentValue = harjKotikunta.getText().toString();
                    attribute = IDnr5City;
                    callDialogForInput("Harjoittelijan kotikunta",
                            currentValue, harjKotikunta, attribute, 50);

                    break;
                case R.id.harjoittelijanSähköpostiOsoiteEdittext:
                    currentValue = harjoittelijanSähköposti.getText().toString();
                    attribute = IDnr5Email;
                    callDialogForInput("Harjoittelijan sähköpostiosoite",
                            currentValue, harjoittelijanSähköposti, attribute, 50);

                    break;
                case R.id.lisätietoa_harjoittelussa_huomioitavaaEditxt:
                    currentValue = lisätietoa.getText().toString();
                    attribute = IDnr6;
                    callDialogForInput("Lisätietoa (Harjoittelussa huomioitavat asiat)",
                            currentValue, lisätietoa, attribute, 1000);

                    break;
                case R.id.muutOpinnotEdittxt:
                    currentValue = muutOpinnot.getText().toString();
                    attribute = IDnr7;
                    callDialogForInput("Muut opinnot",
                            currentValue, muutOpinnot, attribute, 1000);
                    break;

            }
        }
    };

    private void callDialogForDate(final int startOrEnd, String currentDate,
                                   final EditText dateToChange, final String attribute) {

        String[] separated;
        String currentDay="";
        String currentMonth="";

        String firstParseYear = "";
        String currentYear = "";
        if (currentDate.length() < 0) {
           separated = currentDate.split("\\.");
           currentDay = separated[0];
            currentMonth = separated[1];
            firstParseYear = separated[2];

            currentYear = firstParseYear.split("\\s+")[0];
            Log.d("Split string", "päivä: " + currentDay + " kk: " + currentMonth + " vuosi: "
                    + currentYear);
        }

        int mYear = 0;
        int mMonth = 0;
        int mDay = 0;

        if (currentDate.equals("") || currentDate.equals(null)) {
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        else
        {
            mYear = Integer.parseInt(currentYear);
            mMonth = Integer.parseInt(currentMonth) - 1;
            mDay = Integer.parseInt(currentDay);
        }

        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (view.isShown()) {
                            int newMonthOfYear = monthOfYear + 1;

                            String dateToString = dayOfMonth + "." + newMonthOfYear + "." + year;
                            dateToChange.setText(dateToString);

                            switch (startOrEnd) {
                                case (0):
                                    String dateToSendStart = dateToString + " 0:00:00";
                                    Log.d("DatetoSendStart", dateToSendStart);
                                    if (saveState.equals("update")) {
                                        new JSONUpdate().execute(attribute, dateToSendStart, ID, IDValue);
                                    }
                                    if (saveState.equals("insert")) {
                                        new JSONInsertInfo().execute(attribute, dateToSendStart);
                                    }
                                        break;
                                case (1):
                                    String dateToSendEnd = dateToString + " 0:00:00";
                                    Log.d("DatetoSendEnd", dateToSendEnd);
                                    if (saveState.equals("update")) {
                                        new JSONUpdate().execute(attribute, dateToSendEnd, ID, IDValue);
                                    }
                                    if (saveState.equals("insert")) {
                                        new JSONInsertInfo().execute(attribute, dateToSendEnd);
                                    }
                                    break;
                            }
                        }
                    }
                }, mDay, mMonth , mYear);
        dpd.updateDate(mYear, mMonth, mDay);
        dpd.show();

    }

    private void callDialogForInput(String description, final String originalValue,
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

                    if (!value.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*")) {
                        Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi sisällä seuraavia \n " +
                                "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
                    } else {
                        //eri arvo ==> Tallenna editTextiin
                        viewToChange.setText(value);
                        wantToCloseDialog = true;
                        if (saveState.equals("update")) {
                            new JSONUpdate().execute(attribute, value, ID, IDValue);
                        }
                        if (saveState.equals("insert")) {
                            new JSONInsertInfo().execute(attribute, value);
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tyo_harjoittelunlisatiedot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_discuss) {
            Intent intent = new Intent(this,discussionActivity.class);
            startActivity(intent);
        } if (id == R.id.action_worktime)
        {
            Intent intent = new Intent(this,WorkTimeReport.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String tyoharjoittelunTiedotString = "ItemIDforJSON";

            try{
                JSONObject json = jsonClass.letsGetPRTextItemInfoByUsername(tyoharjoittelunTiedotString);
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
                IDValue = json.getString(ID);
                saveState = "update";

                String startDate = json.getString(IDnr1);
                beginDate.setText(startDate.split("\\s+")[0]);

                String enddate = json.getString(IDnr2);
                endDate.setText(enddate.split("\\s+")[0]);

                harjPituusTyötunteina.setText(json.getString(IDnr3));
                harjValvoja.setText(json.getString(IDnr4));
                harjVavojanPuhelinnro.setText(json.getString(IDnr4Phone));
                harjValvojanSähköposti.setText(json.getString(IDnr4Email));
                harjHenkilöturvatunnus.setText(json.getString(IDnr5HETU));
                harjOsoite.setText(json.getString(IDnr5Address));
                harjPostiNumero.setText(json.getString(IDnr5Postalcode));
                harjKotikunta.setText(json.getString(IDnr5City));
                harjoittelijanSähköposti.setText(json.getString(IDnr5Email));
                lisätietoa.setText(json.getString(IDnr6));
                muutOpinnot.setText(json.getString(IDnr7));

            } catch (Exception e) {
                saveState = "insert";
            }
        }
        else {
            saveState = "insert";
        }
    }

    private class JSONUpdate extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();

            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getPassword());

            // Päivitysmetodi
            String attribute = strings[0];
            String value = strings[1];
            String descriptionOfID = strings[2];
            String ID = strings[3];
            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value + " ja ID: " + IDValue);

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

}
