package siirtyminenfragmenteilla.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import siirtyminenfragmenteilla.app.apuluokat.MyApp;


public class MainActivity extends Activity implements View.OnClickListener {



    Button kirjautumisButton;
  private  EditText username;
   private EditText password;
    CheckBox checkBox;

    MyApp app;
    //Nimi minne käyttäjänimi ja salasana varastoidaan
    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public static final String PREF_REALNAME = "realname";

    public static final String PREF_LOGIN_NAME = "login_name";
    public static final String PREF_LOGIN_PASSWORD = "login_password";

    public SharedPreferences mSharedPreferences;

    String userGivenname;

    public final static String CURRENT_USER = "sisaankirjautuminen.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);
        // poistetaan ohjelman sisäiset käyttäjätunnukset
        SharedPreferences.Editor e = mSharedPreferences.edit();
        e.remove(PREF_NAME);
        e.remove(PREF_PASSWORD);
        e.remove(PREF_FID);
        e.remove(PREF_REALNAME);

        kirjautumisButton = (Button) findViewById(R.id.kirjautumisbutton);
        kirjautumisButton.setOnClickListener(this);

        username = (EditText)findViewById(R.id.usernametext);
        password = (EditText)findViewById(R.id.password);

        checkBox = (CheckBox)findViewById(R.id.muistatunnukset);

