package siirtyminenfragmenteilla.app.kotielaintuotannon_alisivut;

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


public class ElaintenRuokintaAlisivu extends ActionBarActivity {

    private String tableName = "tableID";
    private String ID = "ID";
    private String IDValue;

    EditText rehujenKäyttö;
    EditText rehujenTuottoJaOsto;
    EditText rehujenKulutusJaHinnat;
    EditText ruokinnanOnnistuminenJaSeuranta;

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
        setContentView(R.layout.activity_elainten_ruokinta_alisivu);

        setCredentialsFromPreferences();

        rehujenKäyttö = (EditText)findViewById(R.id.rehujenKäyttöEläinryhmilleLayout);
        rehujenTuottoJaOsto = (EditText)findViewById(R.id.rehujenTuottoJaOstoLayout);
        rehujenKulutusJaHinnat = (EditText)findViewById(R.id.rehunKulutusJaHinnatLayout);
        ruokinnanOnnistuminenJaSeuranta = (EditText)findViewById(R.id.ruokintaJaSenSeuranta);

        rehujenKäyttö.setOnClickListener(onClickListener);
        rehujenTuottoJaOsto.setOnClickListener(onClickListener);
        rehujenKulutusJaHinnat.setOnClickListener(onClickListener);
        ruokinnanOnnistuminenJaSeuranta.setOnClickListener(onClickListener);

        new JSONParse().execute();
        // TODO lisää tiedot
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
            String currentValue;
            String attribute;

            switch (view.getId()) {
                case (R.id.rehujenKäyttöEläinryhmilleLayout):
                    Log.d("Ruokinta", "painoit rehujen käyttöä eläinryhmille");
                    currentValue = rehujenKäyttö.getText().toString();
                    attribute = "attributeForJSON";
                    callDialogForInput("Mitä rehuja käytetään eri eläinryhmille?",
                            currentValue, rehujenKäyttö, attribute, 1000);
                    break;
                case (R.id.rehujenTuottoJaOstoLayout):
                    Log.d("Ruokinta", "painoit rehujen tuottoa ja ostoa");
                    currentValue = rehujenTuottoJaOsto.getText().toString();
                    attribute = "attributeForJSON";
                    callDialogForInput("Mitkä rehut tuotetaan itse, mitä ostetaan ja mistä?",
                            currentValue, rehujenTuottoJaOsto, attribute, 1000);
                    break;
                case (R.id.rehunKulutusJaHinnatLayout):
                    Log.d("Ruokinta", "painoit rehujen kulutusta ja hintoja");
                    currentValue = rehujenKulutusJaHinnat.getText().toString();
                    attribute = "attributeForJSON";
                    callDialogForInput("Arvioi rehun kulutus/päivä ja etsi ostorehujen hinnat",
                            currentValue, rehujenKulutusJaHinnat, attribute, 1000);

                    break;
                case (R.id.ruokintaJaSenSeuranta):
                    Log.d("Ruokinta", "painoit ruokintaa ja sen seurantaa");
                    currentValue = ruokinnanOnnistuminenJaSeuranta.getText().toString();
                    attribute = "attributeForJSON";
                    callDialogForInput("Miten ruokintaa ja sen onnistumista seurataan?",
                            currentValue, ruokinnanOnnistuminenJaSeuranta, attribute, 1000);
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
        getMenuInflater().inflate(R.menu.elainten_ruokinta_alisivu, menu);
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

            String itemIDforJSON = "feed";

                try {
                    JSONObject json = jsonClass.letsGetPRTextItemInfoByUsername(itemIDforJSON);
                    Log.d("json haku", "haun ItemID: " + itemIDforJSON);
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
                   // tilanLypsyrutiinit.setText(json.getString("mrRoutines"));
                    // TODO testaa onko getStringit oikeita
                    IDValue = json.getString(ID);
                    saveState = "update";
                    rehujenKäyttö.setText(json.getString("JsonID"));
                    rehujenTuottoJaOsto.setText(json.getString("JsonID"));
                    rehujenKulutusJaHinnat.setText(json.getString("JsonID"));
                    ruokinnanOnnistuminenJaSeuranta.setText(json.getString("JsonID"));
                }
                else {
                    Log.d("Json on tyhjä", "...");
                    saveState = "insert";
                }
            } catch (Exception e) {
                Log.d("Tapahtui virhe", e.toString());
                saveState = "insert";
            }
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
            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value + " ID:n kuvaus: " +
                    descriptionOfID + " ja ID:n arvo: " + ID);

            JSONObject json = jsonClass.letsUpdatePR_PracticeAdditionalInfo(tableName,
                    attribute, value, descriptionOfID, ID);

            return null;
        }
    }

}
