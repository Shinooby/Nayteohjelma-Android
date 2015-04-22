package siirtyminenfragmenteilla.app.Viewpagerin_fragmentit;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;

import siirtyminenfragmenteilla.app.AsyncClass;
import siirtyminenfragmenteilla.app.CAMperustiedot;
import siirtyminenfragmenteilla.app.R;
import siirtyminenfragmenteilla.app.apuluokat.ApprovalStates;
import siirtyminenfragmenteilla.app.apuluokat.MyApp;
import siirtyminenfragmenteilla.app.kasvituotannon_alisivut.Kalkitus;
import siirtyminenfragmenteilla.app.kasvituotannon_alisivut.KasvinSuojelu;


public class kasvi_frag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private JSONArray arrayToReceive;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private ImageButton perusMuokkaus;
    private ImageButton kylvoMuokkaus;
    private ImageButton lannoitus;
    private ImageButton kylvö;
    private ImageButton kasvinSuojelu;
    private ImageButton viljasadonKorjuu;
    private ImageButton nurmisadonKorjuu;
    private ImageButton sadonJälkikäsittely;
    private ImageButton kalkitus;

    private TextView arviointiOtsikko;
    private TextView arvioinninLisätietoa;

    private String wantedSection = "kasvituotanto";

    MyApp app = new MyApp();

    private static final String PREFS = "prefs";
    //talletettavat tiedot
    public static final String PREF_NAME = "name";
    public static final String PREF_PASSWORD = "password";
    public static final String PREF_FID = "fid";
    public SharedPreferences mSharedPreferences;

    private String itemID;

    private ApprovalStates[] arrayOfApprovals;

    public static kasvi_frag newInstance(String param1) {
        kasvi_frag fragment = new kasvi_frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    public kasvi_frag() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
        setCredentialsFromPreferences();
    }

    private void setCredentialsFromPreferences() {
        // avataan yhteys preferensseihin
        mSharedPreferences = this.getActivity().getSharedPreferences(PREFS, Context.MODE_PRIVATE);

        //luetaan käyttäjän nimi ja salasana
        String familiarUser = mSharedPreferences.getString(PREF_NAME, "");
        String familiarPassword = mSharedPreferences.getString(PREF_PASSWORD, "");
        String fIDfromPref = mSharedPreferences.getString(PREF_FID, "");

        app.setUsername(familiarUser);
        app.setPassword(familiarPassword);
        app.setfID(fIDfromPref);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.kasvituotanto, container, false);

        perusMuokkaus = (ImageButton) v.findViewById(R.id.perusmuokkausButton);
        kylvoMuokkaus = (ImageButton) v.findViewById(R.id.kylvomuokkausButton);
        lannoitus = (ImageButton) v.findViewById(R.id.lannoitusButton);
        kylvö = (ImageButton) v.findViewById(R.id.kylvoButton);
        kasvinSuojelu = (ImageButton) v.findViewById(R.id.kasvinSuojeluButton);
        viljasadonKorjuu = (ImageButton) v.findViewById(R.id.viljasadonKorkuuButton);
        nurmisadonKorjuu = (ImageButton) v.findViewById(R.id.nurmisadonKorjuuButton);
        sadonJälkikäsittely = (ImageButton) v.findViewById(R.id.sadonJatkokäsittelyButton);
        kalkitus = (ImageButton) v.findViewById(R.id.kalkitusButton);

        perusMuokkaus.setOnClickListener(onClickListener);
        kylvoMuokkaus.setOnClickListener(onClickListener);
        lannoitus.setOnClickListener(onClickListener);
        kylvö.setOnClickListener(onClickListener);
        kasvinSuojelu.setOnClickListener(onClickListener);
        viljasadonKorjuu.setOnClickListener(onClickListener);
        nurmisadonKorjuu.setOnClickListener(onClickListener);
        sadonJälkikäsittely.setOnClickListener(onClickListener);
        kalkitus.setOnClickListener(onClickListener);

        arviointiOtsikko = (TextView) v.findViewById(R.id.kasvituotannonArviointiOtsikko);
        arvioinninLisätietoa = (TextView) v.findViewById(R.id.kasvituotannonArviointiLisätietoa);

        if (arrayOfApprovals == null) {
            Log.d("arrayofApprovalsin koko", "On null --> Tehdään uusi haku ");
            new JSONApproval().execute();
        }  else {
            Log.d("arrayofApprovalsin koko", "Ei ole null --> Asetetaan tiedot Arrayn perusteella ");
            updateApproval(arrayOfApprovals);
        }
        return v;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            JSONGetPRImageInfo task;

            switch (v.getId()) {
                case R.id.perusmuokkausButton:
                    Log.d("Kasvituotanto", "Perusmuokkaus Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.kylvomuokkausButton:
                    Log.d("Kasvituotanto", "Kylvömuokkaus Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.lannoitusButton:
                    Log.d("Kasvituotanto", "lannoitus Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.kylvoButton:
                    Log.d("Kasvituotanto", "Kylvö Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.kasvinSuojeluButton:
                    Log.d("Kasvituotanto", "Kasvinsuojelu Button");
                    Intent intentKasvinsuojelu = new Intent(getActivity(), KasvinSuojelu.class);
                    startActivity(intentKasvinsuojelu);
                    break;
                case R.id.viljasadonKorkuuButton:
                    Log.d("Kasvituotanto", "Viljasadonkorjuu Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.nurmisadonKorjuuButton:
                    Log.d("Kasvituotanto", "Nurmisadonkorjuu Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.sadonJatkokäsittelyButton:
                    Log.d("Kasvituotanto", "Sadon Jatkokäsittely Button");
                    itemID = "IDforThisPage";
                    task = new JSONGetPRImageInfo();
                    task.execute(itemID);
                    break;
                case R.id.kalkitusButton:
                    Log.d("Kasvituotanto", "Kalkitus Button");
                    Intent intentKalkitus = new Intent(getActivity(), Kalkitus.class);
                    startActivity(intentKalkitus);
                    break;
            }
        }
    };


    private class JSONApproval extends AsyncTask<String, String, ApprovalStates[]> {

        @Override
        protected ApprovalStates[] doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            jsonClass.setUsername(app.getUsername());
            jsonClass.setPassword(app.getPassword());
            jsonClass.setfID(app.getfID());

            ObjectMapper mapper = new ObjectMapper();

            ApprovalStates[] approval = null;

            JSONArray jarray = null;
            try {
                jarray = jsonClass.letsGetEvaluation();

                approval = mapper.readValue(jarray.toString().getBytes(), ApprovalStates[].class);
                String test = approval[2].getSaSection();

                Log.d("Testaus", "ID  = " + test);
            } catch (Exception e) {
                Log.d("Tapahtui virhe", e.toString());
            }
            return approval;
        }

        @Override
        protected void onPostExecute(ApprovalStates[] json) {
            super.onPostExecute(json);
            arrayOfApprovals = json;
            updateApproval(json);
        }
    }

    private void updateApproval(ApprovalStates[] jarray) {

        Log.d("Kutsu vastaanotettu", "");
        if (jarray != null) {
            for (int i = 0; i < jarray.length; i++) {

                if (jarray[i].getSaSection().equals(wantedSection)) {
                    String status = jarray[i].getSaStatus();
                    Log.d("Statuksen debuggaus", "status = " + status);

                    if (status.equals("complete")) {
                        arviointiOtsikko.setText("Täydennettävää");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.red));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                    if (status.equals("accepted")) {
                        arviointiOtsikko.setText("Hyväksytty");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.green));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                    if (status.equals("nothandled")) {
                        arviointiOtsikko.setText("Ei käsitelty");
                        arviointiOtsikko.setTextColor(getResources().getColor(R.color.Gray));
                        arvioinninLisätietoa.setText(jarray[i].getSaInfo());
                        break;
                    }
                }
                Log.d("For-lauseen tarkkailu", "i = " + i);
            }
        }
    }

    private class JSONGetPRImageInfo extends AsyncTask<String, String, JSONArray> {

        @Override
        protected JSONArray doInBackground(String... strings) {
            AsyncClass jsonClass = new AsyncClass();
            String itemID = strings[0];
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
            if (json != null) {
                arrayToReceive = json;
                Intent intentForCameraActivities = new Intent(getActivity().getApplicationContext(), CAMperustiedot.class);
                intentForCameraActivities.putExtra("ID_TO_GET", itemID);
                intentForCameraActivities.putExtra("jsonarray", arrayToReceive.toString());
                startActivity(intentForCameraActivities);
            }
            else {
                Intent intentForCameraActivities = new Intent(getActivity().getApplicationContext(), CAMperustiedot.class);
                startActivity(intentForCameraActivities);
            }
        }
    }
}