        checkIfFamiliar();
    }



    public void checkIfFamiliar() {
        // avataan yhteys preferensseihin

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_LOGIN_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_LOGIN_PASSWORD, "");

        //Jos tiedot löytyvät, niin...
        if (familiarUser.length() != 0 && familiarPassword.length() != 0)
        {
            checkBox.setChecked(true);
            username.setText(familiarUser);
            password.setText(familiarPassword);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public void onClick(View view) {
        try {
            checkUserInput();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    private void checkUserInput() throws InterruptedException, ExecutionException, TimeoutException {
        // TODO Autentikointi hoidetaan taustasäikeen avulla, joka ottaa yhteyden palvelimeen
        // TODO ja tarkistaa käyttäjän antamat tiedot


        // alustetaan käyttäjän antamat tiedot Stringeiksi
        userGivenname = username.getText().toString();
        String userGivenpassword = password.getText().toString();
        // onko Edittexteihin kirjoitettu mitään?

        if(userGivenname.length() != 0 && userGivenpassword.length() != 0) {
            //new JSONParse().execute(userGivenname, userGivenpassword);
                Intent intent = new Intent(getApplicationContext(), FragmentHolder.class);
                startActivity(intent);


        }
        //jos ei niin ilmoitetaan toastilla.
        else
            Toast.makeText(this, "Tarkista käyttäjänimi ja salasana", Toast.LENGTH_LONG).show();
    }

    private void isCheckBoxChecked(String userGivenname, String userGivenpassword) {

        if (checkBox.isChecked())
        {
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.putString(PREF_LOGIN_NAME, userGivenname);
            e.putString(PREF_LOGIN_PASSWORD, userGivenpassword);
            e.apply();
        }
        else {
            SharedPreferences.Editor e = mSharedPreferences.edit();
            e.remove(PREF_LOGIN_NAME);
            e.remove(PREF_LOGIN_PASSWORD);

            e.commit();
        }
    }

    // Luokka joka lähettää http-haun ja palauttaa sieltä saadun JSONObjectin:
    private class JSONParse extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            JSONObject json2 = null;
            try {
            json2 = jsonClass.letsGetAuth(strings[0], strings[1]);
            } catch (Exception e) {
                Toast.makeText(getParent(), "Tarkista käyttäjätunnus ja salasana", Toast.LENGTH_LONG).show();
            }
            return json2;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            app = new MyApp();
            JSONArray farmIDs;
            if (json != null) {
                Log.d("Tunnukset toimivat", json.toString());
                try {
                    app.setUsername(username.getText().toString());
                    app.setPassword(password.getText().toString());
                    app.setRealname(json.getString("uName"));

                    isCheckBoxChecked(username.getText().toString(), password.getText().toString());

                    SharedPreferences.Editor e = mSharedPreferences.edit();
                    e.putString(PREF_NAME, app.getUsername());
                    e.putString(PREF_PASSWORD, app.getPassword());
                    e.putString(PREF_REALNAME, app.getRealname());

                    farmIDs = json.getJSONArray("Farms");

                    int lenght = farmIDs.length();

                    AsyncClass jsonclass = new AsyncClass();

                    Log.d("Kuinka suuri array on? ",  "" +lenght);
                    if (farmIDs.length() > 1) {
                        createDialogForIDs(farmIDs);

                        // luo alertdialog joka kysyy mikä farmi on kyseessä
                    } else {

                    String firstFarm = farmIDs.getJSONObject(0).getString("fID");

                    e.putString(PREF_FID, firstFarm);
                    e.apply();

                    jsonclass.setfID(firstFarm);
                    app.setfID(firstFarm);
                    new JSONCorrectUser().execute(firstFarm);

                    Intent intent = new Intent(getApplicationContext(), FragmentHolder.class);
                    startActivity(intent);
                    }

                } catch (Exception e) {
                    Log.d("Tapahtui virhe", e.toString());
                }
            }
            else {
                Log.d("Tunnukset ei toimi", "JSON on null");
                Toast.makeText(getApplicationContext(), "Kirjautuminen epäonnistui \n " +
                        "Tarkista käyttäjätunnus ja salasana ", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class JSONCorrectUser extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            String fIDtoUse = app.getfID();
            JSONObject json = jsonClass.letsGetFarmInfo(fIDtoUse);

            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            // Intent joka lähettää lähetyksen
            if (json != null) {
                Log.d("Tunnukset toimivat", json.toString());

                Intent broadcastIntent = new Intent("siirtyminenfragmenteilla.app.action.UI_UPDATE");
                broadcastIntent.putExtra("JSONObject", json.toString());
                broadcastIntent.putExtra("username", app.getUsername());
                broadcastIntent.putExtra("password", app.getPassword());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(broadcastIntent);

            }
            else
                Log.d("Tunnukset ei toimi", json.toString());
        }
    }

    private void createDialogForIDs(JSONArray fids) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        View view = inflater.inflate(R.layout.custom_dialog_farmid, null);

        final RadioButton[] rb = new RadioButton[fids.length()];

        final RadioGroup farmsToChoose = (RadioGroup) view.findViewById(R.id.radiobuttonsForIDs);
        farmsToChoose.setOrientation(RadioGroup.VERTICAL);

        for (int i = 0; i < fids.length(); i++) {
           rb[i] = new RadioButton(this);
            farmsToChoose.addView(rb[i]);
            String textForButton;
            try {
                textForButton = fids.getJSONObject(i).getString("fID");
            } catch (Exception e) {
                textForButton = "Epäonnistui";
            e.printStackTrace();
            }
            rb[i].setText(textForButton);
        }
           farmsToChoose.check(rb[0].getId());
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                .setTitle("Valitse maatila ID:n perusteella")
                .setPositiveButton("Tallenna", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Dialog f = (Dialog) dialog;
                        int foundID = farmsToChoose.getCheckedRadioButtonId();

                        RadioButton foundButton = (RadioButton) f.findViewById(foundID);
                        String fidFromButton = foundButton.getText().toString();

                        SharedPreferences.Editor e = mSharedPreferences.edit();
                        e.putString(PREF_FID, fidFromButton);
                        e.apply();

                        app.setfID(fidFromButton);
                        new JSONCorrectUser().execute(fidFromButton);

                        Intent intent = new Intent(getApplicationContext(), FragmentHolder.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Peruuta", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Älä tee mitään
                    }
                });
        builder.create().show();
    }
}
