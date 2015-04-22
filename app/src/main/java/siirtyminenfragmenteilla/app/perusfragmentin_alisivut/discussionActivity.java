package siirtyminenfragmenteilla.app.perusfragmentin_alisivut;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.apuluokat.discussions;


public class discussionActivity extends Activity {

    private EditText newMessage;
    private Button sendMessage;

     ListView discussionsView;
     ArrayAdapter mArrayAdapter;
     ArrayList mDiscussionList;

    private String tableName = "tableName";
    private String DiscussionOrWorkTime = "Discussion";

    MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public static final String PREF_REALNAME = "realname";
    public SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   setTheme(R.style.BlackTheme);
        setContentView(R.layout.activity_discussion);

        setCredentialsFromPreferences();

        newMessage = (EditText) findViewById(R.id.newMessageLayout);
        sendMessage = (Button) findViewById(R.id.buttonToSend);

        mDiscussionList = new ArrayList<Map<String, String>>();

        discussionsView = (ListView) findViewById(R.id.listviewDiscussionsLayout);

        mArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                mDiscussionList);

        discussionsView.setAdapter(mArrayAdapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Lisää listViewhin uusi itemi jossa newMessage:n teksti
                String message = newMessage.getText().toString();

                if (!message.equals("") && message.matches("[a-zA-ZÄ-Öä-öåÅ0-9/?.,: _-]*"))
                {
                    Log.d("Keskusteviestin sisältö", message);
                    String attribute = "currentAttribute";

                    JSONSendMessage jsonToUpdate = new JSONSendMessage();
                    jsonToUpdate.execute(attribute, message);

                    newMessage.setText("");
                } else
                    Toast.makeText(getApplicationContext(), "Tarkista ettei viestisi ole tyhjä \n tai sisällä seuraavia " +
                            "erikoismerkkejä: \"  \' ;", Toast.LENGTH_LONG).show();
            }
        });

        new JSONDiscussionsGetAndUpdate().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.discussion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    // Async-luokka joka hakee JSONin serveriltä
    private class JSONDiscussionsGetAndUpdate extends AsyncTask<String, String, discussions[]> {

        @Override
        protected discussions[] doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());
            jsonClass.setName(app.getRealname());
            ObjectMapper mapper = new ObjectMapper();

            discussions[] listOfDiscussions = null;

            JSONArray jarray = null;
            try {
                jarray = jsonClass.letsGetDiscussionsAndWorkTime(DiscussionOrWorkTime);

                listOfDiscussions = mapper.readValue(jarray.toString().getBytes(), discussions[].class);

            } catch (Exception e) {
                Log.d("Tapahtui virhe", e.toString());
            }
            return listOfDiscussions;
        }

        @Override
        protected void onPostExecute(discussions[] json) {
            super.onPostExecute(json);
            if (json != null) {

                 updateDiscussions(json);
            }
        }
    }

    public void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(mSharedPreferences.getString(PREF_FID, ""));
        app.setRealname(mSharedPreferences.getString(PREF_REALNAME, ""));
    }

    private void updateDiscussions(discussions[] json) {

        if ( json != null) {
            Log.d("JSONin koko", ""  + json.length);
            for (int i = 0; i < json.length; i++) {
                String newMessage = json[i].getDiText();
                mDiscussionList.add(i, newMessage);
            }
            mArrayAdapter.notifyDataSetChanged();
        }
    }

    private class JSONSendMessage extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());
            jsonClass.setName(app.getRealname());
            String attribute = strings[0];
            String value = strings[1];

            Log.d("Parametrien debuggaus", "attribute: " + attribute + " value: " + value);

            try {
                jsonClass.letsInsertPRTextItem(tableName, attribute, value);
            } catch (Exception e) {
                Log.d("Tapahtui virhe", "...");
                e.printStackTrace();
            }
            String newMessage = "[" + app.getRealname() + "] " + value;
            mDiscussionList.add(newMessage);

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            super.onPostExecute(json);

            mArrayAdapter.notifyDataSetChanged();
        }
    }
}

